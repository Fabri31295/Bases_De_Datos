package Parquimetros;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class VentanaParquimetro extends javax.swing.JFrame {

	private JScrollPane scroll_tarjetas;
	private JScrollPane scroll_parquimetros;
	private JScrollPane scroll_informacion;
	private DefaultListModel<Object> model_parquimetros;
	private DefaultListModel<Object> model_tarjetas;
	private DefaultListModel<Object> model_ubicaciones;
	private JList<Object> lista_parquimetros;
	private JList<Object> lista_tarjetas;
	private JList<Object> lista_info;
	private JList<Object> lista_ubicaciones;
	private JButton btnConfirmar;
	private JButton btnCerrar;
	private JLabel labelTarjetas;
	private JLabel labelUbicaciones;
	private JLabel labelParquimetros;
	protected Connection conexionBD = null;

	/**
	 * Create the application.
	 */
	public VentanaParquimetro(Connection c) {
		conexionBD = c;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		inicializarPaneles();
		inicializarBotones();
		cargarUbicaciones();
		cargarTarjetas();

	}

	private void inicializarPaneles() {
		this.setVisible(true);
		this.setBounds(100, 100, 640, 480);
		getContentPane().setLayout(null);
		this.setLocationRelativeTo(null);
		this.setTitle("Parquimetro");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		scroll_tarjetas = new JScrollPane();
		scroll_tarjetas.setBounds(457, 272, 160, 86);
		getContentPane().add(scroll_tarjetas);

		lista_tarjetas = new JList<Object>();
		scroll_tarjetas.setViewportView(lista_tarjetas);

		scroll_parquimetros = new JScrollPane();
		scroll_parquimetros.setBounds(457, 157, 160, 86);
		getContentPane().add(scroll_parquimetros);
		
		lista_parquimetros = new JList<Object>();
		scroll_parquimetros.setViewportView(lista_parquimetros);
				
		labelUbicaciones = new JLabel("Ubicaciones");
		labelUbicaciones.setHorizontalAlignment(SwingConstants.CENTER);
		labelUbicaciones.setFont(new Font("Tahoma", Font.BOLD, 14));
		labelUbicaciones.setBounds(457, 19, 160, 20);
		getContentPane().add(labelUbicaciones);	
				
		labelParquimetros = new JLabel("Parquimetros");
		labelParquimetros.setHorizontalAlignment(SwingConstants.CENTER);
		labelParquimetros.setFont(new Font("Tahoma", Font.BOLD, 14));
		labelParquimetros.setBounds(457, 138, 160, 20);
		getContentPane().add(labelParquimetros);

		labelTarjetas = new JLabel("Tarjetas");
		labelTarjetas.setHorizontalAlignment(SwingConstants.CENTER);
		labelTarjetas.setFont(new Font("Tahoma", Font.BOLD, 14));
		labelTarjetas.setBounds(455, 253, 160, 20);
		getContentPane().add(labelTarjetas);

		scroll_informacion = new JScrollPane();
		scroll_informacion.setBounds(26, 24, 409, 398);
		getContentPane().add(scroll_informacion);

		lista_info = new JList<Object>();
		scroll_informacion.setViewportView(lista_info);
		
		JScrollPane scroll_ubicaciones = new JScrollPane();
		scroll_ubicaciones.setBounds(457, 37, 160, 86);
		getContentPane().add(scroll_ubicaciones);
		
		lista_ubicaciones = new JList<Object>();
		scroll_ubicaciones.setViewportView(lista_ubicaciones);
		lista_ubicaciones.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				obtenerParquimetrosAsociados();
			}
		});

		model_parquimetros = new DefaultListModel<Object>();
		model_ubicaciones = new DefaultListModel<Object>();
		model_tarjetas = new DefaultListModel<Object>();
	}

	private void inicializarBotones() {

		btnCerrar = new JButton("Cerrar Sesion");
		btnCerrar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCerrar.setBounds(457, 393, 160, 29);
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desconectarBD();
				dispose();
			}
		});

		getContentPane().add(btnCerrar);

		btnConfirmar = new JButton("Confirmar");
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				
				/*
				 * 
				 * Zona en construccion
				 * 
				 */
				
				JOptionPane.showMessageDialog(null, "ACA VA LO DEL STORED PROCEDURE", "ERROR", 0);
			}
		});
		btnConfirmar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConfirmar.setBounds(457, 357, 160, 20);
		getContentPane().add(btnConfirmar);
		
	}

	private void cargarUbicaciones() {
		try {
			String sql = "SELECT DISTINCT calle, altura FROM parquimetros";
			Statement stmt = this.conexionBD.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				model_ubicaciones.add(0, rs.getString("calle") + " " + rs.getString("altura"));
			}
			lista_ubicaciones.setModel(model_ubicaciones);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void obtenerParquimetrosAsociados() {
		try {
			String ubicacionSeleccionada = (String) lista_ubicaciones.getSelectedValue();
			String[] words = ubicacionSeleccionada.split(" ", 2);
			String calle = words[0];
			String altura = words[1];
			String sql = "SELECT numero FROM parquimetros WHERE calle = '" + calle + "' AND altura = '" + altura + "'";

			Statement stmt = this.conexionBD.createStatement();
			ResultSet rs = stmt.executeQuery(sql);


			model_parquimetros.removeAllElements();
			while (rs.next()) {
				model_parquimetros.add(0, rs.getString("numero"));
			}
			lista_parquimetros.setModel(model_parquimetros);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void cargarTarjetas() {
		try {
			String sql = "SELECT id_tarjeta FROM tarjetas";
			Statement stmt = this.conexionBD.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				model_tarjetas.add(0, rs.getString("id_tarjeta"));
			}
			lista_tarjetas.setModel(model_tarjetas);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	/*
	 * Cierra la conexion a la base de datos
	 */
	private void desconectarBD() {
		if (this.conexionBD != null) {
			try {
				this.conexionBD.close();
				this.conexionBD = null;
			} catch (SQLException ex) {
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
	}
}
