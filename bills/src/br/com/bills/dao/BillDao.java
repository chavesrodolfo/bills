package br.com.bills.dao;

import java.util.List;

import br.com.bills.model.Bill;
import br.com.bills.model.Usuario;

public interface BillDao {

	public List<Bill> listaAtivas();

	public void salva(Bill bill);

	public void atualiza(Bill bill);

	public List<Bill> listaInativas();

	public List<Bill> listaTudo();

	public List<Bill> buscarContas(Usuario usuario);

}
