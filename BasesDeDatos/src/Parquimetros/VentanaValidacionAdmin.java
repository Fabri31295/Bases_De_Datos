package Parquimetros;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaValidacionAdmin {

	private JFrame frame;
	private JPasswordField textPassword;
	private JTextField textUsuario;
	private static String password = "admin";
	private static String user = "admin";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaValidacionAdmin window = new VentanaValidacionAdmin();
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
	public VentanaValidacionAdmin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setSize(new Dimension(300, 300));
		frame.setBounds(500, 200, 400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel ingreseUsuario = new JLabel("Ingrese usuario");
		
		JLabel ingreseContraseña = new JLabel("Ingrese contrase\u00F1a");
		
		JButton botonAceptar = new JButton("Aceptar");
		botonAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				verificacion();
			}
		});
		
		textPassword = new JPasswordField();
		
		textUsuario = new JTextField();
		textUsuario.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(137)
					.addComponent(botonAceptar, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(47)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(ingreseUsuario, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
						.addComponent(ingreseContraseña, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE))
					.addGap(59)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(textUsuario)
						.addComponent(textPassword, GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
					.addGap(52))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(85)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(ingreseUsuario)
						.addComponent(textUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(38)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(ingreseContraseña)
						.addComponent(textPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(53)
					.addComponent(botonAceptar))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	private void verificacion() {
		String userIngresado = textUsuario.getText();
		String passwordIngresado = new String(textPassword.getPassword());
		if((user.compareTo(userIngresado)) == 0 && (password.compareTo(passwordIngresado) == 0)) {
			frame.setVisible(false);
			VentanaConsultas v = new VentanaConsultas ();
			v.setVisible(true);
		}
		else {
			JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
			textUsuario.setText("");
			textPassword.setText("");
			
		}
	}
}
