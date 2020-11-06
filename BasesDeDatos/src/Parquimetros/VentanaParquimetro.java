package Parquimetros;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class VentanaParquimetro extends javax.swing.JFrame{


	private JScrollPane scroll_parquimetros;
	private JScrollPane scroll_tarjetas;
	private JScrollPane scroll_informacion;
	private DefaultListModel<Object> model_parquimetros;
	private DefaultListModel<Object> model_tarjetas;
	private JList<Object> lista_parquimetros;
	private JList<Object> lista_tarjetas;
	private JList<Object> lista_info;
	private Connection conexionBD;

	/**
	 * Create the application.
	 */
	public VentanaParquimetro(Connection c) {
		initialize();
		conexionBD = c;
		cargarTablas();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		inicializarPaneles();
		inicializarBotones();
	}
	
	
	private void inicializarPaneles() {
		this.setVisible(true);
		this.setBounds(100, 100, 640, 480);
		getContentPane().setLayout(null);
		this.setLocationRelativeTo(null);
		this.setTitle("Parquimetro");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		scroll_parquimetros = new JScrollPane();
		scroll_parquimetros.setBounds(457, 220, 160, 116);
		getContentPane().add(scroll_parquimetros);
		
		lista_parquimetros = new JList<Object>();
		scroll_parquimetros.setViewportView(lista_parquimetros);
		
		scroll_tarjetas = new JScrollPane();
		scroll_tarjetas.setBounds(457, 41, 160, 110);
		getContentPane().add(scroll_tarjetas);
		
		lista_tarjetas = new JList<Object>();
		scroll_tarjetas.setViewportView(lista_tarjetas);
		
		JLabel parquimetros = new JLabel("Parquimetros");
		parquimetros.setFont(new Font("Tahoma", Font.BOLD, 14));
		parquimetros.setBounds(491, 24, 101, 13);
		getContentPane().add(parquimetros);
		
		JLabel tarjetas = new JLabel("Tarjetas");
		tarjetas.setFont(new Font("Tahoma", Font.BOLD, 14));
		tarjetas.setBounds(509, 200, 67, 19);
		getContentPane().add(tarjetas);
		
		scroll_informacion = new JScrollPane();
		scroll_informacion.setBounds(25, 24, 409, 398);
		getContentPane().add(scroll_informacion);
		
		lista_info = new JList<Object>();
		scroll_informacion.setViewportView(lista_info);
	}
	
	
	private void inicializarBotones() {
		JButton okParq = new JButton("confirmar");
		okParq.setFont(new Font("Tahoma", Font.PLAIN, 12));
		okParq.setBounds(491, 152, 85, 21);
		okParq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(lista_parquimetros.getSelectedValue() != null) {
					
				}
			}	
		});
		
		JButton okTarj = new JButton("confirmar");
		okTarj.setFont(new Font("Tahoma", Font.PLAIN, 12));
		okTarj.setBounds(498, 337, 85, 21);
		okTarj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(lista_tarjetas.getSelectedValue() != null) {
					
				}
			}	
		});
		
		
		JButton Cerrar = new JButton("Cerrar Sesion");
		Cerrar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Cerrar.setBounds(473, 413, 132, 29);
		Cerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desconectarBD();
				dispose();
			}
		});
		
		getContentPane().add(Cerrar);
		getContentPane().add(okTarj);
		getContentPane().add(okParq);
	}
	
	
	private void cargarTablas() {
		try {
			java.sql.Statement stat = conexionBD.createStatement();
			ResultSet res = stat.executeQuery("SELECT calle,numero,altura FROM parquimetros");
			while(res.next()) 
				model_parquimetros.add(0,res.getString("calle")+" "+res.getString("altura"));
			lista_parquimetros.setModel(model_parquimetros);
			
			res = stat.executeQuery("SELECT id_tarjeta FROM tarjetas");
			while(res.next())
				model_tarjetas.add(0, res.getString("id_tarjeta"));
			lista_tarjetas.setModel(model_tarjetas);
			
			res.close();
		
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	
	private void desconectarBD(){
	      if(this.conexionBD != null) {
	    	 try {
	            this.conexionBD.close();
	            this.conexionBD = null;
	         }
	         catch (SQLException e){
	            System.out.println("SQLException: " + e.getMessage());
	            System.out.println("SQLState: " + e.getSQLState());
	            System.out.println("VendorError: " + e.getErrorCode());
	         }
	      }
	   }
	
}
