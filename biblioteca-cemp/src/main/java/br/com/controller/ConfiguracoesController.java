package br.com.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.dao.ConfiguracoesDao;
import br.com.proline.model.Configuracoes;
import br.com.util.FacesUtil;
import br.com.util.StringUtil;

@SessionScoped
@ManagedBean(name = "configuracoesController")
public class ConfiguracoesController implements Serializable {

	private static final long serialVersionUID = 1L;

	private Configuracoes configuracoes;
	private ConfiguracoesDao configuracoesDao;
	private String confirmaSenha;

	public ConfiguracoesController() {
		super();
		limpar();
	}

	public void limpar() {
		configuracoes = new Configuracoes();
		configuracoesDao = new ConfiguracoesDao();
	}

	public String salvar() {
		if (StringUtil.comparaStrings(configuracoes.getSenhaEmailBiblioteca(), confirmaSenha)) {
			configuracoes = configuracoesDao.salvarOuAtualizar(configuracoes);
			FacesUtil.addInfoMessage("Operação realizada com sucesso !");
			return "/index.xhtml?faces-redirect=true";
		}

		else {
			FacesUtil.addErroMessage("A senha não confere !");
			return null;
		}

	}

	@PostConstruct
	public void carregarConfiguracoes() {
		// limpar
		limpar();
		configuracoes = configuracoesDao.configuracao();
	}

	public Configuracoes getValorConfiguracoes() {
		return configuracoesDao.configuracao();
	}

	public String novo() {
		configuracoes = new Configuracoes();
		return "cadastro-configuracoes?faces-redirect=true";
	}

	public String editar() {
		return "cadastro-configuracoes.xhtml?faces-redirect=true";

	}

	public Configuracoes getConfiguracoes() {
		return configuracoes;
	}

	public void setConfiguracoes(Configuracoes configuracoes) {
		this.configuracoes = configuracoes;
	}

	public ConfiguracoesDao getConfiguracoesDao() {
		return configuracoesDao;
	}

	public void setConfiguracoesDao(ConfiguracoesDao configuracoesDao) {
		this.configuracoesDao = configuracoesDao;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

}
