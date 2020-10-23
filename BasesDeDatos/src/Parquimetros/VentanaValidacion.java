package Parquimetros;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;


public class VentanaValidacion extends javax.swing.JFrame{
	
	protected Connection conexionBD = null;
	
	private static VentanaValidacion instancia;

	
	private JLabel labelUsuario;
	private JLabel labelContrase�a;
	private JTextField textUsuario;
	private JPasswordField textContrase�a;


	private JButton btnAceptar;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaValidacion window = new VentanaValidacion();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static VentanaValidacion getInstancia() {
		if(instancia==null) {
			instancia = new VentanaValidacion();
		}
		return instancia;
	}
	private VentanaValidacion() {
		super();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		this.setBounds(100, 100, 450, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		this.setTitle("Autenticaci�n");
		this.setResizable(true);
		this.setVisible(false);
		
		labelUsuario = new JLabel("Usuario");
		labelUsuario.setBounds(85, 59, 76, 14);
		getContentPane().add(labelUsuario);
		
		labelContrase�a = new JLabel("Contrase\u00F1a");
		labelContrase�a.setBounds(85, 118, 76, 14);
		getContentPane().add(labelContrase�a);
		
		textUsuario = new JTextField();
		textUsuario.setBounds(235, 56, 86, 20);
		getContentPane().add(textUsuario);
		textUsuario.setColumns(10);
		
		textContrase�a = new JPasswordField();
		textContrase�a.setBounds(235, 115, 86, 20);
		getContentPane().add(textContrase�a);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aceptar();
			}
		});
		btnAceptar.setBounds(156, 184, 86, 20);
		getContentPane().add(btnAceptar);
	}
	
	private void aceptar() {
		String userIngresado = textUsuario.getText();
		String passwordIngresado = new String(textContrase�a.getPassword());
		
		conectarBD(userIngresado,passwordIngresado);
		
		if(conexionBD != null) {
			setVisible(false);
			VentanaPrincipal p = VentanaPrincipal.getInstancia();
			p.setVisible(false);
			VentanaConsultas c = VentanaConsultas.getInstancia();
			c.llenarListaTablas();
			desconectarBD();
			
		}
		textUsuario.setText("");
		textContrase�a.setText("");
		
	}
	
	private boolean conectarBD(String user, String password)
	   {
	      if (conexionBD == null)
	      {             
	         try
	         {  
	            String servidor = "localhost:3306";
	            String baseDatos = "parquimetros";
	            String usuario = user;
	            String clave = password;
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos + 
	            		          "?serverTimezone=America/Argentina/Buenos_Aires";
	            conexionBD = DriverManager.getConnection(uriConexion, usuario, clave);
	            
	            return true;
	          
	         }
	         catch (SQLException ex)
	         {
	        	 JOptionPane.showMessageDialog(null, "Usuario o contrase�a incorrectos");
	         }
	      }
	     return false;
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
