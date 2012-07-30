package br.com.bills.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bills.dao.BillDao;
import br.com.bills.dao.InformativoDao;
import br.com.bills.model.Bill;
import br.com.bills.model.HistoricoAlteracao;
import br.com.bills.model.Usuario;
import br.com.bills.service.OperacoesBill;

@Service("operacoesBill")
public class OperacoesBillImpl implements OperacoesBill {

	private InformativoDao informativoDao;
	private BillDao billDao;

	@Autowired
	public OperacoesBillImpl(InformativoDao informativoDao, BillDao billDao) {
		this.informativoDao = informativoDao;
		this.billDao = billDao;
	}

	@Override
	public void gerarInformativoAlteracoes(Bill billAntiga, Bill billAtual,
			String operacao, Usuario usuario) {
		HistoricoAlteracao informativo = new HistoricoAlteracao();
		informativo.setOperacao(operacao);
		informativo.setBeneficiario(billAntiga.getBeneficiario());
		informativo.setData(billAntiga.getData());
		informativo.setDevedor(billAntiga.getDevedor());
		informativo.setEstado(billAntiga.getEstado());
		informativo.setMotivo(billAntiga.getMotivo());
		informativo.setUltimaAlteracao(new Date());
		informativo.setValor(billAntiga.getValor());
		informativo.setBillAtual(billAtual);
		informativo.setUsuario(usuario);
		informativoDao.registrarInformativoAlteracoes(informativo);
	}

	@Override
	public void excluirTodasContas() {
		informativoDao.limparHistorico();
		billDao.excluirTodasContas();
	}

}
