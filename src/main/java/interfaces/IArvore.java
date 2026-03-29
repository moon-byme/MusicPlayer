package interfaces;

import java.util.List;

/**
 * Interface para Árvore de Busca Binária Balanceada (AVL).
 *
 * <p>
 * Define o contrato das operações essenciais de uma árvore AVL genérica.
 * A árvore mantém balanceamento automático após inserções e remoções,
 * garantindo complexidade O(log n) para busca, inserção e remoção.
 * </p>
 *
 * <p>
 * No sistema de música, é usada para organizar e buscar músicas por título
 * em ordem alfabética eficiente.
 * </p>
 *
 * <p>
 * Implementada por: {@link structures.ArvoreAVL}
 * </p>
 *
 * @param <T> tipo dos elementos armazenados; deve ser {@link Comparable}
 * @author Lethycia
 */
public interface IArvore<T extends Comparable<T>> {

    /**
     * Insere um elemento na árvore.
     * Mantém o balanceamento AVL automaticamente após a inserção.
     * Elementos duplicados (mesmo título e artista) são ignorados.
     *
     * @param elemento o elemento a ser inserido; ignorado se {@code null}
     */
    void inserir(T elemento);

    /**
     * Verifica se um elemento existe na árvore.
     *
     * @param elemento o elemento a ser buscado
     * @return {@code true} se encontrado, {@code false} caso contrário
     */
    boolean buscar(T elemento);

    /**
     * Remove um elemento da árvore.
     * Mantém o balanceamento AVL automaticamente após a remoção.
     *
     * @param elemento o elemento a ser removido
     */
    void remover(T elemento);

    /**
     * Retorna todos os elementos em ordem crescente (percurso in-order).
     * Para músicas, retorna a lista em ordem alfabética por título.
     *
     * @return lista com os elementos em ordem
     */
    List<T> inOrder();

    /**
     * Verifica se a árvore está vazia.
     *
     * @return {@code true} se não houver elementos, {@code false} caso contrário
     */
    boolean isEmpty();
}