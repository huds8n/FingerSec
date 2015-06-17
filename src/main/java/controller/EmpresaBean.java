package controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jsf.FacesUtil;
import service.EmpresaService;
import service.NegocioException;
import model.Empresa;
import model.TipoPessoa;

@Named
@ViewScoped
public class EmpresaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Empresa empresa;
	private String tipoPessoa;
	private String mask;
	@Inject
	private EmpresaService empresaService;

	public EmpresaBean() {
		
	}

	public void inicializar() {
		empresa = new Empresa();
	}

	public void salvar() {
		try {
			this.empresa.setTipoPessoa(TipoPessoa.JURIDICA);
			this.empresa = empresaService.salvar(this.empresa);
			empresa = new Empresa();
			FacesUtil.addInfoMessage("Empresa salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public void pegarMascara() {
		if (tipoPessoa.equals("FISICA")) {
			mask = "999.999.999-99";
		} else {
			mask = "99.999.999/9999-99";
		}
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public boolean isEditando() {
		return this.empresa.getId() != null;
	}

	public TipoPessoa[] getTipos() {
		return TipoPessoa.values();
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

}