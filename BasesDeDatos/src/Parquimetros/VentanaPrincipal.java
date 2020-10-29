/*
 * @autores: Almaraz Fabricio, Pacione Luciano
 * 
 */

package Parquimetros;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

@SuppressWarnings("serial")
public class VentanaPrincipal extends javax.swing.JFrame {

	
	private JTextField textUsuario;
	private JPasswordField textPassword;
	protected Connection conexionBD = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					new VentanaPrincipal().setVisible(true);
			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	private VentanaPrincipal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBounds(100, 100, 300, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		this.setTitle("PARQUIMETROS");
		this.setResizable(false);

		JButton btnAcceder = new JButton("Acceder");
		btnAcceder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acceder();
			}
		});
		btnAcceder.setBounds(98, 123, 100, 27);
		getContentPane().add(btnAcceder);

		JLabel lblAdmin = new JLabel("Usuario");
		lblAdmin.setBounds(28, 24, 60, 20);
		getContentPane().add(lblAdmin);

		JLabel lblPassAdmin = new JLabel("Password");
		lblPassAdmin.setBounds(28, 75, 60, 20);
		getContentPane().add(lblPassAdmin);

		textUsuario = new JTextField();
		textUsuario.setBounds(185, 24, 86, 20);
		getContentPane().add(textUsuario);
		textUsuario.setColumns(10);

		textPassword = new JPasswordField();
		textPassword.setBounds(185, 75, 86, 20);
		getContentPane().add(textPassword);
	}
	
	
	//Método para conseguir el JFrame de la Ventana Principal
	private JFrame getFrame(){
	    return this;
	}
	
	
	private void acceder() {

		try {
			String user = textUsuario.getText();
			String password = new String(textPassword.getPassword());

			if(user.equals("admin")) { // Si quiere acceder como administrador
				conexionBD = conectarBD("admin",password);
				if(conexionBD != null) {
					VentanaConsultas v = new VentanaConsultas(conexionBD) {
			            public void dispose(){
			                getFrame().setVisible(true);
			                super.dispose();
			            }
					};
					v.setVisible(true);
					dispose();
					desconectarBD();
				} else
					JOptionPane.showMessageDialog(null, "Usuario y/o contraseña incorrecto", "ERROR", 0);
			}
			else 
				if(!user.equals("") && !password.equals("")) {
					
					conexionBD = conectarBD("inspector", "inspector");
					Statement stmt = conexionBD.createStatement();
					
					ResultSet rs = stmt.executeQuery("SELECT nombre, apellido FROM inspectores WHERE legajo = '"+user+"' AND password = md5('"+password+"')");
					
					if (rs.next()) {
						VentanaInspector v = new VentanaInspector(conexionBD, user) {
							public void dispose(){
								getFrame().setVisible(true);
								super.dispose();
				            }
						};
						v.setVisible(true);
						dispose();
						desconectarBD();
					} else {
						JOptionPane.showMessageDialog(null, "Usuario y/o contraseña incorrecto", "ERROR", 0);
						desconectarBD();
					}
				}

			textUsuario.setText("");
			textPassword.setText("");

		} catch (SQLException e) {
			// Aca es mejor no mostrar nada pero algo hay que poner
		}
	}

	
	private Connection conectarBD(String user, String password) {
		Connection c = null;
		if (conexionBD == null) {
			try {

				String servidor = "localhost:3306";
				String baseDatos = "parquimetros";
				String usuario = user;
				String clave = password;
				String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos
						+ "?serverTimezone=America/Argentina/Buenos_Aires";

				c =  DriverManager.getConnection(uriConexion, usuario, clave);

			} catch (SQLException ex) {
				// Aca es mejor no mostrar nada pero algo hay que poner
			}
		}
		return c;
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
