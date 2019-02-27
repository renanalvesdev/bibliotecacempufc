package br.com.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import br.com.dao.ReservaDao;
import br.com.enums.PermissaoEnum;
import br.com.proline.model.Reserva;
import br.com.util.FacesUtil;

@SessionScoped
@ManagedBean(name = "reservaController")
public class ReservaController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String confirmaSenha;
	private Reserva filtro;
	private Reserva reserva;
	private ReservaDao reservaDao;
	private List<Reserva> listaReserva;
	private List<Reserva> reservaSelecionadas;
	private List<SelectItem> selectItems;

	public ReservaController() {
		super();
		limpar();
	}

	public void limpar() {

		filtro = new Reserva();
		reserva = new Reserva();
		reservaDao = new ReservaDao();
		listaReserva = new ArrayList<Reserva>();
		reservaSelecionadas = new ArrayList<Reserva>();
		selectItems = new ArrayList<SelectItem>();
	}

	public String salvar() {
		reservaDao.salvarOuAtualizar(reserva);
		reserva = new Reserva();
		carregarReserva();
		return "lista-reserva.xhtml?faces-redirect=true";
	}

	public String editar() {
		return "cadastro-reserva.xhtml?faces-redirect=true";

	}

	public String novo() {
		reserva = new Reserva();
		return "cadastro-reserva?faces-redirect=true";
	}

	/*
	 * public void excluirSelecionados() { if (!reservaSelecionadas.isEmpty()) { for
	 * (Reserva m : reservaSelecionadas) { reservaDao.excluirPorId(m);
	 * listaReserva.remove(m); }
	 * 
	 * FacesUtil.addInfoMessage("Reserva(s) excluído(s) com sucesso !"); }
	 * 
	 * else FacesUtil.addErroMessage("Nenhum Reserva selecionado");
	 * 
	 * }
	 */
	/*
	 * public void desativarSelecionados() { if (!reservaSelecionadas.isEmpty()) {
	 * for (Reserva m : reservaSelecionadas) { reservaDao.desativaPorId(m); ;
	 * listaReserva.remove(m); }
	 * 
	 * FacesUtil.addInfoMessage("Reserva(s) excluído(s) com sucesso !"); }
	 * 
	 * else FacesUtil.addErroMessage("Nenhum Reserva selecionado");
	 * 
	 * }
	 */
	public String excluir() {
		reservaDao.excluirPorId(reserva);
		reserva = new Reserva();
		carregarReserva();
		return "";
	}
	
	public Integer getTotal() {
		return reservaDao.getListEntity(Reserva.class).size();
	}

	@PostConstruct
	public void carregarReserva() {
		listaReserva = reservaDao.getListEntity(Reserva.class);
	}
	

	public void buscarReserva() {
		listaReserva = reservaDao.findByExample(filtro);
	}
	
	public Reserva getFiltro() {
		return filtro;
	}

	public void setFiltro(Reserva filtro) {
		this.filtro = filtro;
	}

	public String listar() {
		carregarReserva();
		return "lista-reserva.xhtml?faces-redirect=true";
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

	public List<Reserva> getReservaSelecionadas() {
		return reservaSelecionadas;
	}

	public void setReservaSelecionadas(List<Reserva> reservaSelecionadas) {
		this.reservaSelecionadas = reservaSelecionadas;
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

}
