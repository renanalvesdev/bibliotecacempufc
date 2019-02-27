package br.com.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import br.com.util.FacesUtil;

@SessionScoped
@ManagedBean(name = "dashboardController")
public class DashboardController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EmprestimoController emprestimoController;

	@Inject
	private ReservaController reservaController;

	@Inject
	private LivroController livroController;

	@Inject
	private AlunoController alunoController;

	private Integer totalEmprestimos;
	private Integer totalReservas;
	private Integer totalLivros;
	private Integer totalAlunos;

	public void limpar() {
		emprestimoController = new EmprestimoController();
		reservaController = new ReservaController();
		livroController = new LivroController();
		alunoController = new AlunoController();

	}

	@PostConstruct
	public void inicializar() {

		limpar();
		setTotalEmprestimos(emprestimoController.getTotal());
		setTotalReservas(reservaController.getTotal());
		setTotalLivros(livroController.getTotal());
		setTotalAlunos(alunoController.getTotal());

	}

	public Integer getTotalEmprestimos() {
		return totalEmprestimos;
	}

	public void setTotalEmprestimos(Integer totalEmprestimos) {
		this.totalEmprestimos = totalEmprestimos;
	}

	public Integer getTotalReservas() {
		return totalReservas;
	}

	public void setTotalReservas(Integer totalReservas) {
		this.totalReservas = totalReservas;
	}

	public Integer getTotalLivros() {
		return totalLivros;
	}

	public void setTotalLivros(Integer totalLivros) {
		this.totalLivros = totalLivros;
	}

	public Integer getTotalAlunos() {
		return totalAlunos;
	}

	public void setTotalAlunos(Integer totalAlunos) {
		this.totalAlunos = totalAlunos;
	}

}
