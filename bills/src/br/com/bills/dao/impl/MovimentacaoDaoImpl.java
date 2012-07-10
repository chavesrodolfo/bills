package br.com.bills.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.bills.dao.MovimentacaoDao;
import br.com.bills.model.Movimentacao;

@Repository("movimentacaoDao")
@Transactional
public class MovimentacaoDaoImpl implements MovimentacaoDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Movimentacao> listarTodas() {
		return entityManager.createQuery("from Movimentacao", Movimentacao.class).getResultList();
	}

	@Override
	public void salvar(Movimentacao movimentacao) {
		entityManager.merge(movimentacao);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Movimentacao carrega(Long id) {
		return entityManager.find(Movimentacao.class, id);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
