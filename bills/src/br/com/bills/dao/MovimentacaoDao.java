package br.com.bills.dao;

import java.util.List;

import br.com.bills.model.Movimentacao;

public interface MovimentacaoDao {

	public List<Movimentacao> listarTodas();

	public void salvar(Movimentacao movimentacao);

	public Movimentacao carrega(Long id);
}
