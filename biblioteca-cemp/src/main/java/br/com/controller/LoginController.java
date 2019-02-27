package br.com.controller;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.github.adminfaces.template.session.AdminSession;

import br.com.dao.UsuarioDao;
import br.com.proline.model.Usuario;
import br.com.util.FacesUtil;

@Named
@SessionScoped
@Specializes
public class LoginController extends AdminSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username = null, password = null, nomeSessao;
	private boolean carregando = false;
	private boolean logado = false, logadoMunicipio = false;

	private Usuario usuario;
	private EmprestimoController emprestimoController = new EmprestimoController();

	private UsuarioDao usuarioDao;

	public LoginController() {
		super();
		usuarioDao = new UsuarioDao();
		// TODO Auto-generated constructor stub
	}

	/*
	 * public String login() throws NoSuchAlgorithmException { logado = false;
	 * logadoMunicipio = false; usuario = new Usuario(); usuario.setLogin(username);
	 * usuario.setSenha(password); usuario = usuarioDao.login(usuario);
	 * 
	 * if (usuario != null) { return autenticarSessao(); }
	 * FacesUtil.addErroMessage("Login e/ou senha incorretos.");
	 * 
	 * return null;
	 * 
	 * }
	 */

	public String login() throws NoSuchAlgorithmException {
		carregando = true;
		logado = false;
		logadoMunicipio = false;

		usuario = new Usuario();
		usuario.setLogin(username);
		usuario.setSenha(password);

		usuario = usuarioDao.login(usuario);
		if (usuario != null) {
			carregando = false;
			return autenticarSessao();
		}

		FacesUtil.addErroMessage("Falha na autenticação!" + "Nenhuma credencial de autenticação localizada.");
		carregando = false;
		return null;

	}

	@Override
	public boolean isLoggedIn() {

		return username != null;
	}

	public String autenticarSessao() {
		logado = true;
		logadoMunicipio = true;
		FacesUtil.getHttpSession(true).setAttribute("usuario", usuario);
		emprestimoController.expiraReservas();
		return "index.xhtml?faces-redirect=true";
	}

	public String logout() {
		FacesUtil.getHttpSession(true).removeAttribute("usuario");
		FacesUtil.getHttpSession(false);
		FacesUtil.getExternalContext().invalidateSession();

		return "/login.xhtml?faces-redirect=true";
		
	}

	public void reset() {
		logado = false;
		logadoMunicipio = false;
		username = new String();
		password = new String();
	}

	public static Usuario sessaoUsuario() {
		return (Usuario) FacesUtil.getHttpSession(true).getAttribute("usuario");
	}

	public String getUsername() {
		return username;
	}

	public boolean isCarregando() {
		return carregando;
	}

	public void setCarregando(boolean carregando) {
		this.carregando = carregando;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLogado() {
		return logado;
	}

	public void setLogado(boolean logado) {
		this.logado = logado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isLogadoMunicipio() {
		return logadoMunicipio;
	}

	public void setLogadoMunicipio(boolean logadoMunicipio) {
		this.logadoMunicipio = logadoMunicipio;
	}

	public String getNomeSessao() {
		return nomeSessao;
	}

	public void setNomeSessao(String nomeSessao) {
		this.nomeSessao = nomeSessao;
	}
}