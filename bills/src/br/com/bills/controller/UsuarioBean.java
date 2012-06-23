package br.com.bills.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.com.bills.controller.util.BillsConstants;
import br.com.bills.controller.util.FacesUtils;
import br.com.bills.dao.PerfilDao;
import br.com.bills.dao.UsuarioDao;
import br.com.bills.model.Perfil;
import br.com.bills.model.Usuario;

@ManagedBean
@RequestScoped
public class UsuarioBean {

	private Usuario usuario = new Usuario();
	private String confirmacaoDeSenha;
	private List<Usuario> usuarios = new ArrayList<Usuario>();

	private static final String ESTADO_DE_NOVO = "_novo";
	private static final String ESTADO_DE_EDICAO = "_edicao";
	private static final String ESTADO_DE_PESQUISA = "_pesquisa";

	private String state = ESTADO_DE_PESQUISA;

	@ManagedProperty("#{usuarioDao}")
	private UsuarioDao usuarioDao;
	@ManagedProperty("#{perfilDao}")
	private PerfilDao perfilDao;
	@ManagedProperty("#{facesUtils}")
	private FacesUtils facesUtils;

	public void lista() {
		usuarios = usuarioDao.listaTudo();
		setState(ESTADO_DE_PESQUISA);
	}

	public void preparaParaAdicionar() {
		this.usuario = new Usuario();
		lista();
		setState(ESTADO_DE_NOVO);
	}

	public void adiciona() {

		boolean senhaInvalida = !confirmacaoDeSenha.equals(usuario.getSenha());
		if (senhaInvalida) {
			facesUtils
					.adicionaMensagemDeErro("Senha e confirmação de senha não conferem.");
			return;
		}

		List<Perfil> perfis = perfilDao.lista();
		if (perfis == null) {
			Perfil perfilAdministrador = new Perfil();
			perfilAdministrador.setPerfil(BillsConstants.ADMIN);
			usuario.setPerfil(perfilAdministrador);
		} else if (perfis.size() == 1) {
			Perfil perfilUsuario = new Perfil();
			perfilUsuario.setPerfil(BillsConstants.USER);
			usuario.setPerfil(perfilUsuario);
		} else {
			usuario.setPerfil(perfis.get(1));
		}

		usuarioDao.salva(usuario);
		facesUtils
				.adicionaMensagemDeInformacao("Usuário adicionado com sucesso!");
		preparaParaAdicionar();
	}

	public void remove() {
		usuarioDao.remove(usuario);
		facesUtils
				.adicionaMensagemDeInformacao("Usuário removido com sucesso!");
		preparaParaAdicionar();
	}

	public void preparaParaAlterar(Usuario usuario) {
		this.usuario = usuarioDao.carrega(usuario.getId()); // evita
															// LazyInitializationException
		setState(ESTADO_DE_EDICAO);
	}

	public void altera() {

		boolean senhaInvalida = !confirmacaoDeSenha.equals(usuario.getSenha());
		if (senhaInvalida) {
			facesUtils
					.adicionaMensagemDeErro("Senha e confirmação de senha não conferem.");
			return;
		}

		usuarioDao.atualiza(usuario);
		facesUtils
				.adicionaMensagemDeInformacao("Usuário atualizado com sucesso!");
		lista();
	}

	public boolean isAdicionando() {
		return ESTADO_DE_NOVO.equals(state);
	}

	public boolean isEditando() {
		return ESTADO_DE_EDICAO.equals(state);
	}

	public boolean isPesquisando() {
		return ESTADO_DE_PESQUISA.equals(state);
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getConfirmacaoDeSenha() {
		return confirmacaoDeSenha;
	}

	public void setConfirmacaoDeSenha(String confirmacaoDeSenha) {
		this.confirmacaoDeSenha = confirmacaoDeSenha;
	}

	public void setFacesUtils(FacesUtils facesUtils) {
		this.facesUtils = facesUtils;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public PerfilDao getPerfilDao() {
		return perfilDao;
	}

	public void setPerfilDao(PerfilDao perfilDao) {
		this.perfilDao = perfilDao;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public FacesUtils getFacesUtils() {
		return facesUtils;
	}

}
