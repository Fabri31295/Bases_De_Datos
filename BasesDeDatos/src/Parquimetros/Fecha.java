// Almaraz Fabricio, Pacione Luciano
package Parquimetros;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
		switch(getDayNumberOld(date)) {
    		case(1):{return "Do";}
	    	case(2):{return "Lu";}
	    	case(3):{return "Ma";}
	    	case(4):{return "Mi";}
	    	case(5):{return "Ju";}
	    	case(6):{return "Vi";}
	    	case(7):{return "Sa";}
	    }
	    return null;
	}
	
	private int getDayNumberOld(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    return cal.get(Calendar.DAY_OF_WEEK);
	}

	
	public String getTurno() {
	    int hora = Integer.parseInt(new SimpleDateFormat("H").format(date));
	    if(hora >= 8 && hora < 14) 
	    	return "M";
	    else 
	    	if(hora >= 14 && hora <= 20) 
	    		return"T";
	    	
	    return null;
	}
	
}
