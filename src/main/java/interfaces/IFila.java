package interfaces;

import java.util.NoSuchElementException;

/**
 * Interface para Fila com política FIFO (First In, First Out).
 *
 * <p>
 * Define o contrato de uma fila genérica onde o primeiro elemento
 * inserido é o primeiro a ser removido. Complexidade O(1) para
 * enqueue e dequeue.
 * </p>
 *
 * <p>
 * No sistema de música, é usada como fila de reprodução:
 * as músicas tocam na ordem em que foram adicionadas.
 * </p>
 *
 * <p>
 * Implementada por: {@link structures.FiladeReproducao}
 * </p>
 *
 * @param <T> tipo dos elementos armazenados na fila
 * @author Lethycia
 */
public interface IFila<T> {

    /**
     * Adiciona um elemento ao final da fila.
     *
     * @param elemento o elemento a ser inserido
     */
    void enqueue(T elemento);

    /**
     * Retorna o primeiro elemento da fila sem removê-lo.
     *
     * @return o elemento no início da fila
     * @throws NoSuchElementException se a fila estiver vazia
     */
    T peek() throws NoSuchElementException;

    /**
     * Verifica se a fila está vazia.
     *
     * @return {@code true} se não houver elementos, {@code false} caso contrário
     */
    boolean isEmpty();

    /**
     * Retorna o número de elementos na fila.
     *
     * @return quantidade de elementos
     */
    int size();
}