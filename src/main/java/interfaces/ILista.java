package interfaces;

import java.util.NoSuchElementException;

/**
 * Interface para Lista Encadeada genérica.
 * Implementada por: (a definir)
 */
public interface ILista<T> {
    void addFirst(T elemento);

    void addLast(T elemento);

    T removeFirst() throws NoSuchElementException;

    T removeLast() throws NoSuchElementException;

    T get(int index) throws IndexOutOfBoundsException;

    boolean remove(T elemento);

    boolean isEmpty();

    int size();
}