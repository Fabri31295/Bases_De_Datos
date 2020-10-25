package Parquimetros;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class VentanaValidacion extends javax.swing.JFrame{
	
	protected Connection conexionBD = null;
	
	private JLabel labelUsuario;
	private JLabel labelContrase�a;
	private JTextField textUsuario;
	private JPasswordField textContrase�a;

	private JButton btnAceptar;

	
	public VentanaValidacion() {
		super();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		this.setBounds(200, 200, 300, 170);
		getContentPane().setLayout(null);
		this.setTitle("Autenticaci�n");
		this.setResizable(true);
		this.setVisible(false);
		this.setLocationRelativeTo(null);
		
		labelUsuario = new JLabel("Usuario");
		labelUsuario.setBounds(28, 23, 76, 14);
		getContentPane().add(labelUsuario);
		
		labelContrase�a = new JLabel("Contrase\u00F1a");
		labelContrase�a.setBounds(28, 61, 76, 14);
		getContentPane().add(labelContrase�a);
		
		textUsuario = new JTextField();
		textUsuario.setBounds(166, 20, 86, 20);
		getContentPane().add(textUsuario);
		textUsuario.setColumns(10);
		
		textContrase�a = new JPasswordField();
		textContrase�a.setBounds(166, 58, 86, 20);
		getContentPane().add(textContrase�a);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aceptar();
			}
		});
		btnAceptar.setBounds(95, 100, 86, 20);
		getContentPane().add(btnAceptar);
	}
	
	private void aceptar() {
		String userIngresado = textUsuario.getText();
		String passwordIngresado = new String(textContrase�a.getPassword());
		
		conectarBD(userIngresado,passwordIngresado);
		
		if(conexionBD != null) {
			setVisible(false);
			VentanaConsultas vc = new VentanaConsultas (conexionBD);
			vc.llenarListaTablas();
			VentanaPrincipal vp = new VentanaPrincipal();
			vp.setVisible(false);
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
	        	 JOptionPane.showMessageDialog(null, "Usuario y/o contrase�a incorrecto", "ERROR", 0);
	         }
	      }
	     return false;
	   }
}
