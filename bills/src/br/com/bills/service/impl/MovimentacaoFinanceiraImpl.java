package br.com.bills.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bills.dao.MovimentacaoDao;
import br.com.bills.model.Categoria;
import br.com.bills.model.Movimentacao;
import br.com.bills.model.Usuario;
import br.com.bills.service.MovimentacaoFinanceira;
import br.com.bills.util.Utils;

@Service("movimentacaoFinanceira")
public class MovimentacaoFinanceiraImpl implements MovimentacaoFinanceira {

	private MovimentacaoDao movimentacaoDao;

	private List<Categoria> categoriasMensal = new ArrayList<Categoria>();
	private List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();
	private List<Categoria> categorias = new ArrayList<Categoria>();
	private double totalMensal;
	private double totalGeral;

	@Autowired
	public MovimentacaoFinanceiraImpl(MovimentacaoDao movimentacaoDao) {
		this.movimentacaoDao = movimentacaoDao;
	}

	public List<Categoria> somarPorCategoriaMensal(int mes, int ano, Usuario usuario) {
		movimentacoes = movimentacaoDao.listarTodas(usuario);
		categorias = movimentacaoDao.listarCategorias();
		categoriasMensal.removeAll(categoriasMensal);
		totalMensal = 0;
		for (Categoria categoria : categorias) {
			categoria.setTotal(0);
			double soma = 0;
			for (Movimentacao movimentacao : movimentacoes) {
				Calendar dataMov = Calendar.getInstance();
				dataMov.setTime(movimentacao.getData());
				int mesMov = dataMov.get(Calendar.MONTH);
				int anoMov = dataMov.get(Calendar.YEAR);

				if (categoria.getNome().equals(movimentacao.getCategoria().getNome()) && (mes == mesMov)
						&& (ano == anoMov)) {
					soma += movimentacao.getValor();
				}
			}
			categoria.setTotal(categoria.getTotal() + Utils.precision(soma));
			categoriasMensal.add(categoria);
			totalMensal += categoria.getTotal();
		}
		porcentagemCategoriaMensal();
		return categoriasMensal;
	}

	private void porcentagemCategoriaMensal() {
		for (Categoria categoria : categoriasMensal) {
			if (totalMensal > 0) {
				categoria.setPorcentagem(Utils.precision(categoria.getTotal() / totalMensal * 100));
			}
		}
	}

	public List<Categoria> somarPorCategoriaGeral(Usuario usuario) {
		movimentacoes = movimentacaoDao.listarTodas(usuario);
		categorias = movimentacaoDao.listarCategorias();
		totalGeral = 0;
		for (Categoria categoria : categorias) {
			categoria.setTotal(0);
			double soma = 0;
			for (Movimentacao movimentacao : movimentacoes) {
				if (categoria.getNome().equals(movimentacao.getCategoria().getNome())) {
					soma += movimentacao.getValor();
				}
			}
			categoria.setTotal(categoria.getTotal() + Utils.precision(soma));
			if (categoria.getTotal() > 0) {
				totalGeral += categoria.getTotal();
			}
		}
		return categorias;
	}

	@SuppressWarnings("unused")
	private void porcentagemCategoriaGeral() {
		for (Categoria categoria : categoriasMensal) {
			if (totalGeral > 0) {
				categoria.setPorcentagem(Utils.precision(categoria.getTotal() / totalGeral * 100));
			}
		}
	}

	public double somarMovimentacaoMes(int mes, int ano, Usuario usuario) {
		movimentacoes = movimentacaoDao.listarTodas(usuario);
		double totalMes = 0;
		for (Movimentacao movimentacao : movimentacoes) {
			Calendar dataMov = Calendar.getInstance();
			dataMov.setTime(movimentacao.getData());
			int mesMov = dataMov.get(Calendar.MONTH);
			int anoMov = dataMov.get(Calendar.YEAR);
			if ((mesMov == mes) && (anoMov == ano)) {
				totalMes += movimentacao.getValor();
			}
		}
		return totalMes;
	}
}
