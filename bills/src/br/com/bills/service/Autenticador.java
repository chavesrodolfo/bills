package br.com.bills.service;

import br.com.bills.model.Usuario;

public interface Autenticador {

	public Usuario autentica(String login, String senha);

}
