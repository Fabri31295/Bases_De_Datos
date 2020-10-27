package Parquimetros;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.Scrollbar;
import java.awt.ScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import quick.dbtable.*;

import java.awt.Rectangle;
import javax.swing.JTable;
import javax.swing.JSeparator;
import java.awt.Panel;
import javax.swing.JEditorPane;
import java.awt.Component;
import java.awt.ComponentOrientation;

public class VentanaConsultas extends javax.swing.JFrame {

	protected Connection conexionBD = null;

	private JTextArea textConsulta;
	private JButton btnConsultar;
	private JButton btnLimpiar;
	private ScrollPane scrollPaneTablas;
	private ScrollPane scrollPaneAtributos;
	private JList listaTablas;
	private JList listaAtributos;
	private JLabel labelTablas;
	private JLabel labelAtributos;
	private JSeparator separatorHorizontal;
	private JSeparator separatorVertical;
	private DBTable tablaConsultas;

	private DefaultListModel modelTablas;
	private DefaultListModel modelAtributos;

	public VentanaConsultas(Connection c) {
		super();
		conexionBD = c;
		initialize();
		llenarListaTablas();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		this.setBounds(100, 100, 640, 480);
		getContentPane().setLayout(null);
		this.setLocationRelativeTo(null);
		this.setTitle("Consultas");
		this.setResizable(false);

		textConsulta = new JTextArea();
		textConsulta.setBounds(10, 11, 388, 74);
		getContentPane().add(textConsulta);

		tablaConsultas = new DBTable();
		tablaConsultas.setBounds(10, 125, 388, 306);
		getContentPane().add(tablaConsultas);
		tablaConsultas.setEditable(false);

		btnConsultar = new JButton("Consultar");
		btnConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				obtenerTabla();
			}
		});
		btnConsultar.setBounds(454, 12, 160, 35);
		getContentPane().add(btnConsultar);

		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpiarTextArea();
				limpiarTabla();
			}
		});

		btnLimpiar.setBounds(454, 62, 160, 35);
		getContentPane().add(btnLimpiar);

		scrollPaneTablas = new ScrollPane();
		scrollPaneTablas.setBounds(new Rectangle(0, 0, 200, 200));
		scrollPaneTablas.setBounds(454, 127, 160, 110);
		getContentPane().add(scrollPaneTablas);

		scrollPaneAtributos = new ScrollPane();
		scrollPaneAtributos.setBounds(new Rectangle(0, 0, 200, 200));
		scrollPaneAtributos.setBounds(454, 263, 160, 110);
		getContentPane().add(scrollPaneAtributos);

		labelTablas = new JLabel("Tablas");
		labelTablas.setHorizontalAlignment(SwingConstants.CENTER);
		labelTablas.setBounds(454, 107, 160, 14);
		getContentPane().add(labelTablas);

		labelAtributos = new JLabel("Atributos");
		labelAtributos.setHorizontalAlignment(SwingConstants.CENTER);
		labelAtributos.setBounds(454, 243, 160, 14);
		getContentPane().add(labelAtributos);

		separatorHorizontal = new JSeparator();
		separatorHorizontal.setBounds(10, 107, 388, 2);
		getContentPane().add(separatorHorizontal);

		separatorVertical = new JSeparator();
		separatorVertical.setOrientation(SwingConstants.VERTICAL);
		separatorVertical.setBounds(426, 24, 19, 407);
		getContentPane().add(separatorVertical);

		listaTablas = new JList();
		scrollPaneTablas.add(listaTablas);
		listaTablas.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				obtenerAtributosTabla();
			}
		});
		listaTablas.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listaTablas.setBounds(454, 127, 156, 110);

		listaAtributos = new JList();
		scrollPaneAtributos.add(listaAtributos);
		listaAtributos.setBounds(454, 263, 156, 110);

		JButton btnCerrarSesion = new JButton("Cerrar Sesion");
		btnCerrarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				desconectarBD();
				setVisible(false);
				limpiarVentanaConsultas();
				VentanaPrincipal p = VentanaPrincipal.getInstancia();
				p.setVisible(true);
			}
		});
		btnCerrarSesion.setBounds(454, 384, 160, 35);
		getContentPane().add(btnCerrarSesion);

		modelTablas = new DefaultListModel();
		modelAtributos = new DefaultListModel();
	}

	private void limpiarVentanaConsultas() {
		limpiarTextArea();
		limpiarTabla();
		limpiarAtributos();

	}

	private void limpiarTextArea() {
		textConsulta.setText("");
	}

	private void limpiarTabla() {
		tablaConsultas.removeAllRows();
	}

	private void limpiarAtributos() {
		modelAtributos.removeAllElements();
	}

	public void llenarListaTablas() {
		try {

			if (conexionBD == null)
				System.out.println("Null");

			DatabaseMetaData metaDatos = conexionBD.getMetaData();
			ResultSet rs = metaDatos.getTables("parquimetros", null, "%", null);

			while (rs.next()) {
				modelTablas.addElement(rs.getString(3));
			}

			listaTablas.setModel(modelTablas);
			this.setVisible(true);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void obtenerAtributosTabla() {
		try {

			if (conexionBD == null)
				System.out.println("Null");

			String tablaSeleccionada = (String) listaTablas.getSelectedValue();
			DatabaseMetaData metaDatos = conexionBD.getMetaData();
			ResultSet rs = metaDatos.getColumns(null, null, tablaSeleccionada, null);
			modelAtributos.removeAllElements();

			while (rs.next()) {
				modelAtributos.addElement(rs.getString(4));
			}

			listaAtributos.setModel(modelAtributos);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void obtenerTabla() {
		try {

			String sql = textConsulta.getText();
			Statement stmt = this.conexionBD.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			tablaConsultas.refresh(rs);
			for(int i = 0; i < tablaConsultas.getColumnCount(); i++) {
				if (tablaConsultas.getColumn(i).getType() == Types.TIME) {
					tablaConsultas.getColumn(i).setType(Types.CHAR);
				}
				if (tablaConsultas.getColumn(i).getType() == Types.DATE) {
					tablaConsultas.getColumn(i).setDateFormat("dd/MM/YYYY");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Consulta incorrecta", "ERROR", 0);
		}
	}

	private void conectarBD(String user, String password) {
		if (conexionBD == null) {
			try {

				String servidor = "localhost:3306";
				String baseDatos = "parquimetros";
				String usuario = user;
				String clave = password;
				String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos
						+ "?serverTimezone=America/Argentina/Buenos_Aires";

				conexionBD = DriverManager.getConnection(uriConexion, usuario, clave);

			} catch (SQLException ex) {

			}
		}
	}

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
