package Artistas;

public class testeHistorico {
	public static void main(String[] args) {
		HashArtistas repositorio = new HashArtistas(5);
		
		Pilha<Artista> historicoArtistas = new Pilha<>();
		
		repositorio.insert(new Artista("Linkin Park", "Rock"));
	    repositorio.insert(new Artista("Alok", "Eletrônica"));
	    
	    Artista procurado = repositorio.search("Alok");
	    
	    if(procurado != null) {
	    	System.out.println("visitando: " + procurado.getNome());
	        historicoArtistas.push(procurado);
	    }
	    historicoArtistas.exibir("ARTISTAS VISITADOS RECENTEMENTE");
	}

}
