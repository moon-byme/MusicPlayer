package reproducer;

import java.util.ArrayList;
import java.util.List;

import model.Musica;
import structures.ArvoreAVL;
import structures.FiladeReproducao;
import structures.HeapBinaria;
import structures.HistoricoMusicas;

public class Reprodutor {

    private ArvoreAVL catalogo;
    private FiladeReproducao fila;
    private HistoricoMusicas historico;
    private HeapBinaria ranking;
    private Musica tocandoAtualmente;

    public Reprodutor() {
        this.catalogo = new ArvoreAVL();
        this.fila = new FiladeReproducao();
        this.historico = new HistoricoMusicas();
        this.ranking = new HeapBinaria();
        this.tocandoAtualmente = null;
    }

    public void adicionarMusica(Musica musica) {
        catalogo.inserir(musica);
        System.out.println("✓ Música \"" + musica.getTitulo() + "\" adicionada.");
    }

    public Musica buscarMusica(String titulo) {
        return catalogo.buscarPorTitulo(titulo);
    }

    public List<Musica> listarTodasMusicas() {
        return catalogo.inOrder();
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
        historico.exibir();
    }

    public boolean filaVazia() {
        return fila.isEmpty();
    }

    public List<Musica> buscarMusicasPorTitulo(String parte) {
        List<Musica> resultado = new ArrayList<>();
        for (Musica m : catalogo.inOrder()) {
            if (m.getTitulo().toLowerCase().contains(parte.toLowerCase())) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    public Musica getMusicaAtual() {
        return tocandoAtualmente;
    }
}