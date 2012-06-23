package br.com.bills.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

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

	private Bill billAntiga;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_BILL", referencedColumnName = "id")
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

	public Bill getBillAntiga() {
		return billAntiga;
	}

	public void setBillAntiga(Bill billAntiga) {
		this.billAntiga = billAntiga;
	}

	public Bill getBillAtual() {
		return billAtual;
	}

	public void setBillAtual(Bill billAtual) {
		this.billAtual = billAtual;
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
