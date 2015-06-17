package model;

public enum StatusEntrada {

	ATIVO("Ativo"), 
	INATIVO("Inativo"), 
	CANCELADO("Cancelado");
	
	private String descricao;
	
	StatusEntrada(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}
