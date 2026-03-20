package Artistas;

public class Musicas {
	private String nome;

		public Musicas(String nome, String genero) {
			this.nome = nome;
		}
		
		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		@Override
		public String toString() {
			return "Musicas [nome=" + nome + "]";
		}

}
