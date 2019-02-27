package br.com.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;

import br.com.dao.AlunoDao;
import br.com.dao.EmprestimoDao;
import br.com.dao.LivroDao;
import br.com.dao.ReservaDao;
import br.com.proline.model.Aluno;
import br.com.proline.model.Emprestimo;
import br.com.proline.model.Livro;
import br.com.proline.model.Reserva;
import br.com.util.Data;
import br.com.util.FacesUtil;
import br.com.util.MatriculaUtil;

@SessionScoped
@ManagedBean(name = "alunoController")
public class AlunoController implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int DIAS_EMPRESTIMO = 15;
	private static final int NUM_LIVROS_POR_VEZ = 3;
	private static final int DIAS_RENOVACAO = 7;
	private static final int NUM_LIMITE_RENOVACOES = 2;
	private static final int DIAS_RESERVA = 2;
	// private static final int NUM_DIGITOS_CODIGO = 7;

	// models
	private Aluno aluno;
	private Livro livro;
	private Emprestimo emprestimo;
	private Reserva reserva;

	// daos
	private AlunoDao alunoDao;
	private LivroDao livroDao;
	private ReservaDao reservaDao;
	private EmprestimoDao emprestimoDao;

	// listas
	private List<Emprestimo> listaEmprestimo;
	private List<Emprestimo> listaEmprestimoDevolvidos;
	private List<Emprestimo> listaEmprestimoAdicionados;
	private List<Reserva> listaReserva;
	private List<Reserva> listaReservaExcluidos;
	private List<Aluno> listaAluno;
	private List<Aluno> alunoSelecionadas;
	private List<SelectItem> selectItems;

	public AlunoController() {
		super();
		limpar();
	}

	public void limpar() {
		// instancia models
		aluno = new Aluno();
		livro = new Livro();
		reserva = new Reserva();
		emprestimo = new Emprestimo();

		// instancia daos
		alunoDao = new AlunoDao();
		livroDao = new LivroDao();
		reservaDao = new ReservaDao();
		emprestimoDao = new EmprestimoDao();

		// instancia listas
		listaAluno = new ArrayList<Aluno>();
		listaEmprestimo = new ArrayList<Emprestimo>();
		listaEmprestimoDevolvidos = new ArrayList<Emprestimo>();
		listaEmprestimoAdicionados = new ArrayList<Emprestimo>();

		listaReserva = new ArrayList<Reserva>();
		listaReservaExcluidos = new ArrayList<Reserva>();
		alunoSelecionadas = new ArrayList<Aluno>();
		selectItems = new ArrayList<SelectItem>();
	}

	public String salvar() {

		alunoDao.salvarOuAtualizar(aluno);
		carregarAluno();
		FacesUtil.addInfoMessage("Operação realizada com sucesso !");

		return "lista-aluno.xhtml?faces-redirect=true";

	}

	public String abrirModalMatricula() {
		if (aluno.getId() == null) {

			String matricula = MatriculaUtil.geraMatricula();
			aluno.setMatricula(matricula);
			RequestContext.getCurrentInstance().execute("PF('dlgMatricula').show()");
			return "";
		}

		else
			return salvar();
	}

	public String novo() {
		aluno = new Aluno();
		return "cadastro-aluno?faces-redirect=true";
	}

	public String editar() {
		return "cadastro-aluno.xhtml?faces-redirect=true";

	}

	public int contaDias(Date dataEmprestimo) {
		return Data.subtraiDatas(dataEmprestimo, new Date());
	}

	public boolean permiteRenovacao() {
		// numero de dias que o aluno está com o livro
		int diasDePosse = Data.subtraiDatas(emprestimo.getDataEmprestimo(), new Date());
		// numero total de dias que o aluno tem direito
		int diasTotal = Data.subtraiDatas(emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucaoPrevista());

		boolean minimoDiasAtingido = (diasDePosse >= diasTotal * 0.5);
		boolean existeReserva = reservaDao.findByEmprestimo(emprestimo) != null;
		boolean renovacoesAtingidas = emprestimo.getRenovacoes() >= NUM_LIMITE_RENOVACOES;

		if (!minimoDiasAtingido) {
			FacesUtil.addErroMessage("Dias de posse insuficientes para renovação.");
		}

		else if (existeReserva) {
			FacesUtil.addErroMessage("Livro reservado.");
		}

		else if (renovacoesAtingidas) {
			FacesUtil.addErroMessage("Você já efeutou o numero máximo de renovações.");
		}

		return (!existeReserva && minimoDiasAtingido && !renovacoesAtingidas);

	}

	public boolean emprestimoEmDia(Emprestimo emp) {
		return new Date().before(emp.getDataDevolucaoPrevista());
	}

	public Date novaDataDevolucao() {
		// soma de sete dias;
		return Data.somaDatas(emprestimo.getDataDevolucaoPrevista(), 7);
	}

	public boolean desabilitaAdicionarEmprestimo() {
		return (emprestimo.getId() != null);
	}

	public boolean desabilitaAdicionarReserva() {

		return (emprestimo.getId() == null);
	}

	public boolean desabilitaMoverParaEmprestimo(Reserva reserva) {

		return (!reserva.getEmprestimo().isStatus()); // ou lista de emprestimo estiver cheia
	}

	public void devolverLivro() {
		if (emprestimo.getId() != null) {

			listaEmprestimo.remove(emprestimo);
			listaEmprestimoDevolvidos.add(emprestimo);
			RequestContext.getCurrentInstance().execute("PF('dlgPrimary').hide()");
			FacesUtil.addInfoMessage("Devolução efetuada com sucesso !");
		}

	}

	public void consultaLivroEmprestimo() {

		if (!livro.getCodigo().trim().isEmpty()) {
			Livro livroAux = livroDao.findByCodigo(livro.getCodigo());
			if (livroAux != null && !emprestimoExisteNaLista(livroAux.getId())) {
				livro = livroAux;
				Emprestimo emprestimoAux = emprestimoDao.findByLivro(livroAux);
				if (emprestimoAux != null) {
					emprestimo = emprestimoAux;
					FacesUtil.addErroMessage("Já existe um empréstimo para este livro.");
				}
			}

			else if (livroAux == null) {
				FacesUtil.addErroMessage("Nenhum livro encontrado para este código.");
			}
		}

	}
	

	public void consultaLivroReserva() {
		if (!livro.getCodigo().trim().isEmpty()) {
			Livro livroAux = livroDao.findByCodigo(livro.getCodigo());
			if (livroAux != null && !reservaExisteNaLista(livroAux.getId())) {
				livro = livroAux;
				Emprestimo emprestimoAux = emprestimoDao.findByLivro(livroAux);
				if (emprestimoAux != null) {
					emprestimo = emprestimoAux;
				}

				else {
					FacesUtil.addErroMessage("Livro disponível para empréstimo.");
				}

			}

			else {
				FacesUtil.addErroMessage("Nenhum livro encontrado para este código.");
			}
		}

	}

	public void adicionarEmprestimo() {
		if (permiteAdicionarEmprestimo()) {
			Emprestimo emprestimo = new Emprestimo();
			emprestimo.setAluno(aluno);
			emprestimo.setLivro(livro);
			emprestimo.setDataEmprestimo(new Date());
			emprestimo.setDataDevolucaoPrevista(Data.somaDatas(emprestimo.getDataEmprestimo(), DIAS_EMPRESTIMO));
			emprestimo.setSituacao(1);

			listaEmprestimo.add(emprestimo);

			RequestContext.getCurrentInstance().execute("PF('dlgEmprestimo').hide()");

		}
	}

	public boolean permiteAdicionarEmprestimo() {
		boolean jaAdicionado = emprestimoExisteNaLista(livro.getId());
		boolean reservado = reservaDao.findByLivro(livro) != null;

		if (jaAdicionado) {
			FacesUtil.addErroMessage("Livro já adicionado");
		}

		else if (reservado) {
			FacesUtil.addErroMessage("Livro reservado");
		}

		return (!jaAdicionado && !reservado);
	}

	public void renovarEmprestimo() {

		if (permiteRenovacao()) {
			emprestimo.setRenovacoes(emprestimo.getRenovacoes() + 1);
			emprestimo.setDataDevolucaoPrevista(Data.somaDatas(emprestimo.getDataDevolucaoPrevista(), DIAS_RENOVACAO));
			RequestContext.getCurrentInstance().execute("PF('dlgRenovacao').hide()");
		}

	}

	public void adicionarReserva() {
		if (livro.getId() != null && aluno.getId() != null && !reservaExisteNaLista(livro.getId())) {
			Reserva reserva = new Reserva();
			reserva.setAluno(aluno);
			reserva.setEmprestimo(emprestimo);
			reserva.setDataReserva(new Date());

			listaReserva.add(reserva);
			RequestContext.getCurrentInstance().execute("PF('dlgReserva').hide()");

		}
	}

	public void removeEmprestimo(Emprestimo emprestimo) {
		listaEmprestimo.remove(emprestimo);
	}

	public void removeReserva(Reserva reserva) {
		listaReserva.remove(reserva);
		if (reserva.getId() != null) {
			listaReservaExcluidos.add(reserva);
		}
	}

	// quando o livro reservado estiver disponível, pode mover para empréstimo
	public void moverParaEmprestimo(Reserva res) {
		// se numero de emprestimos permitido

		// remove da lista de reserva
		listaReserva.remove(res);
		listaReservaExcluidos.add(res);

		// coloca na lista de emprestimo
		Emprestimo emprestimo = new Emprestimo();
		emprestimo.setAluno(aluno);
		emprestimo.setLivro(res.getEmprestimo().getLivro());
		emprestimo.setDataEmprestimo(new Date());
		emprestimo.setDataDevolucaoPrevista(Data.somaDatas(emprestimo.getDataEmprestimo(), DIAS_EMPRESTIMO));
		emprestimo.setSituacao(1);

		listaEmprestimo.add(emprestimo);
		FacesUtil.addInfoMessage("Movido para empréstimo !");

	}

	public boolean emprestimoExisteNaLista(Long id) {
		Optional<Emprestimo> emprestimoEncontrado = listaEmprestimo.stream()
				.filter(p -> p.getLivro().getId().equals(id)).findFirst();
		if (emprestimoEncontrado.isPresent()) {
			FacesUtil.addErroMessage("Livro já adicionado.");
			livro = new Livro();
			return true;
		}

		return false;

	}

	public boolean reservaExisteNaLista(Long id) {
		Optional<Reserva> reservaEncontrado = listaReserva.stream()
				.filter(p -> p.getEmprestimo().getLivro().getId().equals(id)).findFirst();
		if (reservaEncontrado.isPresent()) {
			FacesUtil.addErroMessage("Livro já adicionado.");
			livro = new Livro();
			return true;
		}

		return false;

	}

	public void abrirModalEmprestimo() {
		emprestimo = new Emprestimo();
		livro = new Livro();
		RequestContext.getCurrentInstance().execute("PF('dlgEmprestimo').show()");

	}

	public void abrirModalReserva() {
		reserva = new Reserva();
		livro = new Livro();
		RequestContext.getCurrentInstance().execute("PF('dlgReserva').show()");

	}

	/*
	 * public void excluirSelecionados() { if (!alunoSelecionadas.isEmpty()) { for
	 * (Aluno m : alunoSelecionadas) { alunoDao.excluirPorId(m);
	 * listaAluno.remove(m); }
	 * 
	 * FacesUtil.addInfoMessage("Aluno(s) excluído(s) com sucesso !"); }
	 * 
	 * else FacesUtil.addErroMessage("Nenhum Aluno selecionado");
	 * 
	 * }
	 */

	/*
	 * public void desativarSelecionados() { if (!alunoSelecionadas.isEmpty()) { for
	 * (Aluno m : alunoSelecionadas) { alunoDao.desativaPorId(m); ;
	 * listaAluno.remove(m); }
	 * 
	 * FacesUtil.addInfoMessage("Aluno(s) excluído(s) com sucesso !"); }
	 * 
	 * else FacesUtil.addErroMessage("Nenhum Aluno selecionado");
	 * 
	 * }
	 */

	public void incluirEmprestimos() {
		for (Emprestimo emp : listaEmprestimo) {
			emp.setStatus(true);
			emprestimoDao.salvarOuAtualizar(emp);
		}
	}

	public void finalizarEmprestimos() {
		for (Emprestimo emp : listaEmprestimoDevolvidos) {
			emp.setStatus(false);
			emp.setDataDevolucao(new Date());
			emprestimoDao.salvarOuAtualizar(emp);
		}
	}

	public void incluirReservas() {
		for (Reserva res : listaReserva) {
			if (res.getId() == null) {
				res.setStatus(true);
				reservaDao.salvarOuAtualizar(res);
			}
		}
	}

	public void finalizarReservas() {
		for (Reserva res : listaReservaExcluidos) {
			res.setStatus(false);
			reservaDao.salvarOuAtualizar(res);
		}
	}

	public String excluir() {
		alunoDao.excluirPorId(aluno);
		aluno = new Aluno();
		carregarAluno();
		return "";
	}

	@PostConstruct
	public void carregarAluno() {
		listaAluno = alunoDao.getListEntity(Aluno.class);
	}

	public String listar() {
		carregarAluno();
		return "/pages/aluno/lista-aluno.xhtml?faces-redirect=true";
	}

	// botao da tabela
	public boolean desabilitaBtnDevolver(Emprestimo emp) {
		return emp.getId() == null;
	}

	public boolean desabilitaBtnExcluir(Emprestimo emp) {
		return emp.getId() != null;
	}
	
	public Integer getTotal() {
		return alunoDao.getListEntity(Aluno.class).size();
	}

	// getters e setters
	public List<SelectItem> getSelectItems() {
		selectItems = new ArrayList<SelectItem>();
		if (selectItems.size() == 0) {
			for (Aluno aluno : listaAluno) {
				selectItems.add(new SelectItem(aluno, aluno.getNome()));
			}
		}
		return selectItems;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public AlunoDao getAlunoDao() {
		return alunoDao;
	}

	public void setAlunoDao(AlunoDao alunoDao) {
		this.alunoDao = alunoDao;
	}

	public List<Aluno> getListaAluno() {
		return listaAluno;
	}

	public void setListaAluno(List<Aluno> listaAluno) {
		this.listaAluno = listaAluno;
	}

	public List<Aluno> getAlunoSelecionadas() {
		return alunoSelecionadas;
	}

	public void setAlunoSelecionadas(List<Aluno> alunoSelecionadas) {
		this.alunoSelecionadas = alunoSelecionadas;
	}

	public void setSelectItems(List<SelectItem> selectItems) {
		this.selectItems = selectItems;
	}

	public EmprestimoDao getEmprestimoDao() {
		return emprestimoDao;
	}

	public void setEmprestimoDao(EmprestimoDao emprestimoDao) {
		this.emprestimoDao = emprestimoDao;
	}

	public List<Emprestimo> getListaEmprestimo() {
		return listaEmprestimo;
	}

	public void setListaEmprestimo(List<Emprestimo> listaEmprestimo) {
		this.listaEmprestimo = listaEmprestimo;
	}

	public Emprestimo getEmprestimo() {
		return emprestimo;
	}

	public void setEmprestimo(Emprestimo emprestimo) {
		this.emprestimo = emprestimo;
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public LivroDao getLivroDao() {
		return livroDao;
	}

	public void setLivroDao(LivroDao livroDao) {
		this.livroDao = livroDao;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public ReservaDao getReservaDao() {
		return reservaDao;
	}

	public void setReservaDao(ReservaDao reservaDao) {
		this.reservaDao = reservaDao;
	}

	public List<Reserva> getListaReserva() {
		return listaReserva;
	}

	public void setListaReserva(List<Reserva> listaReserva) {
		this.listaReserva = listaReserva;
	}

	public List<Reserva> getListaReservaExcluidos() {
		return listaReservaExcluidos;
	}

	public void setListaReservaExcluidos(List<Reserva> listaReservaExcluidos) {
		this.listaReservaExcluidos = listaReservaExcluidos;
	}

	public List<Emprestimo> getListaEmprestimoDevolvidos() {
		return listaEmprestimoDevolvidos;
	}

	public void setListaEmprestimoDevolvidos(List<Emprestimo> listaEmprestimoDevolvidos) {
		this.listaEmprestimoDevolvidos = listaEmprestimoDevolvidos;
	}

	public List<Emprestimo> getListaEmprestimoAdicionados() {
		return listaEmprestimoAdicionados;
	}

	public void setListaEmprestimoAdicionados(List<Emprestimo> listaEmprestimoAdicionados) {
		this.listaEmprestimoAdicionados = listaEmprestimoAdicionados;
	}

}
