package br.com.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Messages;

public class FacesUtil {

	public static void addInfoMessage(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);
		context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso : ", msg));
	}

	public static void addErroMessage(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);
		context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção: ", msg));
	}

	public static void warn(String msg) {
		Messages.create("Warning!").warn().detail(msg).add();
	}

	public static void info(String msg) {
		Messages.create("Info").detail(msg).add();
	}

	public static ServletContext getServletContext() {
		return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	}

	public static ExternalContext getExternalContext() {
		FacesContext fc = FacesContext.getCurrentInstance();
		return fc.getExternalContext();
	}

	public static HttpSession getHttpSession(boolean create) {
		try {
			return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(create);
		} catch (Exception e) {
		}
		return null;
	}

	public static String getCurrentUrl() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String url = request.getRequestURL().toString();
		String uri = request.getRequestURI();

		return url;
	}

}
