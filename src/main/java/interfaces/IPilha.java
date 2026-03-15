package interfaces;

import java.util.NoSuchElementException;

/**
 * Interface para Pilha (LIFO).
 * Implementada por: (a definir)
 * Uso no sistema (minha sugestão): histórico de reprodução — botão 'voltar'
 */
public interface IPilha<T> {
    void push(T elemento);

    T pop() throws NoSuchElementException;

    T peek() throws NoSuchElementException;

    boolean isEmpty();

    int size();
}