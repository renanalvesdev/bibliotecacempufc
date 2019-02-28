package br.com.proline.model;

import java.io.Serializable;

import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.domain.AbstractDomain;

@Named
@Entity(name = "Configuracoes")
@Table(name = "configuracoes")
public class Configuracoes extends AbstractDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="dias_duracao_emprestimo")
	private Integer diasDuracaoEmprestimo;	
	
	@Column(name="dias_duracao_renovacao")
	private Integer diasDuracaoRenovacao;
	
	@Column(name="dias_duracao_reserva")
	private Integer diasDuracaoReserva;
	
	@Column(name="renovacoes_por_livro")
	private Integer renovacoesPorLivro;
	
	@Column(name="livros_por_aluno")
	private Integer livrosPorAluno;
	
	
	@Column(name="login_email_biblioteca")
	private String loginEmailBiblioteca;

	@Column(name="senha_email_biblioteca")
	private String senhaEmailBiblioteca;

	public Integer getDiasDuracaoEmprestimo() {
		return diasDuracaoEmprestimo;
	}

	public void setDiasDuracaoEmprestimo(Integer diasDuracaoEmprestimo) {
		this.diasDuracaoEmprestimo = diasDuracaoEmprestimo;
	}

	public Integer getDiasDuracaoRenovacao() {
		return diasDuracaoRenovacao;
	}

	public void setDiasDuracaoRenovacao(Integer diasDuracaoRenovacao) {
		this.diasDuracaoRenovacao = diasDuracaoRenovacao;
	}

	public Integer getDiasDuracaoReserva() {
		return diasDuracaoReserva;
	}

	public void setDiasDuracaoReserva(Integer diasDuracaoReserva) {
		this.diasDuracaoReserva = diasDuracaoReserva;
	}

	public Integer getRenovacoesPorLivro() {
		return renovacoesPorLivro;
	}

	public void setRenovacoesPorLivro(Integer renovacoesPorLivro) {
		this.renovacoesPorLivro = renovacoesPorLivro;
	}

	public Integer getLivrosPorAluno() {
		return livrosPorAluno;
	}

	public void setLivrosPorAluno(Integer livrosPorAluno) {
		this.livrosPorAluno = livrosPorAluno;
	}

	public String getLoginEmailBiblioteca() {
		return loginEmailBiblioteca;
	}

	public void setLoginEmailBiblioteca(String loginEmailBiblioteca) {
		this.loginEmailBiblioteca = loginEmailBiblioteca;
	}

	public String getSenhaEmailBiblioteca() {
		return senhaEmailBiblioteca;
	}

	public void setSenhaEmailBiblioteca(String senhaEmailBiblioteca) {
		this.senhaEmailBiblioteca = senhaEmailBiblioteca;
	}

	
	
}
