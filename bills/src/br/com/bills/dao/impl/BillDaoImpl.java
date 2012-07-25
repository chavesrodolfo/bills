package br.com.bills.dao.impl;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.bills.dao.BillDao;
import br.com.bills.model.Bill;
import br.com.bills.model.Usuario;

@Repository("billDao")
@Transactional
public class BillDaoImpl implements BillDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Bill> listaTudo() {
		return entityManager.createQuery("from Bill ", Bill.class).getResultList();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Bill> listaAtivas() {
		return entityManager.createQuery("from Bill where estado = 'ativa'", Bill.class).getResultList();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Bill> listaInativas() {
		return entityManager.createQuery("from Bill where estado = 'inativa'", Bill.class).getResultList();
	}

	@Override
	public void salva(Bill bill) {
		entityManager.persist(bill);
	}

	@Override
	public void atualiza(Bill bill) {
		entityManager.merge(bill);

	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Bill> buscarContas(Usuario usuario) {
		return entityManager.createQuery(
				"from Bill where beneficiario ='" + usuario.getLogin() + "' or devedor='" + usuario.getLogin() + "'"
						+ "and estado='ativa'", Bill.class).getResultList();
	}

	@Override
	public void excluirTodasContas() {
		List<Bill> contas = listaTudo();
		for (Bill bill : contas) {
			entityManager.remove(bill);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Bill> buscarContas(int ano, int mes) {
		Calendar calInicio = new GregorianCalendar(ano, mes, Calendar.getInstance().getMinimum(
				GregorianCalendar.DAY_OF_MONTH));
		Calendar calFim = new GregorianCalendar(ano, mes, Calendar.getInstance().getMaximum(
				GregorianCalendar.DAY_OF_MONTH));
		return entityManager
				.createQuery("from Bill where data between :inicio and :fim and estado='inativa'", Bill.class)
				.setParameter("inicio", calInicio.getTime()).setParameter("fim", calFim.getTime())
				.getResultList();
	}

}
