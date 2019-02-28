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
@Entity(name = "Reserva")
@Table(name = "reserva")
public class Reserva extends AbstractDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "aluno_id")
	private Aluno aluno;

	@ManyToOne
	@JoinColumn(name = "emprestimo_id")
	private Emprestimo emprestimo;

	@Column(name = "data_reserva")
	private Date dataReserva;

	@Column(name = "data_encerramento_reserva")
	private Date dataEncerramentoReserva;

	@Transient
	private int situacaoFiltro;

	@Column(name = "status", columnDefinition = "boolean default true")
	private boolean status;

	@Column(name = "expirada", columnDefinition = "boolean default false")
	private boolean expirada;

	public boolean isExpirada() {
		return expirada;
	}

	public void setExpirada(boolean expirada) {
		this.expirada = expirada;
	}

	public int getSituacaoFiltro() {
		return situacaoFiltro;
	}

	public void setSituacaoFiltro(int situacaoFiltro) {
		this.situacaoFiltro = situacaoFiltro;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Emprestimo getEmprestimo() {
		return emprestimo;
	}

	public void setEmprestimo(Emprestimo emprestimo) {
		this.emprestimo = emprestimo;
	}

	public Date getDataReserva() {
		return dataReserva;
	}

	public void setDataReserva(Date dataReserva) {
		this.dataReserva = dataReserva;
	}

	public Date getDataEncerramentoReserva() {
		return dataEncerramentoReserva;
	}

	public void setDataEncerramentoReserva(Date dataEncerramentoReserva) {
		this.dataEncerramentoReserva = dataEncerramentoReserva;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aluno == null) ? 0 : aluno.hashCode());
		result = prime * result + ((dataReserva == null) ? 0 : dataReserva.hashCode());
		result = prime * result + ((emprestimo == null) ? 0 : emprestimo.hashCode());
		result = prime * result + (status ? 1231 : 1237);
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
		Reserva other = (Reserva) obj;
		if (aluno == null) {
			if (other.aluno != null)
				return false;
		} else if (!aluno.equals(other.aluno))
			return false;
		if (dataReserva == null) {
			if (other.dataReserva != null)
				return false;
		} else if (!dataReserva.equals(other.dataReserva))
			return false;
		if (emprestimo == null) {
			if (other.emprestimo != null)
				return false;
		} else if (!emprestimo.equals(other.emprestimo))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

}
