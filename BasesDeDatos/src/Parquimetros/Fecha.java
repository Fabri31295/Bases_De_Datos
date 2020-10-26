// Almaraz Fabricio, Pacione Luciano
package Parquimetros;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Fecha {
	
	private java.sql.Date dateSQL;
	private java.sql.Time timeSQL;
	private Date date;
	
	public Fecha() {
		date = new Date();
	    dateSQL = java.sql.Date.valueOf((new SimpleDateFormat("yyyy-MM-dd")).format(date));
	    timeSQL = java.sql.Time.valueOf((new SimpleDateFormat("HH:mm:ss")).format(date));
	}


	public java.sql.Date getDateSQL() {
		return dateSQL;
	}


	public java.sql.Time getTimeSQL() {
		return timeSQL;
	}


	public String getDia() {
		return "Do";
	  /*  switch(new SimpleDateFormat("E").format(date)) {
	    	case("Monday"):{return "Lu";}
	    	case("Tuesday"):{return "Ma";}
	    	case("Wednesday"):{return "Mi";}
	    	case("Thursday"):{return "Ju";}
	    	case("Friday"):{return "Vi";}
	    	case("Saturday"):{return "Sa";}
	    	case("Sunday"):{return "Do";}
	    }
	    return null;*/
	}

	
	public String getTurno() {
		return "M";
		/*
	    int hora = Integer.parseInt(new SimpleDateFormat("H").format(date));
	    if(hora >= 8 && hora < 14) 
	    	return "M";
	    else 
	    	if(hora >= 14 && hora <= 20) 
	    		return"T";
	    	
	    return null;*/
	}
	
}
