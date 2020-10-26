package Parquimetros;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Canvas;

public class VentanaPrincipal extends javax.swing.JFrame{


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


	public VentanaPrincipal() {
		initialize();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBounds(100, 100, 640, 480);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		this.setTitle("PARQUIMETROS");
		this.setResizable(false);

		JButton btnConsultar = new JButton("Consultar");
		btnConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				consultar();
			}
		});
		btnConsultar.setBounds(130, 339, 100, 47);
		getContentPane().add(btnConsultar);


		JButton btnInspector = new JButton("Inspector");
		btnInspector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				inspector();
			}
		});
		btnInspector.setBounds(400, 339, 100, 47);
		getContentPane().add(btnInspector);
	}

	private void consultar() {
		VentanaValidacion v = new VentanaValidacion("Admin");
		v.setVisible(true);
	}

	private void inspector(){
		VentanaValidacion i = new VentanaValidacion("Inspector");
		i.setVisible(true);
	}

}
