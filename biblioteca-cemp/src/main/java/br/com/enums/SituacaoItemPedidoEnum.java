package br.com.enums;

public enum SituacaoItemPedidoEnum {

	PENDENTE(1, "Aguardando preparo"), EM_PREPARO(2, "Em preparo"), PRONTO(3, "Pronto"), ENTREGUE(4, "Entregue");

	private int tipo;
	private String nome;

	private SituacaoItemPedidoEnum(int tipo, String nome) {
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
