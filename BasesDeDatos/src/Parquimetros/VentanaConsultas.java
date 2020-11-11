/*
 * @autores: Almaraz Fabricio, Pacione Luciano
 * 
 */
package Parquimetros;

import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.awt.event.ActionEvent;
import java.awt.ScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import quick.dbtable.*;
import java.awt.Rectangle;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class VentanaConsultas extends javax.swing.JFrame {

	protected Connection conexionBD = null;

	private JTextArea textConsulta;
	private JButton btnEjecutar;
	private JButton btnLimpiar;
	private ScrollPane scrollPaneTablas;
	private ScrollPane scrollPaneAtributos;
	private JList<Object> listaTablas;
	private JList<Object> listaAtributos;
	private JLabel labelTablas;
	private JLabel labelAtributos;
	private JSeparator separatorHorizontal;
	private JSeparator separatorVertical;
	private DBTable tablaConsultas;
	private DefaultListModel<Object> modelTablas;
	private DefaultListModel<Object> modelAtributos;

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
		textConsulta.setLineWrap(true);
		textConsulta.setBounds(10, 11, 388, 74);
		getContentPane().add(textConsulta);

		tablaConsultas = new DBTable();
		tablaConsultas.setBounds(10, 125, 388, 306);
		getContentPane().add(tablaConsultas);
		tablaConsultas.setEditable(false);

		btnEjecutar = new JButton("Ejecutar");
		btnEjecutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				examinarConsulta();
			}
		});
		btnEjecutar.setBounds(454, 12, 160, 35);
		getContentPane().add(btnEjecutar);

		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpiarTextArea();
				limpiarTabla();
			}
		});

		btnLimpiar.setBounds(454, 60, 160, 35);
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

		listaTablas = new JList<Object>();
		scrollPaneTablas.add(listaTablas);
		listaTablas.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				obtenerAtributosTabla();
			}
		});
		listaTablas.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listaTablas.setBounds(454, 127, 156, 110);

		listaAtributos = new JList<Object>();
		scrollPaneAtributos.add(listaAtributos);
		listaAtributos.setBounds(454, 263, 156, 110);

		JButton btnCerrarSesion = new JButton("Cerrar Sesion");
		btnCerrarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				desconectarBD();
				setVisible(false);
				limpiarVentanaConsultas();
				dispose();
			}
		});
		btnCerrarSesion.setBounds(454, 384, 160, 35);
		getContentPane().add(btnCerrarSesion);

		modelTablas = new DefaultListModel<Object>();
		modelAtributos = new DefaultListModel<Object>();
	}

	/*
	 * Limpia la ventana completamente cuando se presiona el boton "Limpiar"
	 */
	private void limpiarVentanaConsultas() {
		limpiarTextArea();
		limpiarTabla();
		limpiarAtributos();

	}

	/*
	 * Limpia el campo donde se ingresa la consulta
	 */
	private void limpiarTextArea() {
		textConsulta.setText("");
	}

	/*
	 * Limpia la tabla donde se muestra el resultado de la consulta
	 */
	private void limpiarTabla() {
		tablaConsultas.removeAllRows();
	}

	/*
	 * Limpia la lista donde se muestran los atributos de cada tabla
	 */
	private void limpiarAtributos() {
		modelAtributos.removeAllElements();
	}

	/*
	 * Obtiene las tablas de la base de datos y los agrega a la lista correspondiente
	 */
	public void llenarListaTablas() {
		try {

			DatabaseMetaData metaDatos = conexionBD.getMetaData();
			ResultSet rs = metaDatos.getTables("parquimetros", null, "%", null);

			while (rs.next()) {
				modelTablas.addElement(rs.getString(3));
			}

			listaTablas.setModel(modelTablas);
			this.setVisible(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Obtiene los atributos (columnas) de la tabla donde se hizo click y los agrega a la lista correspondiente
	 */
	private void obtenerAtributosTabla() {
		try {
			String tablaSeleccionada = (String) listaTablas.getSelectedValue();
			DatabaseMetaData metaDatos = conexionBD.getMetaData();
			ResultSet rs = metaDatos.getColumns(null, null, tablaSeleccionada, null);
			modelAtributos.removeAllElements();

			while (rs.next()) {
				modelAtributos.addElement(rs.getString(4));
			}

			listaAtributos.setModel(modelAtributos);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Se evalua si la consulta ingresada es para mostrar por tabla o para 
	 * insertar/eliminar/actualizar	un registro
	 */
	private void examinarConsulta() {
		try {
			String sql = textConsulta.getText().trim();
			if (!sql.equals("")) {
				String[] words = sql.split(" ", 2);
				String firstWord = words[0];
				if (firstWord.compareToIgnoreCase("Select") == 0)
					obtenerTabla(sql);
				else {
					Statement stmt = this.conexionBD.createStatement();
					stmt.execute(sql);
					JOptionPane.showMessageDialog(null, "Comando ejecutado con exito");
					limpiarTextArea();
					stmt.close();
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "No se pudo actualizar correctamente", "ERROR", 0);
		}
	}

	/*
	 * Se obtiene la tabla correspondiente a la consulta ingresada
	 */
	private void obtenerTabla(String sql) {
		try {

			Statement stmt = this.conexionBD.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			tablaConsultas.refresh(rs);
			for (int i = 0; i < tablaConsultas.getColumnCount(); i++) {
				if (tablaConsultas.getColumn(i).getType() == Types.TIME) {
					tablaConsultas.getColumn(i).setType(Types.CHAR);
				}
				if (tablaConsultas.getColumn(i).getType() == Types.DATE) {
					tablaConsultas.getColumn(i).setDateFormat("dd/MM/YYYY");
				}
			}
			stmt.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Consulta incorrecta", "ERROR", 0);
		}
	}

	/*
	 * Se cierra la conexion a la base de datos
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
