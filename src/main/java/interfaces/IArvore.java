package interfaces;

/**
 * Interface para Árvore de Busca Binária Balanceada (AVL).
 * Implementada por: (a definir)
 * Uso no sistema (minha sugestão): busca por título em O(log n); listagem
 * alfabética via inOrder()
 */
public interface IArvore<T extends Comparable<T>> {
    void inserir(T elemento);

    boolean buscar(T elemento);

    void remover(T elemento);

    ILista<T> inOrder();

    boolean isEmpty();
}