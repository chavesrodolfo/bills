package br.com.bills.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.com.bills.controller.util.BillsConstants;
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
	private List<Categoria> categoriasMensal = new ArrayList<Categoria>();

	private PieChartModel pieModel;
	private PieChartModel pieModelMensal;
	private CartesianChartModel graficoLinhasModel;
	private String mesSelecionado;

	private double totalGeral = 0;
	private double totalMensal = 0;

	public String prepararAcompanhamento() {
		movimentacoes = movimentacaoDao.listarTodas(usuarioWeb.getUsuario());
		categorias = movimentacaoDao.listarCategorias();
		somarPorCategoriaGeral();
		somarPorCategoriaMensal(Calendar.getInstance().get(Calendar.MONTH));
		porcentagemCategoriaMensal();
		criarGraficoLinhasModel();
		mesSelecionado = getMes(getMesAtual());
		return "/pages/acompanhamento.xhtml";
	}

	private void porcentagemCategoriaMensal() {
		for (Categoria categoria : categoriasMensal) {
			categoria.setPorcentagem(Utils.precision(categoria.getTotal() / totalMensal * 100));
		}
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
				pieModel.set(categoria.getNome() + ": R$ " + Utils.formatarReal(categoria.getTotal()),
						categoria.getTotal());
				totalGeral += categoria.getTotal();
			}
		}
		if (pieModel.getData().isEmpty()) {
			pieModel.set("Vazio", 0);
		}
	}

	private void somarPorCategoriaMensal(int mes) {
		pieModelMensal = new PieChartModel();
		categoriasMensal.removeAll(categoriasMensal);
		for (Categoria categoria : categorias) {
			categoria.setTotal(0);
			double soma = 0;
			for (Movimentacao movimentacao : movimentacoes) {
				Calendar dataMov = Calendar.getInstance();
				dataMov.setTime(movimentacao.getData());
				int mesMov = dataMov.get(Calendar.MONTH);

				if (categoria.getNome().equals(movimentacao.getCategoria().getNome()) && mes == mesMov) {
					soma += movimentacao.getValor();
				}
			}
			categoria.setTotal(categoria.getTotal() + Utils.precision(soma));
			if (categoria.getTotal() > 0) {
			pieModelMensal.set(categoria.getNome() + ": " + Utils.formatarReal(categoria.getTotal()),
						categoria.getTotal());
				categoriasMensal.add(categoria);
				totalMensal += categoria.getTotal();
			}
		}
		if (pieModelMensal.getData().isEmpty()) {
			pieModelMensal.set("Vazio", 0);
		}
	}

	public void carregarMes(Long mes) {
		somarPorCategoriaMensal(Integer.valueOf(mes.toString()));
		porcentagemCategoriaMensal();
		mesSelecionado = getMes(Integer.valueOf(mes.toString()));
	}

	private void criarGraficoLinhasModel() {
		graficoLinhasModel = new CartesianChartModel();

		ChartSeries receitas = new ChartSeries();
		receitas.setLabel("Receita");

		ChartSeries despesas = new ChartSeries();
		despesas.setLabel("Despesa");
		for (int i = 1; i <= getMesAtual(); i++) {
			double somaMovimentacao = somarMovimentacaoMes(i);
			// if (somaMovimentacao > 0) {
				despesas.set(getMes(i), somaMovimentacao);
			// }
		}

		graficoLinhasModel.addSeries(despesas);
	}

	private String getMes(int i) {
		if (i == 1) return "Janeiro";
		if (i == 2) return "Fevereiro";
		if (i == 3) return "Março";
		if (i == 4) return "Abril";
		if (i == 5) return "Maio";
		if (i == 6) return "Junho";
		if (i == 7) return "Julho";
		if (i == 8) return "Agosto";
		if (i == 9) return "Setembro";
		if (i == 10) return "Outubro";
		if (i == 11) return "Novembro";
		if (i == 12) return "Dezembro";
		return BillsConstants.EMPTY;
	}

	private int getMesAtual() {
		Calendar data = Calendar.getInstance();
		data.setTime(new Date());
		int mes = data.get(Calendar.MONTH);
		return mes;
	}

	private double somarMovimentacaoMes(int mes) {
		double totalMes = 0;
		for (Movimentacao movimentacao : movimentacoes) {
			Calendar dataMov = Calendar.getInstance();
			dataMov.setTime(movimentacao.getData());
			int mesMov = dataMov.get(Calendar.MONTH);
			if (mesMov == mes) {
				totalMes += movimentacao.getValor();
			}
		}
		return totalMes;
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

	public double getTotalGeral() {
		return totalGeral;
	}

	public void setTotalGeral(double totalGeral) {
		this.totalGeral = totalGeral;
	}

	public double getTotalMensal() {
		return totalMensal;
	}

	public void setTotalMensal(double totalMensal) {
		this.totalMensal = totalMensal;
	}

	public CartesianChartModel getGraficoLinhasModel() {
		return graficoLinhasModel;
	}

	public void setGraficoLinhasModel(CartesianChartModel graficoLinhasModel) {
		this.graficoLinhasModel = graficoLinhasModel;
	}

	public String getMesSelecionado() {
		return mesSelecionado;
	}

	public void setMesSelecionado(String mesSelecionado) {
		this.mesSelecionado = mesSelecionado;
	}

}
