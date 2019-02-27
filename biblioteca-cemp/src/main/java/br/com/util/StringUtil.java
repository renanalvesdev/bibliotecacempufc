package br.com.util;

import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Messages;

public class StringUtil {

	// compara duas strings
	public static boolean comparaStrings(String string1, String string2) {

		return (!string1.isEmpty() && !string2.isEmpty()) && string1.contentEquals(string2);

	}

}
