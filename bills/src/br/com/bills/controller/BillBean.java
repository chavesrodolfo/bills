package br.com.bills.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.com.bills.controller.util.BillsConstants;
import br.com.bills.controller.util.FacesUtils;
import br.com.bills.dao.BillDao;
import br.com.bills.model.Bill;
import br.com.bills.service.OperacoesBill;

@ManagedBean
@RequestScoped
public class BillBean {

	private Bill bill = new Bill();
	private List<Bill> bills = new ArrayList<Bill>();
	private List<Bill> todasBills = null;
	private List<Bill> billsAtivas = null;
	private List<Bill> billsBalanceadasPorPessoa = null;

	private static final String ESTADO_DE_NOVO = "_novo";
	private static final String ESTADO_DE_EDICAO = "_edicao";
	private static final String ESTADO_DE_PESQUISA = "_pesquisa";

	private String state = ESTADO_DE_PESQUISA;

	private Bill[] selectedBills;

	@ManagedProperty("#{facesUtils}")
	private FacesUtils facesUtils;

	@ManagedProperty("#{billDao}")
	private BillDao billDao;

	@ManagedProperty("#{usuarioWeb}")
	private UsuarioWeb usuarioWeb;

	@ManagedProperty("#{chartBean}")
	private ChartBean chartBean;

	@ManagedProperty("#{operacoesBill}")
	private OperacoesBill operacoesBill;

	public String prepararBills() {
		listaAtivas();
		chartBean.gerarGraficoBarrasPessoal();
		return "/pages/bills.xhtml";
	}

	public void preparaParaAdicionar() {
		this.bill = new Bill();
		this.bill.setEstado(BillsConstants.CONTA_ATIVA);
		setState(ESTADO_DE_NOVO);
	}

	public String prepararHistorico() {
		bills = billDao.listaInativas();
		return "/pages/historico.xhtml";
	}

	public String prepararRelatorio() {
		chartBean.gerarGraficoBarrasPessoal();
		chartBean.gerarGraficoBarrasGeral();
		return "/pages/relatorio.xhtml";
	}

	public void listaAtivas() {
		bills = billDao.listaAtivas();
		billsBalanceadasPorPessoa = balancoPorPessoa(bills);
		setState(ESTADO_DE_PESQUISA);
	}

	public void adiciona() {
		operacoesBill.gerarInformativoAlteracoes(bill, bill, BillsConstants.OP_INSERT, usuarioWeb.getUsuario());
		billDao.salva(bill);
	}

	public void addLinhas() {
		for (int i = 0; i < 5; i++) {
			preparaParaAdicionar();
			adiciona();
		}
		listaAtivas();
		facesUtils.adicionaMensagemDeInformacao("Registros adicionados com sucesso!");
	}

	public void atualizarBills(Bill billAlterada) {
		List<Bill> listaOriginal = listaBillsAtivasParaAtualizacao();
		for (Bill billOriginal : listaOriginal) {
			if (billOriginal.getId() == billAlterada.getId()) {
				if ((!billOriginal.equalsValue(billAlterada))) {
					billAlterada.setUsuario(usuarioWeb.getUsuario());
					billAlterada.setUltimaAlteracao(new Date());
					operacoesBill.gerarInformativoAlteracoes(billOriginal, billAlterada, BillsConstants.OP_UPDATE,
							usuarioWeb.getUsuario());
				}
			}
		}
	}

	private List<Bill> listaBillsAtivasParaAtualizacao() {
		if (billsAtivas == null) {
			billsAtivas = billDao.listaAtivas();
		}
		return billsAtivas;
	}

	public void enviarContasHistorico() {
		String msg = "";
		boolean erro = false;

		if (selectedBills != null && selectedBills.length > 0) {
			for (Bill conta : selectedBills) {
				if (contaValidaPreenchida(conta)) {
					operacoesBill.gerarInformativoAlteracoes(conta, conta, BillsConstants.OP_REMOVE,
							usuarioWeb.getUsuario());
					conta.setEstado(BillsConstants.CONTA_INATIVA);
					billDao.atualiza(conta);
				} else {
					msg = "Alguma conta selecionada não pode ser removida.";
					erro = true;
				}
			}
			listaAtivas();
			if (!erro) {
				msg = "Remoção realizada com sucesso!";
			}
		} else {
			msg = "Não foi selecionado nenhum registro.";
			erro = true;
		}
		if (erro) {
			facesUtils.adicionaMensagemDeErro(msg);
		} else {
			facesUtils.adicionaMensagemDeInformacao(msg);
		}
	}

