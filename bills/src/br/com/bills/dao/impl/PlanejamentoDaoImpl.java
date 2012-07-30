package br.com.bills.dao.impl;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.bills.dao.PlanejamentoDao;
import br.com.bills.model.Planejamento;

@Repository("planejamentoDao")
@Transactional
public class PlanejamentoDaoImpl implements PlanejamentoDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Planejamento> listarMesAtual() {
		Calendar calInicio = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance()
				.get(Calendar.MONTH), Calendar.getInstance().getMinimum(GregorianCalendar.DAY_OF_MONTH));
		Calendar calFim = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(
				Calendar.MONTH), Calendar.getInstance().getMaximum(GregorianCalendar.DAY_OF_MONTH));

		return entityManager.createQuery("from Planejamento where data between :inicio and :fim", Planejamento.class)
				.setParameter("inicio", calInicio.getTime()).setParameter("fim", calFim.getTime()).getResultList();
	}

	@Override
	public void salva(Planejamento planejamento) {
		entityManager.merge(planejamento);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Planejamento> listarTudo() {
		return entityManager.createQuery("from Planejamento", Planejamento.class).getResultList();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
