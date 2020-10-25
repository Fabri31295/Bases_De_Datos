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


public class ValidacionInspector extends javax.swing.JFrame{

	protected Connection conexionBD = null;

	private JLabel labelLegajo;
	private JLabel labelPassword;
	private JTextField textLegajo;
	private JPasswordField textPassword;

	private JButton btnAceptar;


	public ValidacionInspector() {
		super();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		this.setBounds(200, 200, 300, 170);
		getContentPane().setLayout(null);
		this.setTitle("Autenticacion");
		this.setResizable(false);
		this.setVisible(false);
		this.setLocationRelativeTo(null);

		labelLegajo = new JLabel("Legajo");
		labelLegajo.setBounds(28, 23, 76, 14);
		getContentPane().add(labelLegajo);

		labelPassword = new JLabel("Password");
		labelPassword.setBounds(28, 61, 76, 14);
		getContentPane().add(labelPassword);

		textLegajo = new JTextField();
		textLegajo.setBounds(166, 20, 86, 20);
		getContentPane().add(textLegajo);
		textLegajo.setColumns(10);

		textPassword = new JPasswordField();
		textPassword.setBounds(166, 58, 86, 20);
		getContentPane().add(textPassword);

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
		String userIngresado = textLegajo.getText();
		String passwordIngresado = new String(textPassword.getPassword());

		conectarBD(userIngresado,passwordIngresado);

		if(conexionBD != null) {
			//setVisible(false);
			//VentanaInspector vc = new VentanaInspector(conexionBD);
		}

		textLegajo.setText("");
		textPassword.setText("");

	}

	private boolean conectarBD(String user, String password)
	   { return true;
	    /*  if (conexionBD == null)
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
	        	 JOptionPane.showMessageDialog(null, "Numero de legajo y/o contraseña incorrecto", "ERROR", 0);
	         }
	      }
	     return false;*/
	   }
}
