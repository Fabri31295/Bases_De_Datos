package Parquimetros;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;


public class VentanaValidacion extends javax.swing.JFrame{
	
	private JLabel labelUsuario;
	private JLabel labelContrase�a;
	private JTextField textUsuario;
	private JPasswordField textContrase�a;


	private static String password = "admin";
	private static String user = "admin";
	private JButton btnAceptar;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the application.
	 */
	public VentanaValidacion() {
		super();
		setMaximizedBounds(new Rectangle(0, 0, 0, 0));
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
				verificacion();
			}
		});
		btnAceptar.setBounds(156, 184, 86, 20);
		getContentPane().add(btnAceptar);
	}
	
	private void verificacion() {
		String userIngresado = textUsuario.getText();
		String passwordIngresado = new String(textContrase�a.getPassword());
		if((user.compareTo(userIngresado)) == 0 && (password.compareTo(passwordIngresado) == 0)) {
			setVisible(false);
			VentanaConsultas v = new VentanaConsultas ();
			v.llenarListaTablas();
		}
		else {
			JOptionPane.showMessageDialog(null, "Usuario o contrase�a incorrectos");
			textUsuario.setText("");
			textContrase�a.setText("");
			
		}
	}
}
