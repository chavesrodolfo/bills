package br.com.bills.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.com.bills.controller.util.BillsConstants;
import br.com.bills.controller.util.FacesUtils;
import br.com.bills.dao.UsuarioDao;
import br.com.bills.model.Usuario;
import br.com.bills.service.Autenticador;

@ManagedBean(name = "loginBean")
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
	@ManagedProperty("#{usuarioDao}")
	private UsuarioDao usuarioDao;

	public String logar() {

		Usuario usuario = autenticador.autentica(login, senha);
		if (usuario == null) {
			List<Usuario> usuarios = usuarioDao.listaTudo();
			if (usuarios.isEmpty()) {
				autenticador.criarAdmin(login, senha, BillsConstants.ADMIN);
				usuario = autenticador.autentica(login, senha);
			}
		} else {
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

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

}
