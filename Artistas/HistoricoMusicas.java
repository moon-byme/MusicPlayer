package Artistas;

public class HistoricoMusicas {
	private No<Musicas> topo;
	private int tamanho;
	
	public HistoricoMusicas() {
		this.topo = null;
		this.tamanho = 0;
	}
	
	public void addAoHistorico(Musicas musica) {
		No<Musicas> novoNo = new No<>(musica);
		
		novoNo.setProximo(topo);
		novoNo = topo;
		tamanho++;
	}
	
	public Musicas voltarMusica() {
		if(isEmpty()) {
			return null;
		}
		
		Musicas ultimaMusica = topo.getDado();
		topo = topo.getProximo();
		tamanho--;
		
		return ultimaMusica;
		
	}
	

	
	public boolean isEmpty() {
		return this.topo == null;
	}

	public int getTamanho() {
		return tamanho;
	}
}
