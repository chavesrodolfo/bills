package br.com.bills.converter;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.com.bills.dao.UsuarioDao;
import br.com.bills.model.Usuario;

@FacesConverter(value = "usuarioConverter")
public class UsuarioConverter implements Converter {

	public static List<Usuario> usuarios;

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		if (submittedValue.trim().equals("")) {
			return null;
		} else {
			try {
				UsuarioDao dao = facesContext.getApplication().evaluateExpressionGet(facesContext, "#{usuarioDao}",
						UsuarioDao.class);
				usuarios = dao.listaTudo();
				Long id = Long.valueOf(submittedValue);

				for (Usuario p : usuarios) {
					if (p.getId() == id) {
						return p;
					}
				}

			} catch (NumberFormatException exception) {
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro na conversão.",
						"Usuário inválido"));
			}
		}

		return null;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		if (value == null || value.equals("")) {
			return "";
		} else {
			return String.valueOf(((Usuario) value).getId());
		}
	}

}
