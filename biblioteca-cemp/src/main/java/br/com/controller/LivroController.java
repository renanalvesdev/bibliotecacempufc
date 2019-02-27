package br.com.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import br.com.dao.LivroDao;
import br.com.proline.model.Livro;
import br.com.util.FacesUtil;

@SessionScoped
@ManagedBean(name = "livroController")
public class LivroController implements Serializable {

	private static final long serialVersionUID = 1L;

	private Livro livro;
	private Livro filtro;
	
	
	private LivroDao livroDao;
	
	private List<Livro> listaLivro;
	private List<Livro> livroSelecionadas;
	private List<SelectItem> selectItems;

	public LivroController() {
		super();
		limpar();
	}

	public void limpar() {
		livro = new Livro();
		filtro = new Livro();
		livroDao = new LivroDao();
		listaLivro = new ArrayList<Livro>();
		livroSelecionadas = new ArrayList<Livro>();
		selectItems = new ArrayList<SelectItem>();
	}

	public String salvar() {
		livroDao.salvarOuAtualizar(livro);
		livro = new Livro();
		carregarLivro();
		FacesUtil.addInfoMessage("Operação realizada com sucesso !");
		return "lista-livro.xhtml?faces-redirect=true";

	}

	public String novo() {
		livro = new Livro();
		return "cadastro-livro?faces-redirect=true";
	}

	public String editar() {
		return "cadastro-livro.xhtml?faces-redirect=true";

	}
	

	public void buscarLivro() {
		listaLivro = livroDao.findByExample(filtro);
	}

	/*
	 * public void excluirSelecionados() { if (!livroSelecionadas.isEmpty()) { for
	 * (Livro m : livroSelecionadas) { livroDao.excluirPorId(m);
	 * listaLivro.remove(m); }
	 * 
	 * FacesUtil.addInfoMessage("Livro(s) excluído(s) com sucesso !"); }
	 * 
	 * else FacesUtil.addErroMessage("Nenhum Livro selecionado");
	 * 
	 * }
	 */

	/*
	 * public void desativarSelecionados() { if (!livroSelecionadas.isEmpty()) { for
	 * (Livro m : livroSelecionadas) { livroDao.desativaPorId(m); ;
	 * listaLivro.remove(m); }
	 * 
	 * FacesUtil.addInfoMessage("Livro(s) excluído(s) com sucesso !"); }
	 * 
	 * else FacesUtil.addErroMessage("Nenhum Livro selecionado");
	 * 
	 * }
	 */
	public String excluir() {
		livroDao.excluirPorId(livro);
		livro = new Livro();
		carregarLivro();
		return "";
	}

	@PostConstruct
	public void carregarLivro() {
		listaLivro = livroDao.getListEntity(Livro.class);
	}

	public String listar() {
		carregarLivro();
		return "/pages/livro/lista-livro.xhtml?faces-redirect=true";
	}

	// getters e setters
	public List<SelectItem> getSelectItems() {
		selectItems = new ArrayList<SelectItem>();
		if (selectItems.size() == 0) {
			for (Livro livro : listaLivro) {
				selectItems.add(new SelectItem(livro, livro.getTitulo()));
			}
		}
		return selectItems;
	}
	
	public void dlgReserva(boolean status, boolean reservado) {
		if(!status || !reservado ) {
			
		}
	}

	public Integer getTotal() {
		return livroDao.getListEntity(Livro.class).size();
	}
	
	public Livro getFiltro() {
		return filtro;
	}

	public void setFiltro(Livro filtro) {
		this.filtro = filtro;
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

	public List<Livro> getListaLivro() {
		return listaLivro;
	}

	public void setListaLivro(List<Livro> listaLivro) {
		this.listaLivro = listaLivro;
	}

	public List<Livro> getLivroSelecionadas() {
		return livroSelecionadas;
	}

	public void setLivroSelecionadas(List<Livro> livroSelecionadas) {
		this.livroSelecionadas = livroSelecionadas;
	}

	public void setSelectItems(List<SelectItem> selectItems) {
		this.selectItems = selectItems;
	}

}
