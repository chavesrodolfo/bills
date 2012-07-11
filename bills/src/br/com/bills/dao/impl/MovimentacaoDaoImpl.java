package br.com.bills.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.bills.dao.MovimentacaoDao;
import br.com.bills.model.Categoria;
import br.com.bills.model.Movimentacao;
import br.com.bills.util.Utils;

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Categoria> listarCategorias() {
		return entityManager.createQuery("from Categoria", Categoria.class).getResultList();
	}

	@Override
	public void salvar(Movimentacao movimentacao) {
		List<Categoria> categoria = entityManager.createQuery(
				"from Categoria where nome='" + movimentacao.getCategoria().getNome() + "'", Categoria.class)
				.getResultList();
		if (categoria.isEmpty()) {
			entityManager.persist(movimentacao.getCategoria());
			categoria = entityManager.createQuery(
					"from Categoria where nome='" + movimentacao.getCategoria().getNome() + "'", Categoria.class)
					.getResultList();
			categoria.get(0).setNome(Utils.capitalize(categoria.get(0).getNome()));
			movimentacao.setCategoria(categoria.get(0));
			entityManager.persist(movimentacao);
		} else {
			categoria = entityManager.createQuery(
					"from Categoria where nome='" + movimentacao.getCategoria().getNome() + "'", Categoria.class)
					.getResultList();
			movimentacao.setCategoria(categoria.get(0));
			entityManager.merge(movimentacao);
		}
	}

	@Override
	public void atualizar(Movimentacao movimentacao) {
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
