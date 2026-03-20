package Artistas;
import java.util.Scanner;

public class teste {

	public static void main(String[] args) {
		HashArtistas artistas = new HashArtistas(5);
		Scanner teclado = new Scanner(System.in);
		
		artistas.insert(new Artista("Linkin Park", "Rock"));
	    artistas.insert(new Artista("Angra", "Power Metal"));
	    artistas.insert(new Artista("Mamonas Assassinas", "Rock"));
	    artistas.insert(new Artista("Bon Jovi", "Rock"));
	    artistas.insert(new Artista("Kate Bush", "Art Pop/Art Rock"));
	    artistas.insert(new Artista("Olivia Rodrigo", "Pop Rock"));
	    artistas.insert(new Artista("Marina Sena", "Pop"));
	    artistas.insert(new Artista("TWICE", "Kpop"));
	    
	    System.out.println("\n--- LISTA DE TODOS OS ARTISTAS ---");
        artistas.exibirCatalogo();
        
        System.out.println("\nDigite o nome do um artista que deseja ouvir: ");
        String nomeBusca = teclado.nextLine();
	    Artista encontrado = artistas.search(nomeBusca);
	    
	    if(encontrado != null) {
	    	System.out.println("Artista Encontrado " + encontrado.getNome() + " [" + encontrado.getGenero() + "]");
	    } else {
	    	System.out.println("Erro: Artista " + nomeBusca + " Não Encontrado.");
	    }
	    teclado.close();

	}

}
