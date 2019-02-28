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

import org.apache.commons.mail.EmailException;
import org.primefaces.context.RequestContext;

import br.com.dao.AlunoDao;
import br.com.dao.EmprestimoDao;
import br.com.dao.LivroDao;
import br.com.dao.ReservaDao;
import br.com.enums.TipoEmprestimo;
import br.com.proline.model.Aluno;
import br.com.proline.model.Emprestimo;
import br.com.proline.model.Livro;
import br.com.proline.model.Reserva;
import br.com.util.Data;
import br.com.util.Email;
import br.com.util.FacesUtil;

@SessionScoped
@ManagedBean(name = "emprestimoController")
public class EmprestimoController implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int DIAS_EMPRESTIMO = 15;
	private static final int NUM_LIVROS_POR_VEZ = 3;
	private static final int DIAS_RENOVACAO = 7;
	private static final int NUM_LIMITE_RENOVACOES = 2;
	private static final int DIAS_RESERVA = 2;

	private Emprestimo emprestimo;
	private Reserva reserva;
	private Emprestimo filtro;
	private ConfiguracoesController configuracoesController;

	private Livro livro;
	private Aluno aluno;

	// DAOS
	private AlunoDao alunoDao;
	private LivroDao livroDao;
	private ReservaDao reservaDao;
	private EmprestimoDao emprestimoDao;

	// listas
	private List<Emprestimo> listagemEmprestimo;
	private List<Emprestimo> listaEmprestimo;
	private List<Emprestimo> emprestimoSelecionadas;
	private List<Emprestimo> listaEmprestimoDevolvidos;
	private List<Emprestimo> listaEmprestimoRenovados;
	private List<Emprestimo> listaEmprestimoAdicionados;

	private List<Reserva> listaReserva;
	private List<Reserva> listaReservaAdicionadas;
	private List<Reserva> listaReservaExcluidos;
	private List<SelectItem> selectItems;

	public EmprestimoController() {
		super();
		limpar();
	}

	public void limpar() {
		// instancia models
		filtro = new Emprestimo();
		filtro.setLivro(new Livro());
		filtro.setAluno(new Aluno());

		emprestimo = new Emprestimo();
		configuracoesController = new ConfiguracoesController();
		livro = new Livro();
		aluno = new Aluno();

		// instancia daos
		emprestimoDao = new EmprestimoDao();
		reservaDao = new ReservaDao();
		livroDao = new LivroDao();
		alunoDao = new AlunoDao();

		// instancia listas
		listagemEmprestimo = new ArrayList<Emprestimo>();
		listaEmprestimo = new ArrayList<Emprestimo>();
		listaEmprestimoDevolvidos = new ArrayList<Emprestimo>();
		listaEmprestimoAdicionados = new ArrayList<Emprestimo>();
		listaEmprestimoRenovados = new ArrayList<Emprestimo>();

		listaReserva = new ArrayList<Reserva>();
		listaReservaExcluidos = new ArrayList<Reserva>();
		listaReservaAdicionadas = new ArrayList<Reserva>();

		emprestimoSelecionadas = new ArrayList<Emprestimo>();
		selectItems = new ArrayList<SelectItem>();
	}

	public String salvar() {

		emprestimo = new Emprestimo();
		incluirEmprestimos();
		finalizarEmprestimos();

		incluirReservas();
		finalizarReservas();

		enviarEmail();

		carregarEmprestimo();
		FacesUtil.addInfoMessage("Operação realizada com sucesso !");
		return "lista-emprestimo.xhtml?faces-redirect=true";

	}

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
			res.setDataEncerramentoReserva(new Date());
			res.setStatus(false);
			reservaDao.salvarOuAtualizar(res);
		}
	}

	public void expiraReservas() {
		for (Reserva res : reservaDao
				.findByReservasExpiradas(configuracoesController.getValorConfiguracoes().getDiasDuracaoReserva())) {
			res.setStatus(false);
			res.setExpirada(true);
			res.setDataEncerramentoReserva(new Date());
			reservaDao.salvarOuAtualizar(res);
		}

	}

	public String novo() {
		limpar();
		// chamando de index
		return "/pages/emprestimo/cadastro-emprestimo?faces-redirect=true";
	}

	public String editar() {
		//aluno = emprestimo.getAluno();
		livro = new Livro();
		listaEmprestimo = emprestimoDao.findByAluno(aluno);
		listaReserva = reservaDao.findByAluno(aluno);
		return "cadastro-emprestimo.xhtml?faces-redirect=true";

	}

	/*
	 * public void excluirSelecionados() { if (!emprestimoSelecionadas.isEmpty()) {
	 * for (Emprestimo m : emprestimoSelecionadas) { emprestimoDao.excluirPorId(m);
	 * listagemEmprestimo.remove(m); }
	 * 
	 * FacesUtil.addInfoMessage("Emprestimo(s) excluído(s) com sucesso !"); }
	 * 
	 * else FacesUtil.addErroMessage("Nenhum Emprestimo selecionado");
	 * 
	 * }
	 */
	/*
	 * public void desativarSelecionados() { if (!emprestimoSelecionadas.isEmpty())
	 * { for (Emprestimo m : emprestimoSelecionadas) {
	 * emprestimoDao.desativaPorId(m); ; listagemEmprestimo.remove(m); }
	 * 
	 * FacesUtil.addInfoMessage("Emprestimo(s) excluído(s) com sucesso !"); }
	 * 
	 * else FacesUtil.addErroMessage("Nenhum Emprestimo selecionado");
	 * 
	 * }
	 */
	public String excluir() {
		emprestimoDao.excluirPorId(emprestimo);
		emprestimo = new Emprestimo();
		carregarEmprestimo();
		return "";
	}

	@PostConstruct
	public void carregarEmprestimo() {
		emprestimoDao.getListEntity(Emprestimo.class);
	}

	public void buscarEmprestimo() {
		listagemEmprestimo = emprestimoDao.findByExample(filtro);
	}

	public String listar() {
		carregarEmprestimo();
		return "/pages/emprestimo/lista-emprestimo.xhtml?faces-redirect=true";
	}

	// getters e setters
	/*
	 * public List<SelectItem> getSelectItems() { selectItems = new
	 * ArrayList<SelectItem>(); if (selectItems.size() == 0) { for (Emprestimo
	 * emprestimo : listaEmprestimo) { selectItems.add(new SelectItem(emprestimo,
	 * emprestimo.getTitulo())); } } return selectItems; }
	 */

	///////////// REGRA DE NEGÓCIOS /////////////////

	public void enviarEmail() {
		if (!listaEmprestimoAdicionados.isEmpty()) {
			Email.emailEmprestimo(aluno, listaEmprestimoAdicionados, configuracoesController.getValorConfiguracoes(),
					TipoEmprestimo.EMPRESTIMO);
		}
		if (!listaEmprestimoDevolvidos.isEmpty()) {
			Email.emailEmprestimo(aluno, listaEmprestimoDevolvidos, configuracoesController.getValorConfiguracoes(),
					TipoEmprestimo.DEVOLUCAO);
		}
		if (!listaEmprestimoRenovados.isEmpty()) {
			Email.emailEmprestimo(aluno, listaEmprestimoRenovados, configuracoesController.getValorConfiguracoes(),
					TipoEmprestimo.RENOVACAO);
		}

		if (!listaReservaAdicionadas.isEmpty()) {
			Email.emailReserva(aluno, listaReservaAdicionadas, configuracoesController.getValorConfiguracoes());
		}

	}

	public void consultaAluno() {
		if (aluno.getMatricula() != null && alunoDao.findByMatricula(aluno.getMatricula()) != null) {

			aluno = alunoDao.findByMatricula(aluno.getMatricula());
			listaEmprestimo = emprestimoDao.findByAluno(aluno);
			listaReserva = reservaDao.findByAluno(aluno);
		}

		else if (aluno.getMatricula() != null && !aluno.getMatricula().isEmpty()
				&& alunoDao.findByMatricula(aluno.getMatricula()) == null) {
			FacesUtil.addErroMessage("Nenhum aluno encontrado para esta matrícula.");
			aluno = new Aluno();
			livro = new Livro();
		}

	}

	public void consultaLivro() {

		if (!livro.getCodigo().trim().isEmpty()) {
			Livro livroAux = livroDao.findByCodigo(livro.getCodigo());

			if (livro.getCodigo() != null && livroAux != null && !emprestimoExisteNaLista(livroAux.getId()))
				livro = livroAux;

			else if (livroAux == null) {
				FacesUtil.addErroMessage("Nenhum livro encontrado para este código.");
				livro = new Livro();
			}
		}

	}

	public boolean desabilitaEmprestimo() {
		return existeEmprestimo() || existeReserva();
	}

	public boolean existeEmprestimo() {
		Emprestimo emprestimoAux = emprestimoDao.findByLivro(livro);
		emprestimo = emprestimoAux;
		return emprestimoAux != null;
	}

	public boolean existeReserva() {
		Reserva reservaAux = reservaDao.findByLivro(livro);
		return reservaAux != null;
	}

	public boolean desabilitaReserva() {
		return existeReserva() || !existeEmprestimo();
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

	public int contaDias(Emprestimo emp) {
		Date dataFim = emp.isStatus() ? new Date() : emp.getDataDevolucao();
		return Data.subtraiDatas(emp.getDataEmprestimo(), dataFim);
	}

	public boolean emprestimoEmDia(Emprestimo emp) {
		return new Date().before(emp.getDataDevolucaoPrevista());
	}

	public void adicionarEmprestimo() {
		if (permiteAdicionarEmprestimo()) {
			Emprestimo emprestimo = new Emprestimo();
			emprestimo.setAluno(aluno);
			emprestimo.setLivro(livro);
			emprestimo.setDataEmprestimo(new Date());
			emprestimo.setDataDevolucaoPrevista(Data.somaDatas(emprestimo.getDataEmprestimo(),
					configuracoesController.getValorConfiguracoes().getDiasDuracaoEmprestimo()));
			emprestimo.setSituacao(1);

			listaEmprestimoAdicionados.add(emprestimo);
			listaEmprestimo.add(emprestimo);
			livro = new Livro();

			// RequestContext.getCurrentInstance().execute("PF('dlgEmprestimo').hide()");

		}
	}

	public boolean permiteAdicionarEmprestimo() {
		boolean jaAdicionado = emprestimoExisteNaLista(livro.getId());
		boolean reservado = reservaDao.findByLivro(livro) != null;
		boolean maximoEmprestimoPermitidos = (listaEmprestimo.size() >= configuracoesController.getValorConfiguracoes()
				.getLivrosPorAluno());

		if (jaAdicionado) {
			FacesUtil.addErroMessage("Livro já adicionado");
		}

		else if (reservado) {
			FacesUtil.addErroMessage("Livro reservado");
		}

		else if (maximoEmprestimoPermitidos) {
			FacesUtil.addErroMessage("Número máximo de livros atingido.");
		}

		return (!jaAdicionado && !reservado && !maximoEmprestimoPermitidos);
	}

	public void adicionarReserva() {
		if (livro.getId() != null && aluno.getId() != null && !reservaExisteNaLista(livro.getId())) {
			Reserva reserva = new Reserva();
			reserva.setAluno(aluno);
			reserva.setEmprestimo(emprestimo);
			reserva.setDataReserva(new Date());

			listaReservaAdicionadas.add(reserva);
			listaReserva.add(reserva);
			livro = new Livro();

		}
	}

	public void removeEmprestimo(Emprestimo emprestimo) {
		listaEmprestimoAdicionados.remove(emprestimo);
		listaEmprestimo.remove(emprestimo);
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
		emprestimo.setDataDevolucaoPrevista(Data.somaDatas(emprestimo.getDataEmprestimo(),
				configuracoesController.getValorConfiguracoes().getDiasDuracaoEmprestimo()));
		emprestimo.setSituacao(1);

		listaEmprestimo.add(emprestimo);
		FacesUtil.addInfoMessage("Movido para empréstimo !");

	}

	public void devolverLivro() {
		if (emprestimo.getId() != null) {

			listaEmprestimo.remove(emprestimo);
			listaEmprestimoDevolvidos.add(emprestimo);
			RequestContext.getCurrentInstance().execute("PF('dlgPrimary').hide()");
			FacesUtil.addInfoMessage("Devolução efetuada com sucesso !");
		}

	}

	public Date novaDataDevolucao() {
		// soma de sete dias;
		return Data.somaDatas(emprestimo.getDataDevolucaoPrevista(), 7);
	}

	///// METODOS DE RENOVACAO

	public void renovarEmprestimo() {

		if (permiteRenovacao()) {
			emprestimo.setRenovacoes(emprestimo.getRenovacoes() + 1);
			emprestimo.setDataDevolucaoPrevista(Data.somaDatas(emprestimo.getDataDevolucaoPrevista(),
					configuracoesController.getValorConfiguracoes().getDiasDuracaoRenovacao()));
			listaEmprestimoRenovados.add(emprestimo);
			RequestContext.getCurrentInstance().execute("PF('dlgRenovacao').hide()");
		}

	}

	public boolean permiteRenovacao() {
		// numero de dias que o aluno está com o livro
		int diasDePosse = Data.subtraiDatas(emprestimo.getDataEmprestimo(), new Date());
		// numero total de dias que o aluno tem direito
		int diasTotal = Data.subtraiDatas(emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucaoPrevista());

		boolean minimoDiasAtingido = (diasDePosse >= diasTotal * 0.5);
		boolean existeReserva = reservaDao.findByEmprestimo(emprestimo) != null;
		boolean renovacoesAtingidas = emprestimo.getRenovacoes() >= configuracoesController.getValorConfiguracoes()
				.getRenovacoesPorLivro();

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

	//// metodos reserva
	public void removeReserva(Reserva reserva) {
		listaReservaAdicionadas.remove(reserva);
		listaReserva.remove(reserva);
		if (reserva.getId() != null) {
			listaReservaExcluidos.add(reserva);
		}
	}

	public Integer getTotal() {
		return emprestimoDao.getListEntity(Emprestimo.class).size();
	}
	
	public int getSizeEmprestimos() {
		return listaEmprestimo.size();
	}
	
	public int getSizeReservas() {
		return listaReserva.size();
	}
	

	/////////////// GETTERS AND SETTERS//////////////

	/*
	 * public int getDuracaoEmprestimo() { return DIAS_EMPRESTIMO; }
	 * 
	 * public int getNumLivrosAluno() { return NUM_LIVROS_POR_VEZ; }
	 * 
	 * public int getDuracaoRenovacao() { return DIAS_RENOVACAO; }
	 * 
	 * public int getRenovacoesPossiveisPorLivro() { return NUM_LIMITE_RENOVACOES; }
	 * 
	 * public int getDuracaoReserva() { return DIAS_RESERVA; }
	 */

	public Emprestimo getEmprestimo() {
		return emprestimo;
	}

	public void setEmprestimo(Emprestimo emprestimo) {
		this.emprestimo = emprestimo;
	}

	public EmprestimoDao getEmprestimoDao() {
		return emprestimoDao;
	}

	public void setEmprestimoDao(EmprestimoDao emprestimoDao) {
		this.emprestimoDao = emprestimoDao;
	}

	public List<Emprestimo> getEmprestimoSelecionadas() {
		return emprestimoSelecionadas;
	}

	public void setEmprestimoSelecionadas(List<Emprestimo> emprestimoSelecionadas) {
		this.emprestimoSelecionadas = emprestimoSelecionadas;
	}

	public void setSelectItems(List<SelectItem> selectItems) {
		this.selectItems = selectItems;
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Emprestimo getFiltro() {
		return filtro;
	}

	public void setFiltro(Emprestimo filtro) {
		this.filtro = filtro;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public AlunoDao getAlunoDao() {
		return alunoDao;
	}

	public void setAlunoDao(AlunoDao alunoDao) {
		this.alunoDao = alunoDao;
	}

	public LivroDao getLivroDao() {
		return livroDao;
	}

	public void setLivroDao(LivroDao livroDao) {
		this.livroDao = livroDao;
	}

	public ReservaDao getReservaDao() {
		return reservaDao;
	}

	public void setReservaDao(ReservaDao reservaDao) {
		this.reservaDao = reservaDao;
	}

	public List<Emprestimo> getListagemEmprestimo() {
		return listagemEmprestimo;
	}

	public void setListagemEmprestimo(List<Emprestimo> listagemEmprestimo) {
		this.listagemEmprestimo = listagemEmprestimo;
	}

	public List<Emprestimo> getListaEmprestimo() {
		return listaEmprestimo;
	}

	public void setListaEmprestimo(List<Emprestimo> listaEmprestimo) {
		this.listaEmprestimo = listaEmprestimo;
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

	public List<Reserva> getListaReservaAdicionadas() {
		return listaReservaAdicionadas;
	}

	public void setListaReservaAdicionadas(List<Reserva> listaReservaAdicionadas) {
		this.listaReservaAdicionadas = listaReservaAdicionadas;
	}

	public ConfiguracoesController getConfiguracoesController() {
		return configuracoesController;
	}

	public void setConfiguracoesController(ConfiguracoesController configuracoesController) {
		this.configuracoesController = configuracoesController;
	}

	public List<Emprestimo> getListaEmprestimoRenovados() {
		return listaEmprestimoRenovados;
	}

	public void setListaEmprestimoRenovados(List<Emprestimo> listaEmprestimoRenovados) {
		this.listaEmprestimoRenovados = listaEmprestimoRenovados;
	}

}
