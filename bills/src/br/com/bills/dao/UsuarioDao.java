package br.com.bills.dao;

import java.util.List;

import br.com.bills.model.Usuario;

public interface UsuarioDao {

	public List<Usuario> listaTudo();

	public void salva(Usuario usuario);

	public void atualiza(Usuario usuario);

	public void remove(Usuario usuario);

	public Usuario buscaPor(String login, String senha);

	public Usuario carrega(Long id);
	
}
