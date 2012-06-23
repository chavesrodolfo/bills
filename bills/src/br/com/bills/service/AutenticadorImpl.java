package br.com.bills.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bills.dao.UsuarioDao;
import br.com.bills.model.Usuario;

@Service("autenticador")
public class AutenticadorImpl implements Autenticador {

	private UsuarioDao usuarioDao;
	
	@Autowired
	public AutenticadorImpl(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	@Override
	public Usuario autentica(String login, String senha) {
		Usuario usuario = usuarioDao.buscaPor(login, senha);
		return usuario;
	}

}



