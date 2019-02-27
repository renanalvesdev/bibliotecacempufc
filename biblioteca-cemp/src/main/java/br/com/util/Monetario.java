package br.com.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Monetario {

	public static String doubleParaMonetario(Double valor) {
		Locale Local = new Locale("pt", "BR");
		DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Local));
		String s = df.format(valor);
		return s;
	}

	public static Double monetarioParaDouble(String valor) {
		Double retorno =  0.00;
		Locale Local = new Locale("pt", "BR");
		DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Local));
		try {
			retorno =  df.parse(valor).doubleValue();
			
			return retorno;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retorno;

		
	}

}
