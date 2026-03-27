package structures;

import model.Artista;

public class HashArtistas {
	private No<Artista>[] tabela;
	private int capacidade;

	public HashArtistas(int capacidade) {
		this.capacidade = capacidade;
		this.tabela = (No<Artista>[]) new No[capacidade];
	}

	public void insert(Artista novoArtista) {
		int indice = calcularIndice(novoArtista.getNome());

		No<Artista> novoNo = new No<>(novoArtista);

		if (tabela[indice] == null) {
			tabela[indice] = novoNo;
		} else {
			No<Artista> atual = tabela[indice];
			while (atual != null) {
				if (atual.getDado().getNome().equalsIgnoreCase(novoArtista.getNome()))
					return;

				if (atual.getProximo() == null)
					break;
				atual = atual.getProximo();
			}
			atual.setProximo(novoNo);
		}
	}

	public void exibirCatalogo() {
		System.out.println("\n====== Artistas Cadastrados ======");
		for (int i = 0; i < capacidade; i++) {
			No<Artista> atual = tabela[i];

			while (atual != null) {
				System.out.println("=> " + atual.getDado().getNome());
				atual = atual.getProximo();
			}
		}
	}

	public Artista search(String nome) {
		int indice = calcularIndice(nome);
		No<Artista> atual = tabela[indice];

		while (atual != null) {
			if (atual.getDado().getNome().equalsIgnoreCase(nome)) {
				return atual.getDado();
			}
			atual = atual.getProximo();
		}
		return null;
	}

	private int calcularIndice(String nome) {
		return Math.abs(nome.toLowerCase().hashCode()) % this.capacidade;
	}

}
