// Almaraz Fabricio, Pacione Luciano

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Date {
	
	public static java.util.Date convertirStringADate(String p_date) {
		java.util.Date retorno = null;
		if (p_date != null) {
			try {
				retorno = (new SimpleDateFormat("dd/MM/yyyy")).parse(p_date);
			} catch (ParseException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return retorno;
	}

	public static String convertirDateAString(java.util.Date p_date) {
		String retorno = null;
		if (p_date != null) {
			retorno = (new SimpleDateFormat("dd/MM/yyyy")).format(p_date);
		}
		return retorno;
	}

	public static String convertirDateAStringDB(java.util.Date p_date) {
		String retorno = null;
		if (p_date != null) {
			retorno = (new SimpleDateFormat("yyyy-MM-dd")).format(p_date);
		}
		return retorno;
	}

	public static java.sql.Date convertirDateADateSQL(java.util.Date p_date) {
		java.sql.Date retorno = null;
		if (p_date != null) {
			retorno = java.sql.Date.valueOf((new SimpleDateFormat("yyyy-MM-dd")).format(p_date));
		}
		return retorno;
	}

	public static java.sql.Date convertirStringADateSQL(String p_date) {
		java.sql.Date retorno = null;
		if (p_date != null) {
			retorno = Date.convertirDateADateSQL(Date.convertirStringADate(p_date));
		}
		return retorno;
	}

	public static boolean validar(String p_date) {
		if (p_date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				sdf.setLenient(false);
				sdf.parse(p_date);
				return true;
			} catch (ParseException ex) {
			}
		}
		return false;
	}
}