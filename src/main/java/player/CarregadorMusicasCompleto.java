package player;

import model.Musica;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Carregador de músicas para o player.
 *
 * <p>
 * Tenta carregar os arquivos MP3 da pasta local
 * {@code ~/Downloads/Músicas do Projeto}.
 * Caso a pasta não seja encontrada, carrega uma lista embutida com
 * 10 artistas e suas músicas como fallback.
 * </p>
 *
 * @author Isabelle
 */
public class CarregadorMusicasCompleto {

    private static final String[][] ARTISTAS = {
            { "Angra", "Power Metal" },
            { "Bon Jovi", "Rock" },
            { "Charlie Brown Jr", "Rock Brasileiro" },
            { "Gracie Abrams", "Pop" },
            { "Kate Bush", "Art Pop" },
            { "Linkin Park", "Rock" },
            { "Marina Sena", "Pop" },
            { "Mitski", "Indie Rock" },
            { "Olivia Rodrigo", "Pop Rock" },
            { "TWICE", "K-pop" }
    };

    /**
     * Ponto de entrada principal para o carregamento do catálogo.
     *
     * <p>
     * Primeiro tenta carregar músicas da pasta local do usuário.
     * Caso a pasta não exista, utiliza a lista embutida como fallback.
     * </p>
     *
     * @return lista com todas as músicas disponíveis (nunca {@code null})
     */
    public static List<Musica> carregarTodasMusicas() {
        // List<Musica> musica = new ArrayList<>();

        System.out.println("📀 CARREGANDO MÚSICAS...");
        System.out.println("-----------------------------------------");

        // Tenta carregar da pasta
        List<Musica> musicasDaPasta = carregarDaPasta();

        if (!musicasDaPasta.isEmpty()) {
            System.out.println("-----------------------------------------");
            System.out.println("✅ " + musicasDaPasta.size() + " músicas carregadas da pasta!");
            return musicasDaPasta;
        }

        // Se não encontrar, usa embutidas
        System.out.println("⚠️ Pasta não encontrada. Usando músicas de exemplo.");
        return carregarMusicasEmbutidas();
    }

    /**
     * Tenta localizar e carregar arquivos MP3 da pasta local do usuário.
     *
     * <p>
     * Verifica sequencialmente os caminhos possíveis e utiliza o primeiro
     * que existir. Para cada subpasta encontrada, interpreta o nome como
     * artista e carrega todos os arquivos {@code .mp3} presentes.
     * </p>
     *
     * @return lista de músicas encontradas, ou lista vazia se nenhum caminho
     *         existir
     */
    private static List<Musica> carregarDaPasta() {
        List<Musica> musica = new ArrayList<>();

        String userHome = System.getProperty("user.home");
        String[] caminhosPossiveis = {
                userHome + "/Downloads/Músicas do Projeto",
                "C:/Users/" + System.getProperty("user.name") + "/Downloads/Músicas do Projeto",
                "./Músicas do Projeto"
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
            return musica;
        }

        File[] pastasArtistas = pastaPrincipal.listFiles(File::isDirectory);

        if (pastasArtistas != null) {
            for (File pastaArtista : pastasArtistas) {
                String nomeArtista = pastaArtista.getName();
                String genero = extrairGenero(nomeArtista);

                File[] arquivos = pastaArtista.listFiles((dir, name) -> name.endsWith(".mp3") || name.endsWith(".MP3"));

                if (arquivos != null) {
                    for (File arquivo : arquivos) {
                        String nomeMusica = arquivo.getName().replaceAll("\\.(mp3|MP3)$", "");
                        // Remove " - Artista" se existir
                        if (nomeMusica.contains(" - ")) {
                            nomeMusica = nomeMusica.split(" - ")[0];
                        }
                        Musica m = new Musica(nomeMusica, nomeArtista, genero, 180);
                        musica.add(m);
                        System.out.println("  📀 " + nomeArtista + " - " + nomeMusica);
                    }
                }
            }
        }

        return musica;
    }

    /**
     * Retorna uma lista embutida de músicas de exemplo.
     *
     * <p>
     * Utilizada como fallback quando a pasta local não é encontrada.
     * Contém 10 artistas com suas músicas predefinidas.
     * </p>
     *
     * @return lista com as músicas embutidas
     */
    private static List<Musica> carregarMusicasEmbutidas() {
        List<Musica> musica = new ArrayList<>();

        String[][] MUSICAS_POR_ARTISTA = {
                { "Carry On", "Lasting Child", "Rebirth", "Streets of Tomorrow", "Wuthering Heights" },
                { "Bed Of Roses", "It's My Life", "Livin On A Prayer", "Runaway", "You Give Love A Bad Name" },
                { "Céu Azul", "Como Tudo Deve Ser", "Dias De Luta, Dias De Gloria", "Ela Vai Voltar",
                        "Senhor Do Tempo" },
                { "21", "Camden", "Close To You", "I Love You, I'm Sorry", "I Should Hate You" },
                { "Army Dreamers", "Baboooshka", "Cloudbusting", "Running Up That Hill", "Wuthering Heights" },
                { "In The End", "What I've Done" },
                { "Coisas Naturais", "Lua Cheia", "Olho No Gato", "Saí Para Ver O Mar", "Santo" },
                { "First Love Late Spring", "I Bet On Losing Dogs", "Me and My Husband", "My Love Mine All Mine" },
                { "All I Want", "Bad Idea Right", "Ballad Of A Homeschooled Girl", "Favorite Crime", "Good 4 U",
                        "So American" },
                { "BLOOM", "Doughnut", "LOVE FOOLISH", "Talk That Talk", "What Is Love" }
        };

        for (int i = 0; i < ARTISTAS.length; i++) {
            String nomeArtista = ARTISTAS[i][0];
            String genero = ARTISTAS[i][1];
            for (String nomeMusica : MUSICAS_POR_ARTISTA[i]) {
                Musica m = new Musica(nomeMusica, nomeArtista, genero, 180);
                musica.add(m);
                System.out.println("  📀 " + nomeArtista + " - " + nomeMusica);
            }
        }

        return musica;
    }

    /**
     * Retorna o gênero musical de um artista pelo nome.
     *
     * <p>
     * Busca no array {@link #ARTISTAS} de forma case-insensitive.
     * Retorna {@code "Pop"} como padrão quando o artista não é encontrado.
     * </p>
     *
     * @param artista nome do artista a pesquisar
     * @return gênero musical correspondente, ou {@code "Pop"} se não encontrado
     */
    private static String extrairGenero(String artista) {
        for (String[] a : ARTISTAS) {
            if (a[0].equalsIgnoreCase(artista)) {
                return a[1];
            }
        }
        return "Pop";
    }
}