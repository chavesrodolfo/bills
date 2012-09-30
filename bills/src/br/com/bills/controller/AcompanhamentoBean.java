package br.com.bills.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.com.bills.controller.util.FacesUtils;
import br.com.bills.dao.MovimentacaoDao;
import br.com.bills.model.Categoria;
import br.com.bills.model.Movimentacao;
import br.com.bills.service.MovimentacaoFinanceira;
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

	@ManagedProperty("#{movimentacaoFinanceira}")
	private MovimentacaoFinanceira movimentacaoFinanceira;

	private List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();
	private List<Categoria> categorias = new ArrayList<Categoria>();
	private List<Categoria> categoriasMensal = new ArrayList<Categoria>();

	private PieChartModel pieModel;
	private PieChartModel pieModelMensal;
	private CartesianChartModel graficoLinhasModel;
	private String mesSelecionadoTexto;

	private Long anoSelecionado;
	private Long mesSelecionado;
	private double totalMensal = 0;

	public String prepararAcompanhamento() {
		verificaMesAno();
		categorias = movimentacaoFinanceira.somarPorCategoriaGeral(usuarioWeb.getUsuario());
		carregarCategoriasMensal();
		criarGraficoPizzaGeral();
		criarGraficoPizzaMensal();
		criarGraficoLinhasModel();
		totalMensal();

		return "/pages/acompanhamento.xhtml";
	}

	private List<Categoria> removerCategoriasZeradas() {
		List<Categoria> categoriasUsadas = new ArrayList<Categoria>();
		for (Categoria cat : categoriasMensal) {
			if (cat.getTotal() > 0) {
				categoriasUsadas.add(cat);
			}
		}
		return categoriasUsadas;
	}

	private void totalMensal() {
		for (Categoria categoria : categoriasMensal) {
			totalMensal += categoria.getTotal();
		}
	}

	private void criarGraficoPizzaGeral() {
		pieModel = new PieChartModel();
		for (Categoria categoria : categorias) {
			if (categoria.getTotal() > 0) {
				pieModel.set(categoria.getNome() + ": " + Utils.formatarReal(categoria.getTotal()),
						categoria.getTotal());
			}
		}
		if (pieModel.getData().isEmpty()) {
			pieModel.set("Vazio", 0);
		}
	}

	private void criarGraficoPizzaMensal() {
		pieModelMensal = new PieChartModel();
		for (Categoria categoria : categoriasMensal) {
			if (categoria.getTotal() > 0) {
				pieModelMensal.set(categoria.getNome() + ": " + Utils.formatarReal(categoria.getTotal()),
						categoria.getTotal());
			}
		}
		if (pieModelMensal.getData().isEmpty()) {
			pieModelMensal.set("Vazio", 0);
		}
	}

	public void carregarMes(Long mes) {
		mesSelecionado = mes;
		verificaMesAno();
		carregarCategoriasMensal();
		criarGraficoPizzaGeral();
		criarGraficoPizzaMensal();
		totalMensal();
	}

	public void carregarAno(Long ano) {
		anoSelecionado = ano;
		verificaMesAno();
		carregarCategoriasMensal();
		criarGraficoLinhasModel();
		totalMensal();
	}

	private void carregarCategoriasMensal() {
		categoriasMensal = movimentacaoFinanceira.somarPorCategoriaMensal(Integer.parseInt(mesSelecionado.toString()),
				Integer.parseInt(anoSelecionado.toString()), usuarioWeb.getUsuario());
		categoriasMensal = removerCategoriasZeradas();
	}

	private void verificaMesAno() {
		if (anoSelecionado == null) {
			anoSelecionado = Long.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		}
		if (mesSelecionado == null) {
			mesSelecionado = Long.valueOf(Calendar.getInstance().get(Calendar.MONTH));
		}
		mesSelecionadoTexto = Utils.getMes(Integer.valueOf(mesSelecionado.toString()));
	}

	private void criarGraficoLinhasModel() {
		graficoLinhasModel = new CartesianChartModel();

		ChartSeries receitas = new ChartSeries();
		receitas.setLabel("Receita");

		ChartSeries despesas = new ChartSeries();
		despesas.setLabel("Despesa");
		verificaMesAno();
		for (int i = 0; i <= Utils.getMesAtual(); i++) {
			double somaMovimentacao = movimentacaoFinanceira.somarMovimentacaoMes(i,
					Integer.parseInt(anoSelecionado.toString()), usuarioWeb.getUsuario());
			// if (somaMovimentacao > 0) {
			despesas.set(Utils.getMes(i), somaMovimentacao);
			// }
		}

		graficoLinhasModel.addSeries(despesas);
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

	public List<Categoria> getCategoriasMensal() {
		return categoriasMensal;
	}

	public void setCategoriasMensal(List<Categoria> categoriasMensal) {
		this.categoriasMensal = categoriasMensal;
	}

	public CartesianChartModel getGraficoLinhasModel() {
		return graficoLinhasModel;
	}

	public void setGraficoLinhasModel(CartesianChartModel graficoLinhasModel) {
		this.graficoLinhasModel = graficoLinhasModel;
	}

	public String getmesSelecionadoTexto() {
		return mesSelecionadoTexto;
	}

	public void setmesSelecionadoTexto(String mesSelecionadoTexto) {
		this.mesSelecionadoTexto = mesSelecionadoTexto;
	}

	public String getMesSelecionadoTexto() {
		return mesSelecionadoTexto;
	}

	public void setMesSelecionadoTexto(String mesSelecionadoTexto) {
		this.mesSelecionadoTexto = mesSelecionadoTexto;
	}

	public Long getAnoSelecionado() {
		return anoSelecionado;
	}

	public void setAnoSelecionado(Long anoSelecionado) {
		this.anoSelecionado = anoSelecionado;
	}

	public Long getMesSelecionado() {
		return mesSelecionado;
	}

	public void setMesSelecionado(Long mesSelecionado) {
		this.mesSelecionado = mesSelecionado;
	}

	public MovimentacaoFinanceira getMovimentacaoFinanceira() {
		return movimentacaoFinanceira;
	}

	public void setMovimentacaoFinanceira(MovimentacaoFinanceira movimentacaoFinanceira) {
		this.movimentacaoFinanceira = movimentacaoFinanceira;
	}

	public double getTotalMensal() {
		return totalMensal;
	}

	public void setTotalMensal(double totalMensal) {
		this.totalMensal = totalMensal;
	}

}
