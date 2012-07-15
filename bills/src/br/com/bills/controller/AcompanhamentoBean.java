package br.com.bills.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.chart.PieChartModel;

import br.com.bills.controller.util.FacesUtils;
import br.com.bills.dao.MovimentacaoDao;
import br.com.bills.model.Categoria;
import br.com.bills.model.Movimentacao;
import br.com.bills.util.Utils;

@ManagedBean
@RequestScoped
public class AcompanhamentoBean {

	@ManagedProperty("#{facesUtils}")
	private FacesUtils facesUtils;

	@ManagedProperty("#{usuarioWeb}")
	private UsuarioWeb usuarioWeb;

	@ManagedProperty("#{movimentacaoDao}")
	private MovimentacaoDao movimentacaoDao;

	private List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();
	private List<Categoria> categorias = new ArrayList<Categoria>();

	private PieChartModel pieModel;
	private PieChartModel pieModelMensal;

	public String prepararAcompanhamento() {
		movimentacoes = movimentacaoDao.listarTodas(usuarioWeb.getUsuario());
		categorias = movimentacaoDao.listarCategorias();
		somarPorCategoriaGeral();
		somarPorCategoriaMensal(Calendar.getInstance().get(Calendar.MONTH));
		return "/pages/acompanhamento.xhtml";
	}

	private void somarPorCategoriaGeral() {
		pieModel = new PieChartModel();

		for (Categoria categoria : categorias) {
			categoria.setTotal(0);
			double soma = 0;
			for (Movimentacao movimentacao : movimentacoes) {
				if (categoria.getNome().equals(movimentacao.getCategoria().getNome())) {
					soma += movimentacao.getValor();
				}
			}
			categoria.setTotal(categoria.getTotal() + Utils.precision(soma));
			if (categoria.getTotal() > 0) {
				pieModel.set(categoria.getNome() + ": $ " + Utils.formatarReal(categoria.getTotal()),
						categoria.getTotal());
			}
		}
	}

	private void somarPorCategoriaMensal(int mes) {
		pieModelMensal = new PieChartModel();

		for (Categoria categoria : categorias) {
			categoria.setTotal(0);
			double soma = 0;
			for (Movimentacao movimentacao : movimentacoes) {
				Calendar dataMov = Calendar.getInstance();
				dataMov.setTime(movimentacao.getData());
				int mesMov = dataMov.get(Calendar.MONTH);

				if (categoria.getNome().equals(movimentacao.getCategoria().getNome())
 && mes == mesMov) {
					soma += movimentacao.getValor();
				}
			}
			categoria.setTotal(categoria.getTotal() + Utils.precision(soma));
			if (categoria.getTotal() > 0) {
				pieModelMensal.set(categoria.getNome() + ": $ " + Utils.formatarReal(categoria.getTotal()),
						categoria.getTotal());
			}
		}
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

	public MovimentacaoDao getMovimentacaoDao() {
		return movimentacaoDao;
	}

	public void setMovimentacaoDao(MovimentacaoDao movimentacaoDao) {
		this.movimentacaoDao = movimentacaoDao;
	}

	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public PieChartModel getPieModel() {
		return pieModel;
	}

	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel;
	}

	public PieChartModel getPieModelMensal() {
		return pieModelMensal;
	}

	public void setPieModelMensal(PieChartModel pieModelMensal) {
		this.pieModelMensal = pieModelMensal;
	}

}
