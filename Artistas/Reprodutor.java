package Artistas;

import java.util.ArrayList;
import java.util.List;

public class Reprodutor {
    
    private ArvoreAVL catalogo;
    private FiladeReproducao fila;
    private HistoricoMusicas historico;
    private HeapBinaria ranking;
    private Musicas tocandoAtualmente;
    
    public Reprodutor() {
        this.catalogo = new ArvoreAVL();
        this.fila = new FiladeReproducao();
        this.historico = new HistoricoMusicas();
        this.ranking = new HeapBinaria();
        this.tocandoAtualmente = null;
    }
    
    public void adicionarMusica(Musicas musica) {
        catalogo.inserir(musica);
        System.out.println("✓ Música \"" + musica.getNome() + "\" adicionada.");
    }
    
    public Musicas buscarMusica(String titulo) {
        return catalogo.buscarPorTitulo(titulo);
    }
    
    public List<Musicas> listarTodasMusicas() {
        return catalogo.inOrder();
    }
    
    public void adicionarNaFila(Musicas musica) {
        if (musica == null) return;
        fila.enqueue(musica);
        System.out.println("✓ \"" + musica.getNome() + "\" adicionado à fila.");
    }
    
    
    public Musicas obterProximaDaFila() {
        return fila.dequeue(); // Remove e retorna a primeira da fila
    }
    
    
    public void marcarComoTocada(Musicas musica) {
        if (tocandoAtualmente != null) {
            historico.adicionar(tocandoAtualmente);
        }
        tocandoAtualmente = musica;
        atualizarRanking();
    }
    
    public void voltarMusica() {
        Musicas anterior = historico.voltar();
        
        if (anterior == null) {
            System.out.println("\n⏮ Nenhuma música no histórico.");
            return;
        }
        
        if (tocandoAtualmente != null) {
            fila.enqueue(tocandoAtualmente);
        }
        
        tocandoAtualmente = anterior;
        System.out.println("\n⏮ Voltando para: " + anterior.getNome());
    }
    
    
    public void atualizarRanking() {
        ranking = new HeapBinaria();
        for (Musicas m : catalogo.inOrder()) {
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
        for (Musicas m : catalogo.inOrder()) {
            copia.inserir(m);
        }
        
        int pos = 1;
        while (!copia.isEmpty()) {
            Musicas m = copia.extrairMax();
            if (m.getPlays() > 0) {
                System.out.println(pos + ". " + m.getNome() + " (" + m.getPlays() + " plays)");
                pos++;
            }
        }
    }
    
    public void musicaAtual() {
        if (tocandoAtualmente == null) {
            System.out.println("Nenhuma música tocando.");
        } else {
            System.out.println("Tocando: " + tocandoAtualmente.getNome());
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
    
    
    public List<Musicas> buscarMusicasPorTitulo(String parte) {
        List<Musicas> resultado = new ArrayList<>();
        for (Musicas m : catalogo.inOrder()) {
            if (m.getNome().toLowerCase().contains(parte.toLowerCase())) {
                resultado.add(m);
            }
        }
        return resultado;
    }
}