package Artistas;

public class HistoricoMusicas {
    private No<Musicas> topo;
    private int tamanho;
    
    public HistoricoMusicas() {
        this.topo = null;
        this.tamanho = 0;
    }
    
    public void adicionar(Musicas musica) {
        No<Musicas> novoNo = new No<>(musica);
        novoNo.setProximo(topo);
        topo = novoNo;
        tamanho++;
    }
    
    public Musicas voltar() {
        if (isEmpty()) {
            return null;
        }
        
        Musicas ultimaMusica = topo.getDado();
        topo = topo.getProximo();
        tamanho--;
        
        return ultimaMusica;
    }
    
    public boolean isEmpty() {
        return topo == null;
    }
    
    public int getTamanho() {
        return tamanho;
    }
    
	//MÉTODO EXIBIR: adicionado para ver as próximas músicas
    public void exibir() {
        System.out.println("\n===== HISTÓRICO =====");
        if (isEmpty()) {
            System.out.println("Histórico vazio.");
            return;
        }
        
        No<Musicas> atual = topo;
        int posicao = 1;
        while (atual != null) {
            System.out.println(posicao + ". " + atual.getDado().getNome());
            atual = atual.getProximo();
            posicao++;
        }
    }
}
