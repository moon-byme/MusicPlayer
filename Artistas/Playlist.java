package Artistas;

public class Playlist {
	No<Musicas> inicio;
	No<Musicas> fim;
	private int tamanho;
	
	public void addMusica(Musicas m) {
		No<Musicas> novoNo = new No<> (m);
		if(inicio == null) {
			inicio = fim = novoNo;
		} else {
			fim.getProximo();
		}
		fim = novoNo;
		setTamanho(getTamanho() + 1);
	}
	
	public void delMusica(String nomeProcurado) {
		if (inicio == null) {
			System.out.println("Sua Playlist Ainda não possui musicas.");
			return;
		}
		
		if (inicio.getDado().getNome().contains(nomeProcurado)) {
			inicio = inicio.getProximo();
			System.out.println("Música " + nomeProcurado + " removida."); // apenas para teste
	        return;
		}
		
		No<Musicas> atual = inicio;
		No<Musicas> anterior = null;
		
		while (atual != null && !atual.getDado().getNome().equalsIgnoreCase(nomeProcurado)) {
			anterior = atual;
			atual = atual.getProximo();
		}
		
		if (atual != null) {
			anterior.setProximo(atual.getProximo());
			System.out.println("Musica" + nomeProcurado + "removida da playlist.");
		} else {
	        System.out.println("Música não encontrada na playlist.");
		}
	}
	
	public void exibirPlaylist() {
		System.out.println("\n===== MINHA PLAYLIST =====");
		No<Musicas> atual =  inicio;
		
		while (atual != null) {
			System.out.println("=>" + atual.getDado().getNome());
			atual = atual.getProximo();
		}
	}

	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

}
