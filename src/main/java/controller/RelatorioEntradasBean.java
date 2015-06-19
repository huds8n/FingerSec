package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jsf.FacesUtil;
import model.Entrada;
import repository.Entradas;

@Named
@RequestScoped
public class RelatorioEntradasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	Entradas entradas;

	private Date dataInicio;
	private Date dataFim;
	private List<Entrada> listaEntradas;

	public void buscar() {
		listaEntradas = new ArrayList<Entrada>();
		listaEntradas = entradas.porPeriodo(dataInicio, dataFim);
		if (listaEntradas.isEmpty()) {
			FacesUtil.addInfoMessage("Sem Registros");
		}
		
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<Entrada> getListaEntradas() {
		return listaEntradas;
	}

	public void setListaEntradas(List<Entrada> listaEntradas) {
		this.listaEntradas = listaEntradas;
	}

}