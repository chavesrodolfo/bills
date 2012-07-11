package br.com.bills.dao;

import java.util.List;

import br.com.bills.model.Categoria;
import br.com.bills.model.Movimentacao;

public interface MovimentacaoDao {

	public List<Movimentacao> listarTodas();

	public void salvar(Movimentacao movimentacao);

	public void atualizar(Movimentacao movimentacao);

	public Movimentacao carrega(Long id);

	public List<Categoria> listarCategorias();
}
