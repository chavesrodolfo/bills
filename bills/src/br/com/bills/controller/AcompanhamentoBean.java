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
	private String mesSelecionadoTexto;

	private Long anoSelecionado;
	private Long mesSelecionado;

	private double totalGeral = 0;
	private double totalMensal = 0;

	public String prepararAcompanhamento() {
		movimentacoes = movimentacaoDao.listarTodas(usuarioWeb.getUsuario());
		categorias = movimentacaoDao.listarCategorias();
		somarPorCategoriaGeral();
		somarPorCategoriaMensal(Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR));
		porcentagemCategoriaMensal();
		criarGraficoLinhasModel();
		verificaMesAno();
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
				pieModel.set(categoria.getNome() + ": " + Utils.formatarReal(categoria.getTotal()),
						categoria.getTotal());
				totalGeral += categoria.getTotal();
			}
		}
		if (pieModel.getData().isEmpty()) {
			pieModel.set("Vazio", 0);
		}
	}

	private void somarPorCategoriaMensal(int mes, int ano) {
		pieModelMensal = new PieChartModel();
		categoriasMensal.removeAll(categoriasMensal);
		for (Categoria categoria : categorias) {
			categoria.setTotal(0);
			double soma = 0;
			for (Movimentacao movimentacao : movimentacoes) {
				Calendar dataMov = Calendar.getInstance();
				dataMov.setTime(movimentacao.getData());
				int mesMov = dataMov.get(Calendar.MONTH);
				int anoMov = dataMov.get(Calendar.YEAR);

				if (categoria.getNome().equals(movimentacao.getCategoria().getNome()) && (mes == mesMov)
						&& (ano == anoMov)) {
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
		mesSelecionado = mes;
		verificaMesAno();
		somarPorCategoriaMensal(Integer.parseInt(mesSelecionado.toString()),
				Integer.parseInt(anoSelecionado.toString()));
		porcentagemCategoriaMensal();
	}

	public void carregarAno(Long ano) {
		anoSelecionado = ano;
		verificaMesAno();
		somarPorCategoriaMensal(Integer.parseInt(mesSelecionado.toString()),
				Integer.parseInt(anoSelecionado.toString()));
		porcentagemCategoriaMensal();
		criarGraficoLinhasModel();
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
		for (int i = 1; i <= getMesAtual(); i++) {
			double somaMovimentacao = somarMovimentacaoMes(i, Integer.parseInt(anoSelecionado.toString()));
			// if (somaMovimentacao > 0) {
			despesas.set(Utils.getMes(i), somaMovimentacao);
			// }
		}

		graficoLinhasModel.addSeries(despesas);
	}

	private int getMesAtual() {
		Calendar data = Calendar.getInstance();
		data.setTime(new Date());
		int mes = data.get(Calendar.MONTH);
		return mes;
	}

	private double somarMovimentacaoMes(int mes, int ano) {
		double totalMes = 0;
		for (Movimentacao movimentacao : movimentacoes) {
			Calendar dataMov = Calendar.getInstance();
			dataMov.setTime(movimentacao.getData());
			int mesMov = dataMov.get(Calendar.MONTH);
			int anoMov = dataMov.get(Calendar.YEAR);
			if ((mesMov == mes) && (anoMov == ano)) {
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

}
