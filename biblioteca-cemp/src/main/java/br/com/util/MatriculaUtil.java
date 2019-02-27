package br.com.util;

import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Messages;

public class MatriculaUtil {

	//gera uma matricula de 6 digitos aleatórios
	public static String geraMatricula() {
		Random random = new Random();

		String matricula = "";

		for (int i = 0; i <= 7; i++) {
			int x = random.nextInt(10);
			matricula = matricula.concat(String.valueOf(x));
		}

		return matricula;

	}

}
