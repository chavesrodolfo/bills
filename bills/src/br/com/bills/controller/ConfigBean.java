package br.com.bills.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.com.bills.controller.util.FacesUtils;

@ManagedBean
@RequestScoped
public class ConfigBean {
	@ManagedProperty("#{usuarioBean}")
	private UsuarioBean usuarioBean;
	@ManagedProperty("#{facesUtils}")
	private FacesUtils facesUtils;

	public String preparaConfiguracoes() {
		usuarioBean.preparaParaAdicionar();
		return "/pages/configuracoes.xhtml";
	}

	public FacesUtils getFacesUtils() {
		return facesUtils;
	}

	public void setFacesUtils(FacesUtils facesUtils) {
		this.facesUtils = facesUtils;
	}

	public UsuarioBean getUsuarioBean() {
		return usuarioBean;
	}

	public void setUsuarioBean(UsuarioBean usuarioBean) {
		this.usuarioBean = usuarioBean;
	}

}
