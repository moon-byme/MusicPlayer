package interfaces;

import java.util.NoSuchElementException;

/**
 * Interface para Lista Encadeada genérica (duplamente encadeada).
 *
 * <p>
 * Define o contrato de uma lista encadeada com operações de inserção
 * e remoção nas duas extremidades em O(1), e acesso por índice em O(n).
 * </p>
 *
 * <p>
 * No sistema de música, pode ser usada como base para playlists
 * onde a ordem de inserção deve ser preservada e músicas podem ser
 * adicionadas tanto no início quanto no final.
 * </p>
 *
 * @param <T> tipo dos elementos armazenados na lista
 * @author Lethycia
 */
public interface ILista<T> {

    /**
     * Insere um elemento no início da lista.
     *
     * @param elemento o elemento a ser inserido
     */
    void addFirst(T elemento);

    /**
     * Insere um elemento no final da lista.
     *
     * @param elemento o elemento a ser inserido
     */
    void addLast(T elemento);

    /**
     * Remove e retorna o primeiro elemento da lista.
     *
     * @return o primeiro elemento
     * @throws NoSuchElementException se a lista estiver vazia
     */
    T removeFirst() throws NoSuchElementException;

    /**
     * Remove e retorna o último elemento da lista.
     *
     * @return o último elemento
     * @throws NoSuchElementException se a lista estiver vazia
     */
    T removeLast() throws NoSuchElementException;

    /**
     * Retorna o elemento na posição indicada sem removê-lo.
     *
     * @param index índice do elemento (base 0)
     * @return o elemento na posição
     * @throws IndexOutOfBoundsException se o índice for inválido
     */
    T get(int index) throws IndexOutOfBoundsException;

    /**
     * Remove a primeira ocorrência do elemento informado.
     *
     * @param elemento o elemento a ser removido
     * @return {@code true} se removido, {@code false} se não encontrado
     */
    boolean remove(T elemento);

    /**
     * Verifica se a lista está vazia.
     *
     * @return {@code true} se não houver elementos, {@code false} caso contrário
     */
    boolean isEmpty();

    /**
     * Retorna o número de elementos na lista.
     *
     * @return quantidade de elementos
     */
    int size();
}