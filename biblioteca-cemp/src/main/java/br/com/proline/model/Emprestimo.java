package br.com.proline.model;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.domain.AbstractDomain;

@Named
@Entity(name = "Emprestimo")
@Table(name = "emprestimo")
public class Emprestimo extends AbstractDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "aluno_id")
	private Aluno aluno;
	
	@Column(name = "status", columnDefinition = "boolean default true")
	private boolean status;

	@ManyToOne
	@JoinColumn(name = "livro_id")
	private Livro livro;
	
	@Column(name="data_emprestimo")
	private Date dataEmprestimo;
	
	@Column(name="data_devolucao_prevista")
	private Date dataDevolucaoPrevista;
	
	@Column(name="data_devolucao")
	private Date dataDevolucao;
	
	@Column(name = "renovacoes", columnDefinition = "int default 0")
	private Integer renovacoes;
	
	@Transient
	private Date dataInicialFiltro;
	
	@Transient
	private Date dataFinalFiltro;
	
	@Transient
	private int situacaoFiltro;
	
	@Transient
	private int tipoDataEmprestimo;
	
	
	public int getTipoDataEmprestimo() {
		return tipoDataEmprestimo;
	}

	public void setTipoDataEmprestimo(int tipoDataEmprestimo) {
		this.tipoDataEmprestimo = tipoDataEmprestimo;
	}

	public Date getDataInicialFiltro() {
		return dataInicialFiltro;
	}

	public void setDataInicialFiltro(Date dataInicialFiltro) {
		this.dataInicialFiltro = dataInicialFiltro;
	}

	public Date getDataFinalFiltro() {
		return dataFinalFiltro;
	}

	public void setDataFinalFiltro(Date dataFinalFiltro) {
		this.dataFinalFiltro = dataFinalFiltro;
	}

	public int getSituacaoFiltro() {
		return situacaoFiltro;
	}

	public void setSituacaoFiltro(int situacaoFiltro) {
		this.situacaoFiltro = situacaoFiltro;
	}

	public boolean isNovo() {
		return getId() == null;
	}
	
	public boolean isEdicao() {
		return !isNovo();
	}
	
	
	//em dia ou atrasado
	private Integer situacao;

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	
	public Date getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(Date dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}


	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}	
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(Date dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	public Integer getRenovacoes() {
		return renovacoes;
	}

	public void setRenovacoes(Integer renovacoes) {
		this.renovacoes = renovacoes;
	}

	public Date getDataDevolucaoPrevista() {
		return dataDevolucaoPrevista;
	}

	public void setDataDevolucaoPrevista(Date dataDevolucaoPrevista) {
		this.dataDevolucaoPrevista = dataDevolucaoPrevista;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aluno == null) ? 0 : aluno.hashCode());
		result = prime * result + ((dataDevolucaoPrevista == null) ? 0 : dataDevolucaoPrevista.hashCode());
		result = prime * result + ((dataEmprestimo == null) ? 0 : dataEmprestimo.hashCode());
		result = prime * result + ((livro == null) ? 0 : livro.hashCode());
		result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
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
		Emprestimo other = (Emprestimo) obj;
		if (aluno == null) {
			if (other.aluno != null)
				return false;
		} else if (!aluno.equals(other.aluno))
			return false;
		
		if (dataEmprestimo == null) {
			if (other.dataEmprestimo != null)
				return false;
		} else if (!dataEmprestimo.equals(other.dataEmprestimo))
			return false;
		if (livro == null) {
			if (other.livro != null)
				return false;
		} else if (!livro.equals(other.livro))
			return false;
		if (situacao == null) {
			if (other.situacao != null)
				return false;
		} else if (!situacao.equals(other.situacao))
			return false;
		return true;
	}
	
	
	
}
