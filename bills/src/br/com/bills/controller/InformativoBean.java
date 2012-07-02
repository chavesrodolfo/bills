package br.com.bills.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.com.bills.dao.InformativoDao;
import br.com.bills.dao.UsuarioDao;
import br.com.bills.model.HistoricoAlteracao;
import br.com.bills.model.Usuario;

@ManagedBean
@RequestScoped
public class InformativoBean {

	@ManagedProperty("#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty("#{informativoDao}")
	private InformativoDao informativoDao;

	@ManagedProperty("#{usuarioWeb}")
	private UsuarioWeb usuarioWeb;

	private List<HistoricoAlteracao> informativo = new ArrayList<HistoricoAlteracao>();

	public String preparaInformativo() {
		carregarInformativo(usuarioWeb.getUsuario());
		return "/pages/home.xhtml";
	}

	public String apagaInformativo() {
		usuarioWeb.getUsuario().setUltimoLogin(new Date());
		usuarioDao.atualiza(usuarioWeb.getUsuario());
		return "/pages/home.xhtml";
	}

	private void carregarInformativo(Usuario usuario) {
		informativo = informativoDao.carregarInformativoAlteracoes(usuario);
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public InformativoDao getInformativoDao() {
		return informativoDao;
	}

	public void setInformativoDao(InformativoDao informativoDao) {
		this.informativoDao = informativoDao;
	}

	public UsuarioWeb getUsuarioWeb() {
		return usuarioWeb;
	}

	public void setUsuarioWeb(UsuarioWeb usuarioWeb) {
		this.usuarioWeb = usuarioWeb;
	}

	public List<HistoricoAlteracao> getInformativo() {
		return informativo;
	}

	public void setInformativo(List<HistoricoAlteracao> informativo) {
		this.informativo = informativo;
	}

}
