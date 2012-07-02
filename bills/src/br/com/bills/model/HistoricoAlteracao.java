package br.com.bills.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

@Entity
public class HistoricoAlteracao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_HISTORICO_ALTERACAO")
	@SequenceGenerator(name = "SEQ_HISTORICO_ALTERACAO", sequenceName = "SEQ_HISTORICO_ALTERACAO", allocationSize = 1)
	private int id;

	@OneToOne
	@JoinColumn(name = "ID_USUARIO", referencedColumnName = "id")
	private Usuario usuario;

	private String operacao;
	private String beneficiario;
	private Double valor;
	private String devedor;
	private String motivo;
	private Date data;
	private String estado;
	private Date ultimaAlteracao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_BILL_ATUAL", referencedColumnName = "id")
	@Transient
	private Bill billAtual;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getDevedor() {
		return devedor;
	}

	public void setDevedor(String devedor) {
		this.devedor = devedor;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getUltimaAlteracao() {
		return ultimaAlteracao;
	}

	public void setUltimaAlteracao(Date ultimaAlteracao) {
		this.ultimaAlteracao = ultimaAlteracao;
	}

	public Bill getBillAtual() {
		return billAtual;
	}

	public void setBillAtual(Bill billAtual) {
		this.billAtual = billAtual;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoricoAlteracao other = (HistoricoAlteracao) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