	private boolean contaValidaPreenchida(Bill conta) {
		if ((conta.getMotivo() != null && !conta.getMotivo().equals(BillsConstants.EMPTY))
				&& (conta.getBeneficiario() != null && !conta.getBeneficiario().equals(BillsConstants.EMPTY))
				&& (conta.getDevedor() != null && !conta.getDevedor().equals(BillsConstants.EMPTY))
				&& (conta.getValor() != null && !conta.getValor().equals(BillsConstants.EMPTY))) {
			return true;
		} else
			return false;
	}

	public List<String> completeBeneficiario(String query) {
		Set<String> results = new HashSet<String>();
		todasBills = getTodasBillsSing();

		for (Bill b : todasBills) {
			if (b != null && b.getBeneficiario() != null) {
				if (b.getBeneficiario().toLowerCase().startsWith(query.toLowerCase()))
					results.add(b.getBeneficiario());
			}
		}
		List<String> resultsList = new ArrayList<String>(results);
		return resultsList;
	}

	public List<String> completeDevedor(String query) {
		Set<String> results = new HashSet<String>();
		todasBills = getTodasBillsSing();

		for (Bill b : todasBills) {
			if (b != null && b.getDevedor() != null) {
				if (b.getDevedor().toLowerCase().startsWith(query.toLowerCase()))
					results.add(b.getDevedor());
			}
		}

		List<String> resultsList = new ArrayList<String>(results);
		return resultsList;
	}

	public List<String> completeMotivo(String query) {
		Set<String> results = new HashSet<String>();
		todasBills = getTodasBillsSing();

		for (Bill b : todasBills) {
			if (b != null && b.getMotivo() != null) {
				if (b.getMotivo().toLowerCase().startsWith(query.toLowerCase()))
					results.add(b.getMotivo());
			}
		}

		List<String> resultsList = new ArrayList<String>(results);
		return resultsList;
	}

	private List<Bill> getTodasBillsSing() {
		if (todasBills == null) {
			todasBills = billDao.listaTudo();
		}
		return todasBills;
	}

