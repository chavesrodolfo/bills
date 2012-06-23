package br.com.bills.controller;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.bills.controller.util.BillsConstants;
import br.com.bills.model.Usuario;

@Controller
@Scope("session")
public class UsuarioWeb implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	private boolean administrador = false;
	
	public void loga(Usuario usuario) {
		this.usuario = usuario;
	}

	public void logout() {
		this.usuario = null;
	}
	
	public boolean isLogado() {
		return this.usuario != null;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	public boolean isAdministrador() {
		if (usuario.getPerfil().getPerfil().endsWith(BillsConstants.ADMIN)) {
			administrador = true;
		} else {
			administrador = false;
		}
		return administrador;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setAdministrador(boolean administrador) {
		this.administrador = administrador;
	}

}
