package br.com.bills.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.com.bills.controller.util.BillsConstants;
import br.com.bills.controller.util.FacesUtils;
import br.com.bills.dao.MovimentacaoDao;
import br.com.bills.dao.PlanejamentoDao;
import br.com.bills.model.Categoria;
import br.com.bills.model.Planejamento;
import br.com.bills.service.MovimentacaoFinanceira;

@ManagedBean
@RequestScoped
public class PlanejamentoBean {

	@ManagedProperty("#{facesUtils}")
	private FacesUtils facesUtils;

	@ManagedProperty("#{usuarioWeb}")
	private UsuarioWeb usuarioWeb;

	@ManagedProperty("#{planejamentoDao}")
	private PlanejamentoDao planejamentoDao;

	@ManagedProperty("#{movimentacaoDao}")
	private MovimentacaoDao movimentacaoDao;

	@ManagedProperty("#{movimentacaoFinanceira}")
	private MovimentacaoFinanceira movimentacaoFinanceira;

	private Planejamento planejamento = new Planejamento();
	private List<Planejamento> planejamentoMensal = new ArrayList<Planejamento>();
	private List<Categoria> categorias = null;
	private List<Categoria> categoriasMensal = new ArrayList<Categoria>();

	public String prepararPlanejamento() {
		listarPlanejamentoMesAtual();
		categoriasMensal = movimentacaoFinanceira.somarPorCategoriaMensal(Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.YEAR), usuarioWeb.getUsuario());
		return "/pages/planejamento.xhtml";
	}

	public void gerarNovoPlanejamento() {
		if (planejamentoVazio()) {
			listarTodasCategorias();
			for (Categoria categoria : categorias) {
				Planejamento plan = new Planejamento();
				plan.setCategoria(categoria);
				plan.setData(new Date());
				plan.setUsuario(usuarioWeb.getUsuario());
				plan.setDescricao(BillsConstants.EMPTY);
				plan.setValor(0.00);
				planejamentoMensal.add(plan);
			}
			for (Planejamento plan : planejamentoMensal) {
				planejamentoDao.salva(plan);
			}
			facesUtils.adicionaMensagemDeInformacao("Planejamento gerado com sucesso!");
		}
	}

	public void salvarPlanejamento() {
		planejamentoDao.salva(planejamento);
		facesUtils.adicionaMensagemDeInformacao("Planejamento salvo com sucesso!");
	}

	private List<Categoria> listarTodasCategorias() {
		if (categorias == null) {
			categorias = movimentacaoDao.listarCategorias();
		}
		return categorias;
	}

	public boolean planejamentoVazio() {
		return planejamentoMensal.size() == 0 ? true : false;
	}

	private void listarPlanejamentoMesAtual() {
		planejamentoMensal = planejamentoDao.listarMesAtual();
	}

	public FacesUtils getFacesUtils() {
		return facesUtils;
	}

	public void setFacesUtils(FacesUtils facesUtils) {
		this.facesUtils = facesUtils;
	}

	public UsuarioWeb getUsuarioWeb() {
		return usuarioWeb;
	}

	public void setUsuarioWeb(UsuarioWeb usuarioWeb) {
		this.usuarioWeb = usuarioWeb;
	}

	public Planejamento getPlanejamento() {
		return planejamento;
	}

	public void setPlanejamento(Planejamento planejamento) {
		this.planejamento = planejamento;
	}

	public PlanejamentoDao getPlanejamentoDao() {
		return planejamentoDao;
	}

	public void setPlanejamentoDao(PlanejamentoDao planejamentoDao) {
		this.planejamentoDao = planejamentoDao;
	}

	public List<Planejamento> getPlanejamentoMensal() {
		return planejamentoMensal;
	}

	public void setPlanejamentoMensal(List<Planejamento> planejamentoMensal) {
		this.planejamentoMensal = planejamentoMensal;
	}

	public MovimentacaoDao getMovimentacaoDao() {
		return movimentacaoDao;
	}

	public void setMovimentacaoDao(MovimentacaoDao movimentacaoDao) {
		this.movimentacaoDao = movimentacaoDao;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<Categoria> getCategoriasMensal() {
		return categoriasMensal;
	}

	public void setCategoriasMensal(List<Categoria> categoriasMensal) {
		this.categoriasMensal = categoriasMensal;
	}

	public MovimentacaoFinanceira getMovimentacaoFinanceira() {
		return movimentacaoFinanceira;
	}

	public void setMovimentacaoFinanceira(MovimentacaoFinanceira movimentacaoFinanceira) {
		this.movimentacaoFinanceira = movimentacaoFinanceira;
	}

}
