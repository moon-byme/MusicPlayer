package structures;

import model.Musica;
import interfaces.IHeap;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Heap binária máxima (Max-Heap) para ranquear músicas por número de
 * reproduções.
 *
 * <p>
 * A raiz sempre contém a música mais tocada. Operações de inserção e extração
 * são O(log n). Implementada sobre um {@link java.util.ArrayList} com índices
 * de
 * pai/filho calculados aritmeticamente.
 * </p>
 *
 * @author Isabelle
 * @see interfaces.IHeap
 * @see model.Musica#getPlays()
 */
public class HeapBinaria implements IHeap<Musica> {

    private List<Musica> heap; // nossa "árvore" no array
    private int tamanho; // quantas músicas tem

    public HeapBinaria() {
        this.heap = new ArrayList<>();
        this.tamanho = 0;
    }

    // ==================== MÉTODOS AUXILIARES :D ====================

    private int pai(int i) {
        return (i - 1) / 2; // achar o pai
    }

    private int esquerdo(int i) {
        return 2 * i + 1; // filho da esquerda
    }

    private int direito(int i) {
        return 2 * i + 2; // filho da direita
    }

    // troca dois elementos de lugar
    private void trocar(int i, int j) {
        Musica temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // MÉTODO HEAPFY MANUTENÇÃO ABAIXO:)

    // HEAPIFY UP
    private void subir(int i) {
        while (i > 0) {
            int p = pai(i);
            // se já tá no lugar certo, para
            if (heap.get(i).getPlays() <= heap.get(p).getPlays())
                break;
            // senão, sobe trocando com o pai
            trocar(i, p);
            i = p;
        }
    }

    // HEAPIFY DOWN
    private void descer(int i) {
        int maior = i;
        int esq = esquerdo(i);
        int dir = direito(i);

        // vê qual filho é o maior
        if (esq < tamanho && heap.get(esq).getPlays() > heap.get(maior).getPlays())
            maior = esq;
        if (dir < tamanho && heap.get(dir).getPlays() > heap.get(maior).getPlays())
            maior = dir;

        // se o maior não é o pai, troca e continua descendo
        if (maior != i) {
            trocar(i, maior);
            descer(maior);
        }
    }

    /**
     * Insere uma música na heap e reajusta a propriedade max-heap — O(log n).
     *
     * @param musica música a ser inserida; ignorada se {@code null}
     */
    public void inserir(Musica musica) {
        if (musica == null)
            return;
        heap.add(musica);
        tamanho++;
        subir(tamanho - 1); // sobe o último elemento
    }

    // TIRA A RAIZ (música mais tocada) O(log n)
    @Override
    public Musica extrairMax() throws NoSuchElementException {
        if (isEmpty())
            throw new NoSuchElementException("Heap vazia");

        Musica max = heap.get(0); // a mais tocada

        // último vai pra raiz
        heap.set(0, heap.get(tamanho - 1));
        heap.remove(tamanho - 1);
        tamanho--;

        if (tamanho > 0)
            descer(0); // ajusta a heap

        return max;
    }

    // OLHA A MAIS TOCADA SEM REMOVER (O(1))
    @Override
    public Musica peek() throws NoSuchElementException {
        if (isEmpty())
            throw new NoSuchElementException("Heap vazia");
        return heap.get(0);
    }

    // Métodos auxiliares gerais abaixo:

    @Override
    public boolean isEmpty() {
        return tamanho == 0;
    }

    @Override
    public int size() {
        return tamanho;
    }

    /**
     * RETORNA TODOS OS ELEMENTOS (pra poder exibir)
     */
    public List<Musica> getElementos() {
        return new ArrayList<>(heap);
    }
}