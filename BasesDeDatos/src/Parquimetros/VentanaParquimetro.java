package Parquimetros;

import java.sql.Connection;
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
import com.mysql.cj.jdbc.CallableStatement;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class VentanaParquimetro extends javax.swing.JFrame {

	private JScrollPane scroll_tarjetas;
	private JScrollPane scroll_parquimetros;
	private DefaultListModel<Object> model_parquimetros;
	private DefaultListModel<Object> model_tarjetas;
	private DefaultListModel<Object> model_ubicaciones;
	private JList<Object> lista_parquimetros;
	private JList<Object> lista_tarjetas;
	private JList<Object> lista_ubicaciones;
	private JButton btnConfirmar;
	private JButton btnCerrar;
	private JTextPane textPane;
	private JLabel labelTarjetas;
	private JLabel labelUbicaciones;
	private JLabel labelParquimetros;
	private CallableStatement cs;
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
		labelParquimetros.setBounds(457, 134, 160, 20);
		getContentPane().add(labelParquimetros);

		labelTarjetas = new JLabel("Tarjetas");
		labelTarjetas.setHorizontalAlignment(SwingConstants.CENTER);
		labelTarjetas.setFont(new Font("Tahoma", Font.BOLD, 14));
		labelTarjetas.setBounds(457, 251, 160, 20);
		getContentPane().add(labelTarjetas);

		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBounds(22, 37, 413, 392);
		getContentPane().add(textPane);

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

		btnConfirmar = new JButton("Confirmar");
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (lista_parquimetros.getSelectedValue() == null || lista_parquimetros.getSelectedValue() == null
							|| lista_parquimetros.getSelectedValue() == null) 
						JOptionPane.showMessageDialog(null, "Seleccione una ubicacion, un parquimetro y una tarjeta", "ERROR", 0);
					else {
						int parq = Integer.parseInt((String) lista_parquimetros.getSelectedValue());
						String idParq = obtenerIDParquimetro(parq);
						String tarj = (String) lista_tarjetas.getSelectedValue();
						if (idParq != null && tarj != null) {
							cs = (CallableStatement) conexionBD.prepareCall("{call conectar(?,?)}");
							cs.setString(1, tarj);
							cs.setString(2, idParq);
							storeprocedure(cs);
							lista_parquimetros.clearSelection();
							lista_ubicaciones.clearSelection();
							lista_tarjetas.clearSelection();
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		btnConfirmar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConfirmar.setBounds(457, 362, 160, 20);

		getContentPane().add(btnConfirmar);
		getContentPane().add(btnCerrar);
	}

	private void storeprocedure(CallableStatement cs) {
		String s = "";
		try {
			ResultSet rs = cs.executeQuery();
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			int col = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= col; i++) {
					String columnValue = rs.getString(i);
					s += rsmd.getColumnName(i) + ": " + columnValue + "\n";
				}
				s += " ";
			}
			textPane.setText(s);

		} catch (SQLException e) {
			e.printStackTrace();
		}
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
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String obtenerIDParquimetro(int numero) {
		String id = null;
		try {
			String sql = "SELECT id_parq FROM parquimetros WHERE numero = '" + numero + "'";

			Statement stmt = this.conexionBD.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				id = rs.getString("id_parq");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
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
