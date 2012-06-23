package br.com.bills.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.bills.dao.PerfilDao;
import br.com.bills.model.Perfil;

@Repository("perfilDao")
@Transactional
public class PerfilDaoImpl implements PerfilDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Perfil> lista() {
		return entityManager.createQuery("from Perfil", Perfil.class)
				.getResultList();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
