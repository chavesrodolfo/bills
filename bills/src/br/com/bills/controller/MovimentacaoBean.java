package br.com.bills.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import br.com.bills.controller.util.BillsConstants;
import br.com.bills.controller.util.FacesUtils;
import br.com.bills.dao.MovimentacaoDao;
import br.com.bills.model.Categoria;
import br.com.bills.model.Movimentacao;

@ManagedBean
@RequestScoped
public class MovimentacaoBean {

	@ManagedProperty("#{facesUtils}")
	private FacesUtils facesUtils;

	@ManagedProperty("#{usuarioWeb}")
	private UsuarioWeb usuarioWeb;

	@ManagedProperty("#{movimentacaoDao}")
	private MovimentacaoDao movimentacaoDao;

	private ScheduleModel eventoModel;
	private DefaultScheduleEvent evento = new DefaultScheduleEvent();

	private Movimentacao movimentacao = new Movimentacao();

	private List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();

	private List<Categoria> categorias = null;

	private static final String ESTADO_DE_NOVO = "_novo";
	private static final String ESTADO_DE_EDICAO = "_edicao";
	private static final String ESTADO_DE_PESQUISA = "_pesquisa";

	private String state = ESTADO_DE_PESQUISA;

	public String prepararMovimentacao() {
		movimentacao = new Movimentacao();
		movimentacao.setCategoria(new Categoria());
		listaMovimentacoes();
		adicionarEvento(new Date());
		return "/pages/movimentacao.xhtml";
	}

	private void adicionarEvento(Date date) {
		movimentacao = new Movimentacao();
		movimentacao.setCategoria(new Categoria());
		evento = new DefaultScheduleEvent();
		evento.setTitle(BillsConstants.EMPTY);
		evento.setStartDate(nextDay(date));
		evento.setEndDate(date);
		evento.setEditable(false);
	}

	private void listaMovimentacoes() {
		movimentacoes = movimentacaoDao.listarTodas(usuarioWeb.getUsuario());
		eventoModel = new DefaultScheduleModel();
		eventoModel = new DefaultScheduleModel();
		for (Movimentacao mov : movimentacoes) {
			DefaultScheduleEvent novoEvento = new DefaultScheduleEvent();
			novoEvento.setTitle(mov.getDescricao());
			novoEvento.setStartDate(mov.getData());
			novoEvento.setEndDate(mov.getData());
			novoEvento.setAllDay(true);
			novoEvento.setData(mov.getId());
			novoEvento.setEditable(false);
			eventoModel.addEvent(novoEvento);
		}
	}

	public void addEvent(ActionEvent actionEvent) {
		evento.setAllDay(true);
		if (evento.getData() == null) {
			eventoModel.addEvent(evento);
		} else {
			eventoModel.updateEvent(evento);
		}
		salvarEvento();
		prepararMovimentacao();
	}

	private void salvarEvento() {
		movimentacao.setData(nextDay(evento.getEndDate()));
		movimentacao.setDescricao(evento.getTitle());
		movimentacao.setUsuario(usuarioWeb.getUsuario());
		if (state.equals(ESTADO_DE_EDICAO)) {
			movimentacaoDao.atualizar(movimentacao);
		} else {
			movimentacaoDao.salvar(movimentacao);
		}
	}

	public List<String> completeCategoria(String query) {
		Set<String> results = new HashSet<String>();
		categorias = getTodasCategorias();

		for (Categoria c : categorias) {
			if (c != null && c.getNome() != null) {
				if (c.getNome().toUpperCase().startsWith(query.toUpperCase()))
					results.add(c.getNome());
			}
		}
		List<String> resultsList = new ArrayList<String>(results);
		return resultsList;
	}

	private List<Categoria> getTodasCategorias() {
		if (categorias == null) {
			categorias = movimentacaoDao.listarCategorias();
		}
		return categorias;
	}

	@SuppressWarnings("unused")
	private void atualizarEvento(ScheduleEvent evento) {
		Movimentacao mov = movimentacaoDao.carrega((Long) evento.getData());
		mov.setData(evento.getStartDate());
		movimentacaoDao.salvar(mov);
	}

	public void onEventSelect(ScheduleEntrySelectEvent selectEvent) {
		movimentacao = movimentacaoDao.carrega((Long) selectEvent.getScheduleEvent().getData());
		evento.setStartDate(lastDay(movimentacao.getData()));
		evento.setEndDate(lastDay(movimentacao.getData()));
		evento.setEditable(false);
		evento.setData(movimentacao.getId());
		evento.setTitle(movimentacao.getDescricao());
		setState(ESTADO_DE_EDICAO);
	}

	public void onDateSelect(DateSelectEvent selectEvent) {
		setState(ESTADO_DE_NOVO);
		adicionarEvento(selectEvent.getDate());
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		// atualizarEvento(event.getScheduleEvent());
		facesUtils.adicionaMensagemDeInformacao("Movido para o dia: " + event.getScheduleEvent().getStartDate());
	}

	private Date nextDay(Date day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		Calendar t = (Calendar) cal.clone();
		t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
		return t.getTime();
	}

	private Date lastDay(Date day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		Calendar t = (Calendar) cal.clone();
		t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
		return t.getTime();
	}

	public FacesUtils getFacesUtils() {
		return facesUtils;
	}

	public void setFacesUtils(FacesUtils facesUtils) {
		this.facesUtils = facesUtils;
	}

	public ScheduleModel getEventoModel() {
		return eventoModel;
	}

	public void setEventoModel(ScheduleModel eventoModel) {
		this.eventoModel = eventoModel;
	}

	public DefaultScheduleEvent getEvento() {
		return evento;
	}

	public void setEvento(DefaultScheduleEvent evento) {
		this.evento = evento;
	}

	public MovimentacaoDao getMovimentacaoDao() {
		return movimentacaoDao;
	}

	public void setMovimentacaoDao(MovimentacaoDao movimentacaoDao) {
		this.movimentacaoDao = movimentacaoDao;
	}

	public Movimentacao getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	public UsuarioWeb getUsuarioWeb() {
		return usuarioWeb;
	}

	public void setUsuarioWeb(UsuarioWeb usuarioWeb) {
		this.usuarioWeb = usuarioWeb;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
