package reproducer;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import model.Musica;
import structures.ArvoreAVL;
import structures.FiladeReproducao;
import structures.HeapBinaria;
import structures.HistoricoMusicas;
import structures.HashArtistas;

/**
 * Camada central de lógica do reprodutor de músicas.
 *
 * <p>
 * Orquestra o catálogo ({@link structures.ArvoreAVL}), a fila de reprodução
 * ({@link structures.FiladeReproducao}), o histórico
 * ({@link structures.HistoricoMusicas}),
 * o ranking de mais tocadas ({@link structures.HeapBinaria}) e o índice por
 * artista
 * ({@link structures.HashArtistas}).
 * </p>
 *
 * <p>
 * Principais responsabilidades:
 * </p>
 * <ul>
 * <li>Adicionar músicas ao catálogo e à hash de artistas</li>
 * <li>Gerenciar a fila FIFO de reprodução</li>
 * <li>Registrar músicas tocadas no histórico e atualizar o ranking</li>
 * <li>Navegar para a música anterior via histórico</li>
 * <li>Buscar músicas por título ou artista (parcial, case-insensitive)</li>
 * </ul>
 *
 * @author Isabelle
 * @author Lethycia
 */
public class Reprodutor {

    private ArvoreAVL catalogo;
    private FiladeReproducao fila;
    private HistoricoMusicas historico;
    private HeapBinaria ranking;
    private Musica tocandoAtualmente;
    private HashArtistas hashArtistas;

    public Reprodutor() {
        this.catalogo = new ArvoreAVL();
        this.fila = new FiladeReproducao();
        this.historico = new HistoricoMusicas();
        this.ranking = new HeapBinaria();
        this.tocandoAtualmente = null;
        this.hashArtistas = new HashArtistas(100);

    }

    // public void adicionarMusica(Musica musica) {
        catalogo.inserir(musica);
        hashArtistas.inserir(musica);
        System.out.println("✓ Música \"" + musica.getTitulo() + "\" adicionada.");
    }

    public Musica buscarMusica(String titulo) {
        Musica resultado = catalogo.buscarPorTitulo(titulo);
        if (resultado != null)
            return resultado;
        // Fallback: busca normalizada (ignora acentos) para títulos acentuados
        String tituloNorm = normalizar(titulo);
        for (Musica m : catalogo.inOrder()) {
            if (normalizar(m.getTitulo()).equals(tituloNorm)) {
                return m;
            }
        }
        return null;
    }

    public List<Musica> listarTodasMusicas() {
        return catalogo.inOrder();
    }

    public void listarPorArtista(String nomeArtista) {
        System.out.println("\n===== M\u00daSICAS DE: " + nomeArtista.toUpperCase() + " =====");

        var lista = hashArtistas.buscarMusicas(nomeArtista);
        if (lista == null || lista.isEmpty()) {
            System.out.println("Nenhuma m\u00fasica encontrada para este artista.");
        } else {
            lista.exibir();
        }
    }

    public ArvoreAVL getCatalogo() {
        return catalogo;
    }

    public HistoricoMusicas getHistoricoMusicas() {
        return historico;
    }

    public void adicionarNaFila(Musica musica) {
        if (musica == null)
            return;
        fila.enqueue(musica);
        System.out.println("✓ \"" + musica.getTitulo() + "\" adicionado à fila.");
    }

    public Musica obterProximaDaFila() {
        return fila.dequeue(); // Remove e retorna a primeira da fila
    }

    public void marcarComoTocada(Musica musica) {
        if (musica != null) {
            musica.incrementarPlays();
            if (tocandoAtualmente != null) {
                historico.adicionar(tocandoAtualmente);
            }
            tocandoAtualmente = musica;
            atualizarRanking();
        }
    }

    /**
     * Volta para a música anterior e retorna ela (para a UI poder tocar)
     */
    public Musica voltarMusicaUI() {
        Musica anterior = historico.voltar();

        if (anterior == null) {
            System.out.println("⎮ Não há música anterior no histórico.");
            return null;
        }

        if (tocandoAtualmente != null) {
            fila.enqueue(tocandoAtualmente);
        }

        tocandoAtualmente = anterior;
        System.out.println("⏮ Voltando para: " + anterior.getTitulo());
        return anterior;
    }

    public void voltarMusica() {
        Musica anterior = historico.voltar();

        if (anterior == null) {
            System.out.println("\n⏮ Nenhuma música no histórico.");
            return;
        }

        if (tocandoAtualmente != null) {
            fila.enqueue(tocandoAtualmente);
        }

        tocandoAtualmente = anterior;
        System.out.println("\n⏮ Voltando para: " + anterior.getTitulo());
    }

    public void atualizarRanking() {
        ranking = new HeapBinaria();
        for (Musica m : catalogo.inOrder()) {
            ranking.inserir(m);
        }
    }

    public void mostrarRanking() {
        System.out.println("\n===== MAIS TOCADAS =====");
        if (ranking.isEmpty()) {
            System.out.println("Nenhuma música tocada.");
            return;
        }

        // Cria cópia
        HeapBinaria copia = new HeapBinaria();
        for (Musica m : catalogo.inOrder()) {
            copia.inserir(m);
        }

        int pos = 1;
        while (!copia.isEmpty()) {
            Musica m = copia.extrairMax();
            if (m.getPlays() > 0) {
                System.out.println(pos + ". " + m.getTitulo() + " (" + m.getPlays() + " plays)");
                pos++;
            }
        }
    }

    public void musicaAtual() {
        if (tocandoAtualmente == null) {
            System.out.println("Nenhuma música tocando.");
        } else {
            System.out.println("Tocando: " + tocandoAtualmente.getTitulo());
        }
    }

    public void exibirFila() {
        fila.exibir();
    }

    public void exibirHistorico() {
        System.out.println("\n========== STATUS DO REPRODUTOR ==========");
        if (tocandoAtualmente != null) {
            System.out.println("▶ Reproduzindo: " + tocandoAtualmente.getTitulo() +
                    " [" + tocandoAtualmente.getArtista() + "]");
        } else {
            System.out.println("▶ Reproduzindo: <Nenhuma música em reprodução>");
        }

        System.out.println("------------------------------------------");
        System.out.println("⏮ MÚSICAS ANTERIORES (Histórico):");
        historico.exibir();

        System.out.println("==========================================");
    }

    public boolean filaVazia() {
        return fila.isEmpty();
    }

    public List<Musica> buscarMusicasPorTitulo(String parte) {
        List<Musica> resultado = new ArrayList<>();
        String parteNorm = normalizar(parte);
        for (Musica m : catalogo.inOrder()) {
            if (normalizar(m.getTitulo()).contains(parteNorm)) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    private static String normalizar(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "")
                .toLowerCase();
    }

    /**
     * Busca músicas pelo nome (parcial) do artista usando a HashArtistas.
     */
    public List<Musica> buscarMusicasPorArtista(String nomeArtista) {
        return hashArtistas.buscarMusicasPorNomeParcial(nomeArtista);
    }

    public Musica getMusicaAtual() {
        return tocandoAtualmente;
    }
}