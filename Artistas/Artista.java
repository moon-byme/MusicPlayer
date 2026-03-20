package Artistas;

import java.util.Objects;

public class Artista {
	private String nome;
	private String genero;
	// private ArvoreALV musicas;
	
	public Artista(String nome, String genero) {
		this.nome = nome;
		this.genero = genero;
		// this.musicas = new ArvoreAVL();
	}
	

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}


	@Override
	public int hashCode() {
		return Objects.hash(genero, nome);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artista other = (Artista) obj;
		return Objects.equals(genero, other.genero) && Objects.equals(nome, other.nome);
	}


	@Override
	public String toString() {
		return "Artista [nome=" + nome + ", genero=" + genero + "]";
	}
	
}
