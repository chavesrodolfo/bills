package br.com.bills.service;

import br.com.bills.model.Bill;
import br.com.bills.model.Usuario;

public interface OperacoesBill {

	public void gerarInformativoAlteracoes(Bill billAntiga, Bill billAtual,
			String operacao, Usuario usuario);

	public void excluirTodasContas();
		
}
