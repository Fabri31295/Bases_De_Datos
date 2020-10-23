package Parquimetros;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaPrincipal extends javax.swing.JFrame{

	private static VentanaPrincipal instancia;

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
		if(instancia==null) {
			instancia = new VentanaPrincipal();
		}
		return instancia;
	}
	
	private VentanaPrincipal() {
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBounds(100, 100, 640, 480);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JButton btnConsultar = new JButton("Consultar");
		btnConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				consultar();
			}
		});
		btnConsultar.setBounds(250, 247, 100, 47);
		getContentPane().add(btnConsultar);
		this.setTitle("PARQUIMETROS");
		this.setResizable(true);
	}
	
	private void consultar() {
		VentanaValidacion v = VentanaValidacion.getInstancia();
		v.setVisible(true);
		setVisible(false);
	}

}
