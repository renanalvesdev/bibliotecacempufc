package br.com.enums;

public enum PermissaoEnum {

	PADRAO(1, "Padrão"), ADMINISTRADOR(2, "Administrador");

	private int tipo;
	private String nome;

	private PermissaoEnum(int tipo, String nome) {
		this.tipo = tipo;
		this.nome = nome;
	}

	public int getTipo() {
		return tipo;
	}

	public String getNome() {
		return nome;
	}

}
