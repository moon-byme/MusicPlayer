package interfaces;

import java.util.NoSuchElementException;

/**
 * Interface para Heap Binária (max-heap).
 * Implementada por: (a definir)
 * Uso no sistema (minha sugestão): ranking de músicas por número de plays
 */
public interface IHeap<T> {
    T extrairMax() throws NoSuchElementException;

    T peek() throws NoSuchElementException;

    boolean isEmpty();

    int size();
}