	public List<Bill> balancoPorPessoa(List<Bill> contas) {
		List<Bill> contasBalanceadas = new ArrayList<Bill>();
		List<Bill> toReturn = new ArrayList<Bill>();

		for (Bill conta : contas) {
			if (contaValidaPreenchida(conta)) {
				Bill contaBalanceada = new Bill();
				contaBalanceada.setBeneficiario(conta.getBeneficiario());
				contaBalanceada.setDevedor(conta.getDevedor());
				contaBalanceada.setEstado(conta.getEstado());
				contaBalanceada.setMotivo(conta.getMotivo());
				contaBalanceada.setValor(conta.getValor());
				contasBalanceadas.add(contaBalanceada);
			}
		}

		for (Bill conta1 : contasBalanceadas) {
			for (Bill conta2 : contasBalanceadas) {
				if (contaValidaPreenchida(conta1) && contaValidaPreenchida(conta2)) {
					/* caso o devedor de uma conta é o beneficiario da outra */
					if (conta1.getDevedor().toLowerCase().equals(conta2.getBeneficiario().toLowerCase())
							&& conta2.getDevedor().toLowerCase().equals(conta1.getBeneficiario().toLowerCase())) {
						if (conta1.getValor() > conta2.getValor()) {
							if (conta2.getEstado().equals(BillsConstants.CONTA_ATIVA)
									&& conta1.getEstado().equals(BillsConstants.CONTA_ATIVA)) {
								conta1.setValor(conta1.getValor() - conta2.getValor());
								conta1.setMotivo(conta1.getMotivo() + " (-)" + conta2.getMotivo());
								conta2.setEstado(BillsConstants.CONTA_INATIVA);
							}
						} else if (conta2.getDevedor().toLowerCase().equals(conta1.getBeneficiario().toLowerCase())
								&& conta1.getDevedor().toLowerCase().equals(conta2.getBeneficiario().toLowerCase())) {
							if (conta1.getEstado().equals(BillsConstants.CONTA_ATIVA)
									&& conta2.getEstado().equals(BillsConstants.CONTA_ATIVA)) {
								conta2.setValor(conta2.getValor() - conta1.getValor());
								conta2.setMotivo(conta2.getMotivo() + " (-)" + conta1.getMotivo());
								conta1.setEstado(BillsConstants.CONTA_INATIVA);
							}
						} else {
							conta1.setEstado(BillsConstants.CONTA_INATIVA);
							conta2.setEstado(BillsConstants.CONTA_INATIVA);
						}
					} else
					/* caso nao seja a mesma conta */
					if (!conta1.equalsValue(conta2)) {
						/*
						 * caso existam contas que tem o mesmo devedor e
						 * beneficiario. e não é a mesma conta
						 */
						if (conta2.getBeneficiario().toLowerCase().equals(conta1.getBeneficiario().toLowerCase())
								&& conta2.getDevedor().toLowerCase().equals(conta1.getDevedor().toLowerCase())) {
							if (conta2.getEstado().equals(BillsConstants.CONTA_ATIVA)
									&& conta1.getEstado().equals(BillsConstants.CONTA_ATIVA)) {
								conta1.setValor(conta1.getValor() + conta2.getValor());
								conta1.setMotivo(conta1.getMotivo() + " (+)" + conta2.getMotivo());
								conta2.setEstado(BillsConstants.CONTA_INATIVA);
							}
						}
					}
				}
			}
		}

		for (Bill conta : contasBalanceadas) {
			if (conta.getEstado().equals(BillsConstants.CONTA_ATIVA)) {
				DecimalFormat twoDForm = new DecimalFormat("0.00");
				conta.setValor(Double.valueOf(twoDForm.format(conta.getValor())));
				toReturn.add(conta);
			}
		}

		return toReturn;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	public List<Bill> getBills() {
		return bills;
	}

	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public FacesUtils getFacesUtils() {
		return facesUtils;
	}

	public void setFacesUtils(FacesUtils facesUtils) {
		this.facesUtils = facesUtils;
	}

	public BillDao getBillDao() {
		return billDao;
	}

	public void setBillDao(BillDao billDao) {
		this.billDao = billDao;
	}

	public static String getEstadoDeNovo() {
		return ESTADO_DE_NOVO;
	}

	public static String getEstadoDeEdicao() {
		return ESTADO_DE_EDICAO;
	}

	public static String getEstadoDePesquisa() {
		return ESTADO_DE_PESQUISA;
	}

	public Bill[] getSelectedBills() {
		return selectedBills;
	}

	public void setSelectedBills(Bill[] selectedBills) {
		this.selectedBills = selectedBills;
	}

	public List<Bill> getBillsAtivas() {
		return billsAtivas;
	}

	public void setBillsAtivas(List<Bill> billsAtivas) {
		this.billsAtivas = billsAtivas;
	}

	public UsuarioWeb getUsuarioWeb() {
		return usuarioWeb;
	}

	public void setUsuarioWeb(UsuarioWeb usuarioWeb) {
		this.usuarioWeb = usuarioWeb;
	}

	public void setTodasBills(List<Bill> todasBills) {
		this.todasBills = todasBills;
	}

	public List<Bill> getTodasBills() {
		return todasBills;
	}

	public List<Bill> getBillsBalanceadasPorPessoa() {
		return billsBalanceadasPorPessoa;
	}

	public void setBillsBalanceadasPorPessoa(List<Bill> billsBalanceadasPorPessoa) {
		this.billsBalanceadasPorPessoa = billsBalanceadasPorPessoa;
	}

	public ChartBean getChartBean() {
		return chartBean;
	}

	public void setChartBean(ChartBean chartBean) {
		this.chartBean = chartBean;
	}

	public OperacoesBill getOperacoesBill() {
		return operacoesBill;
	}

	public void setOperacoesBill(OperacoesBill operacoesBill) {
		this.operacoesBill = operacoesBill;
	}

}
