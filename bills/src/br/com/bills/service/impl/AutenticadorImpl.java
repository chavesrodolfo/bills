package br.com.bills.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bills.dao.UsuarioDao;
import br.com.bills.model.Perfil;
import br.com.bills.model.Usuario;
import br.com.bills.service.Autenticador;

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

	@Override
	public void criarAdmin(String userAdmin, String senhaAdmin, String roleAdmin) {
		Perfil perfil = new Perfil();
		perfil.setPerfil(roleAdmin);
		Usuario usuario = new Usuario();
		usuario.setLogin(userAdmin);
		usuario.setPerfil(perfil);
		usuario.setSenha(senhaAdmin);
		usuarioDao.salva(usuario);
	}

}



