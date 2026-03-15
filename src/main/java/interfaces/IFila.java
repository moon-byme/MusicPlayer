package interfaces;

import java.util.NoSuchElementException; // Import necessário para lançar exceção quando a fila estiver vazia

/**
 * Interface para Fila (FIFO).
 * Implementada por: (a definir)
 * Uso no sistema (minha sugestão): playlist de reprodução — músicas tocam na
 * ordem adicionada
 */
public interface IFila<T> {
    void enqueue(T elemento);

    T peek() throws NoSuchElementException;

    boolean isEmpty();

    int size();
}