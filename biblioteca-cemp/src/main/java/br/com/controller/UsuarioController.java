package br.com.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import br.com.dao.UsuarioDao;
import br.com.enums.PermissaoEnum;
import br.com.proline.model.Usuario;
import br.com.util.FacesUtil;
import br.com.util.StringUtil;

@SessionScoped
@ManagedBean(name = "usuarioController")
public class UsuarioController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String confirmaSenha;
	private Usuario usuario;
	private Usuario filtro;
	private UsuarioDao usuarioDao;
	private List<Usuario> listaUsuario;
	private List<Usuario> usuarioSelecionadas;
	private List<SelectItem> selectItems;

	public UsuarioController() {
		super();
		limpar();
	}

	public void limpar() {

		usuario = new Usuario();
		filtro = new Usuario();
		usuarioDao = new UsuarioDao();		
		listaUsuario = new ArrayList<Usuario>();
		usuarioSelecionadas = new ArrayList<Usuario>();
		selectItems = new ArrayList<SelectItem>();
	}

	public String salvar() {
		if(checaSenha()) {		
			usuarioDao.salvarOuAtualizar(usuario);
			usuario = new Usuario();		

			carregarUsuario();
			return "lista-usuario.xhtml?faces-redirect=true";
		}
		
		return null;
		
	}
	
	public String editar() {
		return "cadastro-usuario.xhtml?faces-redirect=true";

	}
	

	public String novo() {
		usuario = new Usuario();		
		return "cadastro-usuario?faces-redirect=true";
	}


	public boolean checaSenha() {
		if(StringUtil.comparaStrings(usuario.getSenha(), confirmaSenha))
			return true;

		FacesUtil.addErroMessage("A senha não confere !");
		return false;
	}

	public void buscarUsuario() {
		listaUsuario = usuarioDao.findByExample(filtro);
	}
	
	
	
	/*
	 * public void excluirSelecionados() { if (!usuarioSelecionadas.isEmpty()) { for
	 * (Usuario m : usuarioSelecionadas) { usuarioDao.excluirPorId(m);
	 * listaUsuario.remove(m); }
	 * 
	 * FacesUtil.addInfoMessage("Usuario(s) excluído(s) com sucesso !"); }
	 * 
	 * else FacesUtil.addErroMessage("Nenhum Usuario selecionado");
	 * 
	 }*/

	/*
	 * public void desativarSelecionados() { if (!usuarioSelecionadas.isEmpty()) {
	 * for (Usuario m : usuarioSelecionadas) { usuarioDao.desativaPorId(m); ;
	 * listaUsuario.remove(m); }
	 * 
	 * FacesUtil.addInfoMessage("Usuario(s) excluído(s) com sucesso !"); }
	 * 
	 * else FacesUtil.addErroMessage("Nenhum Usuario selecionado");
	 * 
	 * }
	 */
	public String excluir() {
		usuarioDao.excluirPorId(usuario);
		usuario = new Usuario();
		carregarUsuario();
		return "";
	}

	@PostConstruct
	public void carregarUsuario() {
		listaUsuario = usuarioDao.getListEntity(Usuario.class);
	}

	public String listar() {
		carregarUsuario();
		return "lista-usuario.xhtml?faces-redirect=true";
	}

	

	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public List<Usuario> getListaUsuario() {
		return listaUsuario;
	}

	public void setListaUsuario(List<Usuario> listaUsuario) {
		this.listaUsuario = listaUsuario;
	}

	public List<Usuario> getUsuarioSelecionadas() {
		return usuarioSelecionadas;
	}

	public void setUsuarioSelecionadas(List<Usuario> usuarioSelecionadas) {
		this.usuarioSelecionadas = usuarioSelecionadas;
	}

	public void setSelectItems(List<SelectItem> selectItems) {
		this.selectItems = selectItems;
	}
	
	public PermissaoEnum[] getListaPermissao() {
		return PermissaoEnum.values();
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public Usuario getFiltro() {
		return filtro;
	}

	public void setFiltro(Usuario filtro) {
		this.filtro = filtro;
	}
	
	

}
