package br.com.enums;

public enum TipoEmprestimo {

	EMPRESTIMO(1, "Empréstimo"), RENOVACAO(2, "Renovacao"), DEVOLUCAO(3, "Devolução"), ;

	private int tipo;
	private String nome;

	private TipoEmprestimo(int tipo, String nome) {
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
