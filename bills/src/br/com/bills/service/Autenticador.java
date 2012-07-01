package br.com.bills.service;

import br.com.bills.model.Usuario;

public interface Autenticador {

	public Usuario autentica(String login, String senha);

	public void criarAdmin(String userAdmin, String senhaAdmin, String admin);

}
