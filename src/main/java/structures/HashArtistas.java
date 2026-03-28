package structures;

import java.util.ArrayList;
import java.util.List;
import model.Artista;
import model.Musica;

public class HashArtistas {
	private No<Artista>[] tabela;
	private int capacidade;

	// Estrutura interna para agrupar músicas por artista
	private static class EntradaMusica {
		String nomeArtista;
		FiladeReproducao musicas = new FiladeReproducao();
		EntradaMusica proximo;

		EntradaMusica(String nome) {
			this.nomeArtista = nome;
		}
	}

	private EntradaMusica[] tabelaMusicas;

	public HashArtistas(int capacidade) {
		this.capacidade = capacidade;
		this.tabela = (No<Artista>[]) new No[capacidade];
		this.tabelaMusicas = new EntradaMusica[capacidade];
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

	/**
	 * Adiciona uma música à tabela, agrupada pelo nome do artista.
	 */
	public void inserir(Musica musica) {
		int indice = calcularIndice(musica.getArtista());
		EntradaMusica atual = tabelaMusicas[indice];
		while (atual != null) {
			if (atual.nomeArtista.equalsIgnoreCase(musica.getArtista())) {
				atual.musicas.enqueue(musica);
				return;
			}
			atual = atual.proximo;
		}
		EntradaMusica nova = new EntradaMusica(musica.getArtista());
		nova.musicas.enqueue(musica);
		nova.proximo = tabelaMusicas[indice];
		tabelaMusicas[indice] = nova;
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

	/**
	 * Retorna as músicas de um artista agrupadas na hash.
	 */
	public FiladeReproducao buscarMusicas(String nomeArtista) {
		int indice = calcularIndice(nomeArtista);
		EntradaMusica atual = tabelaMusicas[indice];
		while (atual != null) {
			if (atual.nomeArtista.equalsIgnoreCase(nomeArtista)) {
				return atual.musicas;
			}
			atual = atual.proximo;
		}
		return null;
	}

	/**
	 * Busca músicas cujo nome do artista contém o termo informado (parcial,
	 * case-insensitive).
	 * Percorre toda a tabela hash — demonstra varredura da estrutura.
	 */
	public List<Musica> buscarMusicasPorNomeParcial(String parte) {
		List<Musica> resultado = new ArrayList<>();
		String parteLower = parte.toLowerCase();
		for (int i = 0; i < capacidade; i++) {
			EntradaMusica atual = tabelaMusicas[i];
			while (atual != null) {
				if (atual.nomeArtista.toLowerCase().contains(parteLower)) {
					resultado.addAll(atual.musicas.toList());
				}
				atual = atual.proximo;
			}
		}
		return resultado;
	}

	private int calcularIndice(String nome) {
		return Math.abs(nome.toLowerCase().hashCode()) % this.capacidade;
	}

}
