package br.com.bills.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.bills.dao.InformativoDao;
import br.com.bills.model.HistoricoAlteracao;
import br.com.bills.model.Usuario;

@Repository("informativoDao")
@Transactional
public class InformativoDaoImpl implements InformativoDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void registrarInformativoAlteracoes(HistoricoAlteracao informativo) {
		entityManager.persist(informativo);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<HistoricoAlteracao> carregarInformativoAlteracoes(Usuario usuario) {
		System.out.println(usuario.getUltimoLogin());
		List<HistoricoAlteracao> info = entityManager
				.createQuery("from HistoricoAlteracao as h where h.ultimaAlteracao >= :ultimoLogin", HistoricoAlteracao.class)
				.setParameter("ultimoLogin", usuario.getUltimoLogin())
				.getResultList();
		return info;
		
	}

}
