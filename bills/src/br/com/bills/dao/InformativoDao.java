package br.com.bills.dao;

import java.util.List;

import br.com.bills.model.HistoricoAlteracao;
import br.com.bills.model.Usuario;

public interface InformativoDao {

	public void registrarInformativoAlteracoes(HistoricoAlteracao informativo);

	public List<HistoricoAlteracao> carregarInformativoAlteracoes(
			Usuario usuario);

	public void limparHistorico();

}
