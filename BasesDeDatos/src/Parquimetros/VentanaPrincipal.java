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
import java.awt.Canvas;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class VentanaPrincipal extends javax.swing.JFrame {

	private static VentanaPrincipal instancia;
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
					VentanaPrincipal window = new VentanaPrincipal();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */

	public static VentanaPrincipal getInstancia() {
		if (instancia == null)
			instancia = new VentanaPrincipal();
		return instancia;
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

	private void acceder() {
			
			String userIngresado = textUsuario.getText();
			String passwordIngresado = new String(textPassword.getPassword());

			conectarBD(userIngresado, passwordIngresado);

			if(conexionBD != null && userIngresado.equals("admin")) { 
					setVisible(false);
					VentanaConsultas vc = new VentanaConsultas(conexionBD);
					vc.setVisible(true);
			}
			else {
					try {
						conectarBD("inspector", "inspector");
						Statement stmt = this.conexionBD.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT nombre, apellido FROM inspectores WHERE legajo = "+userIngresado+" AND password = md5('"+passwordIngresado+"')");
						
						if(rs.next()) {
							setVisible(false);
							VentanaInspector i = new VentanaInspector(conexionBD,userIngresado);
							i.setVisible(true);
						}else {
							JOptionPane.showMessageDialog(null, "Usuario y/o contraseña incorrecto", "ERROR", 0);
							desconectarBD();
						}
						
					}catch(SQLException ex) {
						
					}
			}

			textUsuario.setText("");
			textPassword.setText("");

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
				
				conexionBD =  DriverManager.getConnection(uriConexion, usuario, clave);

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
