package vo;

import java.io.Serializable;
import java.util.Date;

public class DataValor implements Serializable {



	private static final long serialVersionUID = 7003393705719622243L;
	private Date data;
	private Integer valor;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

}