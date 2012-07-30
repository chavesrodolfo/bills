package br.com.bills.dao;

import java.util.List;

import br.com.bills.model.Planejamento;

public interface PlanejamentoDao {

	public List<Planejamento> listarMesAtual();

	public void salva(Planejamento planejamento);

	public List<Planejamento> listarTudo();

}
