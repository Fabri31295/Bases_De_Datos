package Parquimetros;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.sql.*;
import java.awt.Dimension;
import java.awt.Color;

public class VentanaConsultas {

	private JFrame frame;
	private JList listaTablas;
	private JList listaAtributos;
	private JTextArea textConsulta;
	
	
	protected Connection conexionBD = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaConsultas window = new VentanaConsultas();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VentanaConsultas() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize(){
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnConsultar = new JButton("Consultar");
		btnConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnConsultar.setBounds(313, 11, 89, 23);
		frame.getContentPane().add(btnConsultar);
		
		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpiarTextArea();
			}
		});
		btnLimpiar.setBounds(313, 63, 89, 23);
		frame.getContentPane().add(btnLimpiar);
		
		textConsulta = new JTextArea();
		textConsulta.setBounds(10, 10, 293, 76);
		frame.getContentPane().add(textConsulta);
		
	
		listaTablas = new JList();
		listaTablas.setBackground(Color.WHITE);
		listaTablas.setSize(new Dimension(50, 50));
		listaTablas.setValueIsAdjusting(true);
		listaTablas.setBounds(313, 160, 89, -50);
		frame.getContentPane().add(listaTablas);
		listarTablas();
	
		listaAtributos = new JList();
		listaTablas.setValueIsAdjusting(true);
		listaAtributos.setBounds(313, 242, 89, -50);
		frame.getContentPane().add(listaAtributos);
		DefaultListModel atr = new DefaultListModel();
	}
		/*	listarAtributos(atr);
		listaTablas.setModel(atr);
	}*/
		
	private void limpiarTextArea() {
		textConsulta.setText("");
	}
	
	 /*private void conectarBD()
	   {
	      if (this.conexionBD == null)
	      {             
	         try
	         {  //se genera el string que define los datos de la conexión 
	            String servidor = "localhost:3306";
	            String baseDatos = "datos";
	            String usuario = "admi";
	            String clave = "admin";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos + 
	            		          "?serverTimezone=America/Argentina/Buenos_Aires";
	            //se intenta establecer la conexión
	            this.conexionBD = DriverManager.getConnection(uriConexion, usuario, clave);
	         }
	         catch (SQLException ex)
	         {
	        	 /*JOptionPane.showMessageDialog(this,
	                        "Se produjo un error al intentar conectarse a la base de datos.\n" + 
	                         ex.getMessage(),
	                         "Error",
	                         JOptionPane.ERROR_MESSAGE);
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
	   }*/
	
	private void listarTablas() {
	/*	 try {
			DatabaseMetaData metaDatos = conexionBD.getMetaData();
			ResultSet tables = metaDatos.getTables(conexionBD.getCatalog(), null, "TAB_%", null);
			while (tables.next()) {
			     System.out.println(tables.getString(3));
			}
			listaTablas.setModel(tab);
			System.out.print(listaTablas);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
		
	}
		
		

	
	/*private void listarAtributos (DefaultListModel atr) {
		
	}
*/
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	
}
