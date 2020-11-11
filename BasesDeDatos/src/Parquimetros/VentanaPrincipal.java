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
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Font;

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
		this.setBounds(100, 100, 500, 200);
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
		btnAcceder.setBounds(65, 122, 100, 27);
		getContentPane().add(btnAcceder);

		JLabel lblAdmin = new JLabel("Usuario");
		lblAdmin.setBounds(28, 44, 60, 20);
		getContentPane().add(lblAdmin);

		JLabel lblPassAdmin = new JLabel("Password");
		lblPassAdmin.setBounds(28, 75, 60, 20);
		getContentPane().add(lblPassAdmin);

		textUsuario = new JTextField();
		textUsuario.setBounds(120, 44, 86, 20);
		getContentPane().add(textUsuario);
		textUsuario.setColumns(10);

		textPassword = new JPasswordField();
		textPassword.setBounds(120, 75, 86, 20);
		getContentPane().add(textPassword);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(250, 11, 12, 149);
		getContentPane().add(separator);
		
		JButton btnConexion = new JButton("Conexion");
		btnConexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaParquimetro v = new VentanaParquimetro() {
					public void dispose() {
						getFrame().setVisible(true);
						super.dispose();
					}
				};
			}
		});
		btnConexion.setBounds(322, 65, 113, 40);
		getContentPane().add(btnConexion);
		
		JLabel lblNewLabel = new JLabel("SECCION TARJETAS");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 16));
		lblNewLabel.setBounds(250, 12, 250, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblSeccionUsuarios = new JLabel("SECCION USUARIOS");
		lblSeccionUsuarios.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeccionUsuarios.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 16));
		lblSeccionUsuarios.setBounds(0, 12, 250, 14);
		getContentPane().add(lblSeccionUsuarios);
	}
	
	
	//Método para conseguir el JFrame de la Ventana Principal
	private JFrame getFrame(){
	    return this;
	}
	
	/*
	 * Controles para saber si se debe ingresar como admin,
	 * como inspector o si no es posible entrar por algun
	 * campo erroneo
	 */
	private void acceder() {

		try {
			String user = textUsuario.getText();
			String password = new String(textPassword.getPassword());
			
		/*	if(user.equals("parquimetro")) {
				conexionBD = conectarBD("parquimetro",password);
				if(conexionBD != null) {
					VentanaParquimetro v = new VentanaParquimetro(conexionBD) {
						public void dispose() {
							getFrame().setVisible(true);
							super.dispose();
							desconectarBD();
						}
					};
					v.setVisible(true);
					dispose();
				} else
					JOptionPane.showMessageDialog(null, "Usuario y/o contraseña incorrecto","Error",0);
			}else
				*/
			if(user.equals("admin")) { // Si quiere acceder como administrador
				conectarBD("admin",password);
				if(conexionBD != null) {
					VentanaConsultas v = new VentanaConsultas(conexionBD) {
			            public void dispose(){
			                getFrame().setVisible(true);
			                super.dispose();
							desconectarBD();
			            }
					};
					v.setVisible(true);
					dispose();
				} else
					JOptionPane.showMessageDialog(null, "Usuario y/o contraseña incorrecto", "ERROR", 0);
			}
			else 
				if(!user.equals("") && !password.equals("")) {
					
					conectarBD("inspector", "inspector");
					Statement stmt = conexionBD.createStatement();
					
					ResultSet rs = stmt.executeQuery("SELECT nombre, apellido FROM inspectores WHERE legajo = '"+user+"' AND password = md5('"+password+"')");
					
					if (rs.next()) {
						VentanaInspector v = new VentanaInspector(conexionBD, user) {
							public void dispose(){
								getFrame().setVisible(true);
								super.dispose();
								desconectarBD();
				            }
						};
						v.setVisible(true);
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Usuario y/o contraseña incorrecto", "ERROR", 0);
						desconectarBD();
					}
				}

			textUsuario.setText("");
			textPassword.setText("");

		} catch (SQLException e) {
			//Error
		}
	}

	/*
	 * Abre la conexion a la base de datos con el usuario y la contraseña
	 * que ingresa el usuario
	 */
	private void conectarBD(String user, String password) {
		try {
			if (conexionBD == null) {
				String servidor = "localhost:3306";
				String baseDatos = "parquimetros";
				String usuario = user;
				String clave = password;
				String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos
						+ "?serverTimezone=America/Argentina/Buenos_Aires";

				conexionBD =  DriverManager.getConnection(uriConexion, usuario, clave);
			}

			} catch (SQLException ex) {
				//Error
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
