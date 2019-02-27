package br.com.proline.model;

import java.io.Serializable;

import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.domain.AbstractDomain;
import br.com.enums.PermissaoEnum;


@Named
@Entity
public class Usuario extends AbstractDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	//nome
	//telefone contato
	//email contato
	//tipo usuario -> padrao ou administrador
	private String nome;
	private String telefone;
	private String email;
	private String login;
	private String senha;
	private PermissaoEnum permissao;
	
	@Transient
	private int situacaoFiltro;
	
	
	
	
	public int getSituacaoFiltro() {
		return situacaoFiltro;
	}

	public void setSituacaoFiltro(int situacaoFiltro) {
		this.situacaoFiltro = situacaoFiltro;
	}

	@Column(name = "status", columnDefinition = "boolean default true")
	private Boolean status;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public PermissaoEnum getPermissao() {
		return permissao;
	}

	public void setPermissao(PermissaoEnum permissao) {
		this.permissao = permissao;
	}

	public boolean isNovo() {
		return getId() == null;
	}

	public boolean isEdicao() {
		return !isNovo();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (senha == null) {
			if (other.senha != null)
				return false;
		} else if (!senha.equals(other.senha))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

}
