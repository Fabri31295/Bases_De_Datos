/*
 * @autores: Almaraz Fabricio, Pacione Luciano
 * 
 */
package Parquimetros;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class VentanaInspector extends javax.swing.JFrame{

	
	protected Connection conexionBD = null;
	private ArrayList<String> patentes;
	private ArrayList<String> erroneas;
	private ArrayList<ArrayList<String>> multados;
	private JScrollPane scroll_patentes;
	private JScrollPane scroll_parquimetros;
	private JScrollPane scroll_multas;
	private JScrollPane scroll_errores;
	private DefaultListModel<Object> model_parquimetros;
	private DefaultListModel<Object> model_patentes;
	private DefaultListModel<Object> model_errores;
	private DefaultTableModel model_tabla;
	private JList<Object> lista_patentes,lista_parquimetros,lista_errores;
	private JTable table;
	private JTextPane informacion;
	private JButton set_patente,remove_patente;
	private JButton confirmar,cerrar_sesion;
	private TextField ingreso_patente;
	private JLabel txt_patente,txt_parquimetros;
	private JLabel txt_multas,txt_info,txt_error;
	private Fecha date;
	private String legajo;		
	
	public VentanaInspector(Connection c,String leg) {
		super();
		conexionBD = c;
		legajo = leg;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setVisible(true);
		this.setBounds(100, 100, 640, 480);
		getContentPane().setLayout(null);
		this.setLocationRelativeTo(null);
		this.setTitle("Inspector");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		inicializarPaneles();
		inicializarBotones();
		cargarParquimetros();
	}
	
	/*
	 * Genera la interfaz de usuario.
	 */
	private void inicializarPaneles() {
		
		scroll_parquimetros = new JScrollPane();
		scroll_parquimetros.setBounds(457, 250, 160, 72);
		getContentPane().add(scroll_parquimetros);
		
		scroll_patentes = new JScrollPane();
		scroll_patentes.setBounds(457, 93, 160, 110);
		getContentPane().add(scroll_patentes);
		
		scroll_multas = new JScrollPane();
		scroll_multas.setBounds(14, 36, 432, 280);
		getContentPane().add(scroll_multas);
		
		scroll_errores = new JScrollPane();
		scroll_errores.setBounds(14,338, 102, 90);
		getContentPane().add(scroll_errores);

		Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
		informacion = new JTextPane();
		informacion.setBounds(124,338, 320, 90);
		informacion.setBorder(border);
		informacion.setEditable(false);
		getContentPane().add(informacion);
		
		ingreso_patente = new TextField();
		ingreso_patente.setBounds(462, 35, 150, 30);
		getContentPane().add(ingreso_patente);
		
		txt_patente = new JLabel();
		txt_patente.setBounds(488, 5, 150, 30);
		txt_patente.setText("Ingresar patente");
		getContentPane().add(txt_patente);
		
		txt_parquimetros = new JLabel();
		txt_parquimetros.setBounds(500, 225, 80, 30);
		txt_parquimetros.setText("Parquimetros");
		getContentPane().add(txt_parquimetros);
		
		txt_multas = new JLabel();
		txt_multas.setBounds(207,14,80,30);
		txt_multas.setText("MULTAS");
		getContentPane().add(txt_multas);
		
		txt_info = new JLabel();
		txt_info.setBounds(250,316,80,30);
		txt_info.setText("información");
		getContentPane().add(txt_info);
		
		txt_error = new JLabel();
		txt_error.setBounds(41,316,80,30);
		txt_error.setText("errores");
		getContentPane().add(txt_error);
		
		lista_patentes = new JList<Object>();
		scroll_patentes.setViewportView(lista_patentes);
		
		lista_parquimetros = new JList<Object>();
		scroll_parquimetros.setViewportView(lista_parquimetros);
		
		lista_errores = new JList<Object>();
		lista_errores.setAlignmentX(CENTER_ALIGNMENT);
		scroll_errores.setViewportView(lista_errores);
		
		model_tabla = new DefaultTableModel() {
		      public boolean isCellEditable(int rowIndex, int mColIndex) {
		        return false;
		      }
		 };
		model_patentes = new DefaultListModel<Object>();
		model_parquimetros = new DefaultListModel<Object>();
		model_errores = new DefaultListModel<Object>();
		
		table = new JTable(model_tabla);
		table.setFont(new Font("Montserrat", Font.BOLD, 10));
		scroll_multas.setViewportView(table);
	}
	
	/*
	 * Genera los botones que se muestran por interfaz y les da funcionalidad.
	 */
	private void inicializarBotones() {
		
		set_patente = new JButton("+");
		set_patente.setBounds(490, 70, 42, 18);
		set_patente.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String ingresada = ingreso_patente.getText();
				if(validarPatente(ingresada)) {
						if(getPosicion(ingreso_patente.getText()) == -1) {
								model_patentes.add(0,ingresada);
								lista_patentes.setModel(model_patentes);
								ingreso_patente.setText("");
						}
						else 
							JOptionPane.showMessageDialog(null, "Patente ya ingresada","Error", JOptionPane.ERROR_MESSAGE);					
				}
				else 
					JOptionPane.showMessageDialog(null, "Patente invalida","Error", JOptionPane.ERROR_MESSAGE);	
			}
		});
		
		remove_patente = new JButton("-");
		remove_patente.setBounds(540, 70, 42, 18);
		remove_patente.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(model_patentes.size() > 0) {
					String seleccion;
					if(ingreso_patente.getText().equals("")) 
						seleccion = (String) lista_patentes.getSelectedValue();
					else 
						seleccion = ingreso_patente.getText();
					
					if(validarPatente(seleccion)) {
						if (getPosicion(seleccion) != -1) {
							if(model_patentes.size() == 1)
								model_patentes.removeAllElements();
							else {
								model_patentes.remove(getPosicion(seleccion));
								ingreso_patente.setText("");
							}		
						} else
							JOptionPane.showMessageDialog(null, "La patente ingresada no se encuentra en la lista","Error", JOptionPane.ERROR_MESSAGE);
							
					}else
						JOptionPane.showMessageDialog(null, "Patente invalida","Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		confirmar = new JButton("Confirmar");
		confirmar.setBounds(490,325,100,18);
		confirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(lista_parquimetros.getSelectedValue() != null) {
						date = new Fecha();
						conexionParquimetro();
				}
			}	
		});
		
		cerrar_sesion = new JButton("Cerrar Sesion");
		cerrar_sesion.setBounds(477,400,130,23);
		cerrar_sesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desconectarBD();
				dispose();
			}
		});
		
		getContentPane().add(set_patente);
		getContentPane().add(remove_patente);
		getContentPane().add(confirmar);
		getContentPane().add(cerrar_sesion);
	}
	
	/* 
	 * Carga los parquimetros de la base de datos para mostrarlos en la interfaz
	 */
	private void cargarParquimetros() {
		try {
			java.sql.Statement stat = conexionBD.createStatement();
			ResultSet res = stat.executeQuery("SELECT calle,numero,altura FROM parquimetros");
			while(res.next()) 
				model_parquimetros.add(0,res.getString("calle")+" "+res.getString("altura"));
			
			lista_parquimetros.setModel(model_parquimetros);
			
			res.close();
			
		} catch (SQLException e) {
       	 JOptionPane.showMessageDialog(null, e.getMessage());
           System.out.println("SQLException: " + e.getMessage());
           System.out.println("SQLState: " + e.getSQLState());
           System.out.println("VendorError: " + e.getErrorCode());
		}
	}
		
	/*
	 * Guarda las patentes ingresadas por el usuario en un ArrayList y 
	 * controla que pertenezcan a la base de datos
	 */
	private void almacenarPatentes() {
		if(model_patentes.size() > 0)
			try {
				patentes = new ArrayList<String>();
				erroneas = new ArrayList<String>();
				java.sql.Statement stat = conexionBD.createStatement();
				ResultSet res = null;
				for(int i = 0; i < model_patentes.size(); i++) {
					res = stat.executeQuery("SELECT patente FROM automoviles WHERE patente = '"+model_patentes.get(i)+"';");	
					if(res.next())
						patentes.add((String) model_patentes.get(i));
					else
						erroneas.add((String) model_patentes.get(i));
				}
				res.close();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	/* 
	 *  Simula la conexion de la unidad al parquimetro seleccionado
	 */
	private void conexionParquimetro(){		
		try {						
			informacion.setText("Registrando el acceso del inspector al parquímetro...");
			String txt = (String) lista_parquimetros.getSelectedValue();
			String[] parts = txt.split(" "); // obtengo calle y altura por separado 
			String id_asociado = asociado(parts[0],parts[1]);
			almacenarPatentes();
			if(id_asociado != null) {
				informacion.setText(informacion.getText()+"\n\nRegistrado correctamente!");
				if(!model_patentes.isEmpty()) {
					informacion.setText(informacion.getText()+"\n\nGenerando las multas correspondientes...");
					multados = new ArrayList<ArrayList<String>>();
					generarMultas(parts[0],parts[1],id_asociado);
					mostrarMultas();
				}
			}else {
				informacion.setText("");
			}
			
		}catch(Exception e){
            System.out.println("Exception: " + e.getMessage());
		}
	}
	
	/* 
	 * Controla que el inspector este asociado a la ubicacion y parquimetro en el dia y turno correspondientes
	 * Retorna el id_asociado_con en caso de estar asociado
	 * */
	private String asociado(String calle,String altura) {	
		boolean encontre = false;
		String salida;
		try {			
			java.sql.Statement stat = conexionBD.createStatement();
			
			ResultSet res = stat.executeQuery("SELECT id_asociado_con,legajo,calle,altura,dia,turno FROM asociado_con");			
			
			while(res.next()){
				if(calle.equals(res.getString("calle")) && altura.equals(res.getString("altura")) && legajo.equals(res.getString("legajo"))) {

					// si el parquimetro esta asociado con el inspector, controlo el dia y turno
					if(res.getString("dia").equals(date.getDia())) {
						if(res.getString("turno").equals(date.getTurno())) {
							salida = res.getString("id_asociado_con");
							res.close();
							return salida;
						}else {	
							encontre = true;
							JOptionPane.showMessageDialog(null, "No autorizado para labrar multas en este horario", "ERROR", 0);						
						}
					}else {
						encontre = true;
						JOptionPane.showMessageDialog(null, "No autorizado para labrar multas en este día", "ERROR", 0);	
					}
				}
			}	
			res.close();
		} catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
		}	
		
		if(!encontre)
			JOptionPane.showMessageDialog(null, "No autorizado para labrar multas en esa ubicacion", "ERROR", 0);

		return null;
	}
	
	/* 
	 * Se encarga de generar las multas para las patentes pertenecientes a la base de datos que no posean un estacionamiento abierto en el 
	 * parquimetro seleccionado
	 * 
	 */
	private void generarMultas(String calle,String altura,String id_asociado) {

		try {
			
			java.sql.Statement stat = conexionBD.createStatement();
			ResultSet res = stat.executeQuery("SELECT patente FROM estacionados WHERE calle = '" + calle + "' AND altura = '" + altura+"' AND  fecha_sal = NULL AND hora_sal = NULL;");			
			
			// removemos de la lista ingresada a aquellas patentes que tengan estacionamiento abierto en esta ubicacion
			while(res.next()){ 	
				if(patentes.contains(res.getString("patente"))){
					patentes.remove(res.getString("patente"));
				}
			}
			res.close();
			
				// Para las patentes que no tienen estacionamientos abiertos en la ubicacion seleccionada se les hace una multa
				if(patentes.size() > 0) {
				
					// Busco el ultimo numero de multa 
					ResultSet res_m = stat.executeQuery("SELECT numero FROM multa ORDER BY numero DESC LIMIT 1");
					res_m.next();
					int n_multa = Integer.parseInt(res_m.getString("numero")) + 1;
					res_m.close();
					// Genero un arreglo para cada multa y almaceno los datos correspondientes a la misma
					
					for(int i = 0; i < patentes.size(); i++) {
						ArrayList<String> pm = new ArrayList<String>();
						
						try {
							stat.execute("INSERT INTO multa(numero, fecha, hora, patente, id_asociado_con) VALUES("+n_multa+", '"+date.getDateSQL()+"', '"+date.getTimeSQL()+"', '"+patentes.get(i)+"', "+Integer.valueOf(id_asociado)+");");
							
							pm = new ArrayList<String>();
							pm.add(String.valueOf(n_multa++));
							pm.add(date.getDateSQL().toString());
							pm.add(date.getTimeSQL().toString());
							pm.add(calle);
							pm.add(altura);
							pm.add(patentes.get(i));
							pm.add(legajo);
						
							multados.add(pm);
						
						}catch(java.sql.SQLException ex) {
							ex.printStackTrace();
						}
					}
				}
			
			ResultSet rs_i = stat.executeQuery("SELECT id_parq FROM parquimetros WHERE calle = '"+calle+"' AND altura = "+altura);
			String id_parq = null;
			if(rs_i.next())
				id_parq = rs_i.getString("id_parq");
			rs_i.close();
			
			stat.execute("INSERT INTO accede(legajo,id_parq,fecha,hora) VALUES("+legajo+", "+id_parq+", '"+date.getDateSQL()+"', '"+date.getTimeSQL()+"');");
			limpiarPatentes();
			
		}catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
		}
	}
	
	/*
	 * Muestra por interfaz las multas generadas
	 */
	private void mostrarMultas() {
			if(model_tabla.getColumnCount() == 0) {
				model_tabla.addColumn("Nro");
				model_tabla.addColumn("Fecha");
				model_tabla.addColumn("Hora");
				model_tabla.addColumn("Calle");
				model_tabla.addColumn("Altura");
				model_tabla.addColumn("Patente");
				model_tabla.addColumn("Legajo");
			}
			for(ArrayList<String> multa: multados)
				model_tabla.addRow(new Object[]{multa.get(0),multa.get(1),multa.get(2),multa.get(3),multa.get(4),multa.get(5),multa.get(6)});	
	
			for(String e: erroneas) 
				model_errores.add(0, e);
			lista_errores.setModel(model_errores);
	}
	
	/*
	 *  Retorna la posicion de una patente en la lista ingresada por el usuario.
	 *  Si no encuentra la patente, retorna -1.
	 */
	private int getPosicion(String p) {
		int i = model_patentes.size();
		int ret = -1,j;
		if(i > 0) {
			boolean encontre = false;
			for(j = 0; j < i && !encontre; j++) {
				if(model_patentes.get(j).equals(p)) {
					encontre = true;
					ret = j;
				}
			}
		}
		return ret;
	}
	
	/*
	 * Limpia las patentes ingresadas del panel
	 */
	private void limpiarPatentes() {
		model_patentes.removeAllElements();
	}
	
	/* Controla que la patente ingresada sea de la forma LLL###*/
	private boolean validarPatente(String patente) {
		char a,b,c,d,e,f;
		if(patente.length() != 6)
			return false;
		else {
			a = patente.charAt(0);
			b = patente.charAt(1);
			c = patente.charAt(2);
			d = patente.charAt(3);
			e = patente.charAt(4);
			f = patente.charAt(5);
			if(!Character.isLetter(a) || !Character.isLetter(b) || !Character.isLetter(c))
				return false;
			else
				if(!Character.isDigit(d) || !Character.isDigit(e) || !Character.isDigit(f))
						return false;
		}
		return true;
	}
	
	/*
	 * Desconexion de la base de datos
	 */
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