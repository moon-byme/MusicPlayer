package structures;

import java.util.ArrayList;
import java.util.List;
import model.Musica;

/**
 * Histórico de músicas reproduzidas, implementado como pilha LIFO com nós
 * encadeados.
 *
 * <p>
 * A música mais recente fica no topo; {@link #voltar()} remove e retorna
 * essa música, e {@link #toList()} permite visualizar o histórico sem
 * destruí-lo.
 * </p>
 *
 * @author Carla Nascimento
 * @see structures.No
 * @see model.Musica
 */
public class HistoricoMusicas {
    private No<Musica> topo;
    private int tamanho;

    public HistoricoMusicas() {
        this.topo = null;
        this.tamanho = 0;
    }

    /**
     * Empilha uma música no topo do histórico.
     *
     * @param musica música a ser registrada
     */
    public void adicionar(Musica musica) {
        No<Musica> novoNo = new No<>(musica);
        novoNo.setProximo(topo);
        topo = novoNo;
        tamanho++;
    }

    /**
     * Remove e retorna a música mais recente do histórico (topo da pilha).
     *
     * @return música mais recente, ou {@code null} se o histórico estiver vazio
     */
    public Musica voltar() {
        if (isEmpty()) {
            return null;
        }

        Musica ultimaMusica = topo.getDado();
        topo = topo.getProximo();
        tamanho--;

        return ultimaMusica;
    }

    /**
     * Verifica se o histórico está vazio.
     *
     * @return {@code true} se nenhuma música foi registrada
     */
    public boolean isEmpty() {
        return topo == null;
    }

    /**
     * Retorna o número de músicas no histórico.
     *
     * @return quantidade de entradas
     */
    public int getTamanho() {
        return tamanho;
    }

    /**
     * Exibe no console todas as músicas do histórico, da mais recente à mais
     * antiga.
     */
    public void exibir() {
        System.out.println("\n===== HISTÓRICO =====");
        if (isEmpty()) {
            System.out.println("Histórico vazio.");
            return;
        }

        No<Musica> atual = topo;
        int posicao = 1;
        while (atual != null) {
            System.out.println(posicao + ". " + atual.getDado().getTitulo());
            atual = atual.getProximo();
            posicao++;
        }
    }

    /**
     * Retorna as músicas do histórico em ordem (mais recente primeiro)
     * sem destruir a pilha.
     */
    public List<Musica> toList() {
        List<Musica> lista = new ArrayList<>();
        No<Musica> atual = topo;
        while (atual != null) {
            lista.add(atual.getDado());
            atual = atual.getProximo();
        }
        return lista;
    }
}
