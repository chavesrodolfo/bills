package br.com.bills.controller;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.com.bills.controller.util.BillsConstants;
import br.com.bills.dao.BillDao;
import br.com.bills.dao.UsuarioDao;
import br.com.bills.model.Bill;
import br.com.bills.model.Usuario;

@ManagedBean
@RequestScoped
public class ChartBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private CartesianChartModel categoryModel;

	private CartesianChartModel pessoalModel;
	private CartesianChartModel geralModel;
	private CartesianChartModel linearModel;

	private PieChartModel pieModel;

	@ManagedProperty("#{billDao}")
	private BillDao billDao;

	@ManagedProperty("#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty("#{usuarioWeb}")
	private UsuarioWeb usuarioWeb;

	private List<Bill> bills = null;

	private Double contasPagar = null;
	private Double contasReceber = null;
	private DecimalFormat twoDForm = new DecimalFormat("0.00");

	public void gerarGraficoBarrasPessoal() {
		pessoalModel = new CartesianChartModel();
		somarContas(usuarioWeb.getUsuario());

		ChartSeries receber = new ChartSeries();
		receber.setLabel("Receber");
		receber.set("Balanço", Double.valueOf(twoDForm.format(contasReceber)));

		ChartSeries pagar = new ChartSeries();
		pagar.setLabel("Pagar");
		pagar.set("Balanço", Double.valueOf(twoDForm.format(contasPagar)));

		pessoalModel.addSeries(receber);
		pessoalModel.addSeries(pagar);
	}

	public void gerarGraficoBarrasGeral() {
		geralModel = new CartesianChartModel();
		List<Usuario> todosUsuarios = usuarioDao.listaTudo();

		ChartSeries receber = new ChartSeries();
		receber.setLabel("Receber");
		ChartSeries pagar = new ChartSeries();
		pagar.setLabel("Pagar");
		for (Usuario usuario : todosUsuarios) {
			if (!usuario.getLogin().equals(BillsConstants.USER_ADMIN)) {
				somarContas(usuario);
				receber.set(
						usuario.getLogin() + " " + situacaoPessoal(usuario),
						Double.valueOf(twoDForm.format(contasReceber)));
				pagar.set(usuario.getLogin() + " " + situacaoPessoal(usuario),
						Double.valueOf(twoDForm.format(contasPagar)));
			}
		}
		geralModel.addSeries(receber);
		geralModel.addSeries(pagar);
	}

	public void gerarGraficoLinhasGeral() {
		geralModel = new CartesianChartModel();
		List<Usuario> todosUsuarios = usuarioDao.listaTudo();

		for (Usuario usuario : todosUsuarios) {
			if (!usuario.getLogin().equals(BillsConstants.USER_ADMIN)) {
				ChartSeries receber = new ChartSeries();
				receber.setLabel(usuario.getLogin());
				ChartSeries pagar = new ChartSeries();
				pagar.setLabel(usuario.getLogin());
				// for para todas as datas
				somarContas(usuario);
				receber.set(usuario.getLogin(),
						Double.valueOf(twoDForm.format(contasReceber)));
				pagar.set(usuario.getLogin(),
						Double.valueOf(twoDForm.format(contasPagar)));
				// fim for

				geralModel.addSeries(receber);
				geralModel.addSeries(pagar);
			}
		}

	}

	private void somarContas(Usuario usuario) {
		bills = billDao.buscarContas(usuario);
		contasReceber = new Double(0);
		contasPagar = new Double(0);
		for (Bill bill : bills) {
			if (bill.getBeneficiario().equals(usuario.getLogin())) {
				contasReceber += bill.getValor();
			} else if (bill.getDevedor().equals(usuario.getLogin())) {
				contasPagar += bill.getValor();
			}
		}
	}

	public String situacaoPessoal(Usuario usuario) {
		somarContas(usuario);
		if (contasPagar > contasReceber) {
			return ("$"
					+ Double.valueOf(twoDForm.format(contasPagar
							- contasReceber)) + " Em Débito").replace('.', ',');
		} else {
			return ("$"
					+ Double.valueOf(twoDForm.format(contasReceber
							- contasPagar)) + " Em Crédito").replace('.', ',');
		}
	}

	public String situacaoPessoal() {
		somarContas(usuarioWeb.getUsuario());
		if (contasPagar > contasReceber) {
			return ("$"
					+ Double.valueOf(twoDForm.format(contasPagar
							- contasReceber)) + " Em Débito").replace('.', ',');
		} else {
			return ("$"
					+ Double.valueOf(twoDForm.format(contasReceber
							- contasPagar)) + " Em Crédito").replace('.', ',');
		}
	}

	public void itemSelect(ItemSelectEvent event) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Item selected", "Item Index: " + event.getItemIndex()
						+ ", Series Index:" + event.getSeriesIndex());

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public CartesianChartModel getCategoryModel() {
		return categoryModel;
	}

	public PieChartModel getPieModel() {
		return pieModel;
	}

	private void createPieModel() {
		pieModel = new PieChartModel();

		pieModel.set("Receber", 120);
		pieModel.set("pagar", 52);
	}

	public CartesianChartModel getPessoalModel() {
		return pessoalModel;
	}

	public void setPessoalModel(CartesianChartModel pessoalModel) {
		this.pessoalModel = pessoalModel;
	}

	public BillDao getBillDao() {
		return billDao;
	}

	public void setBillDao(BillDao billDao) {
		this.billDao = billDao;
	}

	public UsuarioWeb getUsuarioWeb() {
		return usuarioWeb;
	}

	public void setUsuarioWeb(UsuarioWeb usuarioWeb) {
		this.usuarioWeb = usuarioWeb;
	}

	public Double getContasPagar() {
		return contasPagar;
	}

	public void setContasPagar(Double contasPagar) {
		this.contasPagar = contasPagar;
	}

	public Double getContasReceber() {
		return contasReceber;
	}

	public void setContasReceber(Double contasReceber) {
		this.contasReceber = contasReceber;
	}

	public CartesianChartModel getGeralModel() {
		return geralModel;
	}

	public void setGeralModel(CartesianChartModel geralModel) {
		this.geralModel = geralModel;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public CartesianChartModel getLinearModel() {
		return linearModel;
	}

	public void setLinearModel(CartesianChartModel linearModel) {
		this.linearModel = linearModel;
	}

}
