package Artistas;

import java.util.ArrayList;
import java.util.List;

public class Reprodutor {
    
    private ArvoreAVL catalogo;
    private FiladeReproducao fila;
    private HistoricoMusicas historico;
    private HeapBinaria ranking;
    private Musicas tocandoAtualmente;
    private HashArtistas hashArtistas;
    
    public Reprodutor() {
        this.catalogo = new ArvoreAVL();
        this.fila = new FiladeReproducao();
        this.historico = new HistoricoMusicas();
        this.ranking = new HeapBinaria();
        this.tocandoAtualmente = null;
        this.hashArtistas = new TabelaHash(100);
    }
    
    public void adicionarMusica(Musicas musica) {
        catalogo.inserir(musica);
        hashArtistas.inserir(musica);
        System.out.println("✓ Música \"" + musica.getNome() + "\" adicionada.");
    }
    
    public Musicas buscarMusica(String titulo) {
        return catalogo.buscarPorTitulo(titulo);
    }

    public void buscarPorArtista(String nomeArtista) {
    System.out.println("\n===== Músicas de: " + nomeArtista + " =====");
    
    // A busca na Hash é O(1) instantânea!
    ListaSimplesmenteEncadeada listaMusicas = hashArtistas.buscar(nomeArtista);
    
    if (listaMusicas == null || listaMusicas.isEmpty()) {
        System.out.println("Nenhuma música encontrada para este artista.");
    } else {
        listaMusicas.exibir(); 
    }
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
        System.out.println("\n========== STATUS DO REPRODUTOR ==========");
        if (tocandoAtualmente != null) {
            System.out.println("▶ A TOCAR AGORA: " + tocandoAtualmente.getNome() + 
                               " [" + tocandoAtualmente.getArtista() + "]");
        } else {
            System.out.println("▶ A TOCAR AGORA: <Nenhuma música em reprodução>");
        }
    
        System.out.println("------------------------------------------");
        System.out.println("⏮ MÚSICAS ANTERIORES (Histórico):");
        historico.exibir(); 
        
        System.out.println("==========================================");
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

    public void tocarProxima() {
        Musicas proxima = fila.dequeue();
        
        if (proxima != null) {
            marcarComoTocada(proxima);
            proxima.incrementarPlays(); 
            System.out.println("\n▶ Tocando agora: " + proxima.getNome());
        } else {
            System.out.println("\n⚠ A fila está vazia!");
        }
    }
}
