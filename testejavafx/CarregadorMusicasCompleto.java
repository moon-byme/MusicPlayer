package testejavafx;

import Artistas.Musicas;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CarregadorMusicasCompleto {
    
    private static final String[][] ARTISTAS = {
        {"Angra", "Power Metal"},
        {"Bon Jovi", "Rock"},
        {"Charlie Brown Jr", "Rock Brasileiro"},
        {"Gracie Abrams", "Pop"},
        {"Kate Bush", "Art Pop"},
        {"Linkin Park", "Rock"},
        {"Marina Sena", "Pop"},
        {"Mitski", "Indie Rock"},
        {"Olivia Rodrigo", "Pop Rock"},
        {"TWICE", "K-pop"}
    };
    
    public static List<Musicas> carregarTodasMusicas() {
        List<Musicas> musicas = new ArrayList<>();
        
        System.out.println("📀 CARREGANDO MÚSICAS...");
        System.out.println("-----------------------------------------");
        
        // Tenta carregar da pasta
        List<Musicas> musicasDaPasta = carregarDaPasta();
        
        if (!musicasDaPasta.isEmpty()) {
            System.out.println("-----------------------------------------");
            System.out.println("✅ " + musicasDaPasta.size() + " músicas carregadas da pasta!");
            return musicasDaPasta;
        }
        
        // Se não encontrar, usa embutidas
        System.out.println("⚠️ Pasta não encontrada. Usando músicas de exemplo.");
        return carregarMusicasEmbutidas();
    }
    
    private static List<Musicas> carregarDaPasta() {
        List<Musicas> musicas = new ArrayList<>();
        
        String userHome = System.getProperty("user.home");
        String[] caminhosPossiveis = {
            userHome + "/Downloads/Músicas do Projeto/Músicas do Projeto",
            "C:/Users/" + System.getProperty("user.name") + "/Downloads/Músicas do Projeto/Músicas do Projeto",
            "./Músicas do Projeto/Músicas do Projeto"
        };
        
        File pastaPrincipal = null;
        for (String caminho : caminhosPossiveis) {
            File teste = new File(caminho);
            if (teste.exists() && teste.isDirectory()) {
                pastaPrincipal = teste;
                System.out.println("✓ Pasta encontrada: " + caminho);
                break;
            }
        }
        
        if (pastaPrincipal == null) {
            return musicas;
        }
        
        File[] pastasArtistas = pastaPrincipal.listFiles(File::isDirectory);
        
        if (pastasArtistas != null) {
            for (File pastaArtista : pastasArtistas) {
                String nomeArtista = pastaArtista.getName();
                String genero = extrairGenero(nomeArtista);
                
                File[] arquivos = pastaArtista.listFiles((dir, name) -> 
                    name.endsWith(".mp3") || name.endsWith(".MP3"));
                
                if (arquivos != null) {
                    for (File arquivo : arquivos) {
                        String nomeMusica = arquivo.getName().replaceAll("\\.(mp3|MP3)$", "");
                        // Remove " - Artista" se existir
                        if (nomeMusica.contains(" - ")) {
                            nomeMusica = nomeMusica.split(" - ")[0];
                        }
                        Musicas musica = new Musicas(nomeMusica, nomeArtista, genero, 180);
                        musicas.add(musica);
                        System.out.println("  📀 " + nomeArtista + " - " + nomeMusica);
                    }
                }
            }
        }
        
        return musicas;
    }
    
    private static List<Musicas> carregarMusicasEmbutidas() {
        List<Musicas> musicas = new ArrayList<>();
        
        String[][] MUSICAS_POR_ARTISTA = {
            {"Carry On", "Lasting Child", "Rebirth", "Streets of Tomorrow", "Wuthering Heights"},
            {"Bed Of Roses", "It's My Life", "Livin On A Prayer", "Runaway", "You Give Love A Bad Name"},
            {"Céu Azul", "Como Tudo Deve Ser", "Dias De Luta, Dias De Gloria", "Ela Vai Voltar", "Senhor Do Tempo"},
            {"21", "Camden", "Close To You", "I Love You, I'm Sorry", "I Should Hate You"},
            {"Army Dreamers", "Baboooshka", "Cloudbusting", "Running Up That Hill", "Wuthering Heights"},
            {"In The End", "What I've Done"},
            {"Coisas Naturais", "Lua Cheia", "Olho No Gato", "Saí Para Ver O Mar", "Santo"},
            {"First Love Late Spring", "I Bet On Losing Dogs", "Me and My Husband", "My Love Mine All Mine"},
            {"All I Want", "Bad Idea Right", "Ballad Of A Homeschooled Girl", "Favorite Crime", "Good 4 U", "So American"},
            {"BLOOM", "Doughnut", "LOVE FOOLISH", "Talk That Talk", "What Is Love"}
        };
        
        for (int i = 0; i < ARTISTAS.length; i++) {
            String nomeArtista = ARTISTAS[i][0];
            String genero = ARTISTAS[i][1];
            for (String nomeMusica : MUSICAS_POR_ARTISTA[i]) {
                Musicas musica = new Musicas(nomeMusica, nomeArtista, genero, 180);
                musicas.add(musica);
                System.out.println("  📀 " + nomeArtista + " - " + nomeMusica);
            }
        }
        
        return musicas;
    }
    
    private static String extrairGenero(String artista) {
        for (String[] a : ARTISTAS) {
            if (a[0].equalsIgnoreCase(artista)) {
                return a[1];
            }
        }
        return "Pop";
    }
}