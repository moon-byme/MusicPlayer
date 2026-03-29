package interfaces;

import java.util.NoSuchElementException;

/**
 * Interface para Pilha com política LIFO (Last In, First Out).
 *
 * <p>
 * Define o contrato de uma pilha genérica onde o último elemento
 * inserido é o primeiro a ser removido. Complexidade O(1) para
 * push, pop e peek.
 * </p>
 *
 * <p>
 * No sistema de música, é usada para o histórico de reprodução:
 * sempre que o usuário pressiona 'voltar', a música anterior
 * (topo da pilha) é retomada.
 * </p>
 *
 * <p>
 * Implementada por: {@link structures.Pilha} e
 * {@link structures.HistoricoMusicas}
 * </p>
 *
 * @param <T> tipo dos elementos armazenados na pilha
 * @author Lethycia
 */
public interface IPilha<T> {

    /**
     * Empilha um elemento no topo da pilha.
     *
     * @param elemento o elemento a ser inserido
     */
    void push(T elemento);

    /**
     * Remove e retorna o elemento do topo da pilha.
     *
     * @return o elemento no topo
     * @throws NoSuchElementException se a pilha estiver vazia
     */
    T pop() throws NoSuchElementException;

    /**
     * Retorna o elemento do topo sem removê-lo.
     *
     * @return o elemento no topo
     * @throws NoSuchElementException se a pilha estiver vazia
     */
    T peek() throws NoSuchElementException;

    /**
     * Verifica se a pilha está vazia.
     *
     * @return {@code true} se não houver elementos, {@code false} caso contrário
     */
    boolean isEmpty();

    /**
     * Retorna o número de elementos na pilha.
     *
     * @return quantidade de elementos
     */
    int size();
}