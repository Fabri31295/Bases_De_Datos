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
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.Scrollbar;
import java.awt.ScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import java.awt.Rectangle;
import javax.swing.JTable;
import javax.swing.JSeparator;

public class VentanaConsultas extends javax.swing.JFrame{

	protected Connection conexionBD = null;

	private static VentanaConsultas instancia;
	
	private JTextArea textConsulta;
	private JButton btnConsultar;
	private JButton btnLimpiar;
	private ScrollPane scrollPaneTablas;
	private ScrollPane scrollPaneAtributos;
	private JList listaTablas;
	private JList listaAtributos;
	private JLabel labelTablas;
	private JLabel labelAtributos;
	private ScrollPane scrollPaneConsulta;
	private JTable tablaConsulta;
	private JSeparator separatorHorizontal;
	private JSeparator separatorVertical;
	
	private DefaultListModel modelTablas;
	private DefaultListModel modelAtributos;
	private DefaultTableModel modelConsulta;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaConsultas window = new VentanaConsultas();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static VentanaConsultas getInstancia() {
		if(instancia==null) {
			instancia = new VentanaConsultas();
		}
		return instancia;
	}
	
	private VentanaConsultas() {
		super();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBounds(100, 100, 640, 480);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		this.setTitle("Consultas");
		this.setResizable(true);
		
		textConsulta = new JTextArea();
		textConsulta.setBounds(10, 11, 388, 74);
		getContentPane().add(textConsulta);
		
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
		
		scrollPaneConsulta = new ScrollPane();
		scrollPaneConsulta.setBounds(10, 125, 388, 306);
		getContentPane().add(scrollPaneConsulta);
		
		tablaConsulta = new JTable();
		tablaConsulta.setBounds(10, 125, 388, 305);
		scrollPaneConsulta.add(tablaConsulta);
		
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
			@Override
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
				VentanaPrincipal v = VentanaPrincipal.getInstancia();
				v.setVisible(true);
				setVisible(false);
				desconectarBD();
			}
		});
		btnCerrarSesion.setBounds(454, 384, 160, 35);
		getContentPane().add(btnCerrarSesion);
		
		
	}
	
	private void limpiarTextArea() {
		textConsulta.setText("");
	}
	
	private void limpiarTabla() {
		modelConsulta.setColumnCount(0);
		modelConsulta.setRowCount(0);
	}
	

	public void llenarListaTablas() {
		try {
			conectarBD();
			modelTablas = new DefaultListModel();
			if(conexionBD == null)
				System.out.println("Null");
			DatabaseMetaData metaDatos = conexionBD.getMetaData();
			ResultSet rs = metaDatos.getTables("parquimetros", null, "%", null);
			while(rs.next()) {
				modelTablas.addElement(rs.getString(3));
			}
			listaTablas.setModel(modelTablas);
			this.setVisible(true);
			desconectarBD();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void obtenerAtributosTabla() {
		try {
			conectarBD();
			modelAtributos = new DefaultListModel();
			if(conexionBD == null)
				System.out.println("Null");
			String tablaSeleccionada = (String) listaTablas.getSelectedValue();
			DatabaseMetaData metaDatos = conexionBD.getMetaData();
			ResultSet rs = metaDatos.getColumns(null, null, tablaSeleccionada, null);
			while(rs.next()) {
				modelAtributos.addElement(rs.getString(4));
			}
			listaAtributos.setModel(modelAtributos);
			desconectarBD();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	private void obtenerTabla() {
		try {
			conectarBD();
			modelConsulta = new DefaultTableModel();
			String sql = textConsulta.getText();
			Statement stmt = this.conexionBD.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			for(int i=1; i<=columnCount; i++) {
				modelConsulta.addColumn(rsmd.getColumnClassName(i));
			}
		

			while(rs.next()) {
				Object[] filas = new Object[columnCount];
				for(int i=0;i<columnCount;i++) {
					filas[i]= rs.getObject(i+1);
				}
				modelConsulta.addRow(filas);	
			}
			
			tablaConsulta.setModel(modelConsulta);
			desconectarBD();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void conectarBD()
	   {
	      if (conexionBD == null)
	      {             
	         try
	         {  
	            String servidor = "localhost:3306";
	            String baseDatos = "parquimetros";
	            String usuario = "admin";
	            String clave = "admin";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos + 
	            		          "?serverTimezone=America/Argentina/Buenos_Aires";
	            conexionBD = DriverManager.getConnection(uriConexion, usuario, clave);
	         }
	         catch (SQLException ex)
	         {
	        	 JOptionPane.showMessageDialog(null, ex.getMessage());
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }
	      }
	   }

	   private void desconectarBD()
	   {
	      if (this.conexionBD != null)
	      {
	         try
	         {
	            this.conexionBD.close();
	            this.conexionBD = null;
	         }
	         catch (SQLException ex)
	         {
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }
	      }
	   }
}
