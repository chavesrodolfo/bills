package br.com.bills.service;

import java.util.List;

import br.com.bills.model.Categoria;
import br.com.bills.model.Usuario;

public interface MovimentacaoFinanceira {

	public List<Categoria> somarPorCategoriaMensal(int mes, int ano, Usuario usuario);

	public List<Categoria> somarPorCategoriaGeral(Usuario usuario);

	public double somarMovimentacaoMes(int mes, int ano, Usuario usuario);

}
