package br.com.bills.dao;

import java.util.List;

import br.com.bills.model.Categoria;
import br.com.bills.model.Movimentacao;
import br.com.bills.model.Usuario;

public interface MovimentacaoDao {

	public List<Movimentacao> listarTodas(Usuario usuario);

	public void salvar(Movimentacao movimentacao);

	public void atualizar(Movimentacao movimentacao);

	public Movimentacao carrega(Long id);

	public List<Categoria> listarCategorias();
}
