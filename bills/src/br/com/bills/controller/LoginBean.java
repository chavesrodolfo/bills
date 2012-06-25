package br.com.bills.controller;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.com.bills.controller.util.FacesUtils;
import br.com.bills.model.Usuario;
import br.com.bills.service.Autenticador;

@ManagedBean(name="loginBean")
@RequestScoped
public class LoginBean {

	private String login;
	private String senha;
	
	@ManagedProperty("#{autenticador}")
	private Autenticador autenticador;
	@ManagedProperty("#{usuarioWeb}")
	private UsuarioWeb usuarioWeb;
	@ManagedProperty("#{facesUtils}")
	private FacesUtils facesUtils;
	
	public String logar() {
		
		Usuario usuario = autenticador.autentica(login, senha);
		if (usuario != null) {
			usuarioWeb.loga(usuario);
			return "/pages/home.xhtml";
		}

		facesUtils.adicionaMensagemDeErro("Login ou senha inválida.");
		return null;
	}
	
	public String sair() {
		usuarioWeb.logout();
		return "login";
	}

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public void setAutenticador(Autenticador autenticador) {
		this.autenticador = autenticador;
	}
	public void setUsuarioWeb(UsuarioWeb usuarioWeb) {
		this.usuarioWeb = usuarioWeb;
	}
	public void setFacesUtils(FacesUtils facesUtils) {
		this.facesUtils = facesUtils;
	}

}
