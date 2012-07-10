package br.com.bills.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.primefaces.event.FileUploadEvent;

import br.com.bills.controller.util.BillsConstants;
import br.com.bills.controller.util.FacesUtils;
import br.com.bills.dao.BillDao;
import br.com.bills.model.Bill;
import br.com.bills.service.OperacoesBill;

@ManagedBean
@RequestScoped
public class ImportCSVBean {

	private List<String> billsCSV = new ArrayList<String>();

	@ManagedProperty("#{billDao}")
	private BillDao billDao;

	@ManagedProperty("#{usuarioWeb}")
	private UsuarioWeb usuarioWeb;

	@ManagedProperty("#{facesUtils}")
	private FacesUtils facesUtils;

	@ManagedProperty("#{operacoesBill}")
	private OperacoesBill operacoesBill;
	
	public void importar(FileUploadEvent event) {

		byte fileContent[] = event.getFile().getContents();

		String strFileContent = new String(fileContent);

		String[] registros = strFileContent.split("\n");
		try {
			for (String registro : registros) {
				billsCSV.add(registro);
			}

			for (int i = 1; i < billsCSV.size(); i++) {
				if (billsCSV.get(i) == null)
					break;

				String[] campos = billsCSV.get(i).split(",");

				Bill bill = new Bill();
				bill.setBeneficiario(campos[0]);
				bill.setValor(Double.valueOf(campos[1]));
				bill.setDevedor(campos[2]);
				bill.setMotivo(campos[3]);
				bill.setData(construirData(campos[4]));
				bill.setEstado(BillsConstants.CONTA_ATIVA);
				bill.setUltimaAlteracao(new Date());
				bill.setUsuario(usuarioWeb.getUsuario());
				operacoesBill.gerarInformativoAlteracoes(bill, bill, BillsConstants.OP_INSERT, usuarioWeb.getUsuario());
			}
			facesUtils
					.adicionaMensagemDeInformacao("Dados importados com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			facesUtils.adicionaMensagemDeErro("Arquivo inválido!");
		}

	}

	private Date construirData(String campo) {
		DateFormat formatter;
		Date date = new Date();
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			date = (Date) formatter.parse(campo);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public BillDao getBillDao() {
		return billDao;
	}

	public void setBillDao(BillDao billDao) {
		this.billDao = billDao;
	}

	public List<String> getBillsCSV() {
		return billsCSV;
	}

	public void setBillsCSV(List<String> billsCSV) {
		this.billsCSV = billsCSV;
	}

	public UsuarioWeb getUsuarioWeb() {
		return usuarioWeb;
	}

	public void setUsuarioWeb(UsuarioWeb usuarioWeb) {
		this.usuarioWeb = usuarioWeb;
	}

	public FacesUtils getFacesUtils() {
		return facesUtils;
	}

	public void setFacesUtils(FacesUtils facesUtils) {
		this.facesUtils = facesUtils;
	}

	public OperacoesBill getOperacoesBill() {
		return operacoesBill;
	}

	public void setOperacoesBill(OperacoesBill operacoesBill) {
		this.operacoesBill = operacoesBill;
	}
	
	

}
