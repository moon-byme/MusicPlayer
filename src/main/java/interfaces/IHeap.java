package interfaces;

import java.util.NoSuchElementException;

/**
 * Interface para Heap Binária Máxima (Max-Heap).
 *
 * <p>
 * Define o contrato de uma heap binária onde o maior elemento
 * sempre está na raiz. Garante complexidade O(log n) para inserção
 * e extração do máximo, e O(1) para consulta da raiz.
 * </p>
 *
 * <p>
 * No sistema de música, é usada para o ranking de músicas mais tocadas:
 * a música com mais plays é sempre a primeira a ser extraída.
 * </p>
 *
 * <p>
 * Implementada por: {@link structures.HeapBinaria}
 * </p>
 *
 * @param <T> tipo dos elementos armazenados na heap
 * @author Lethycia
 */
public interface IHeap<T> {

    /**
     * Remove e retorna o elemento de maior prioridade (raiz da heap).
     * Após a extração, a propriedade de max-heap é restaurada.
     *
     * @return o elemento com maior valor
     * @throws NoSuchElementException se a heap estiver vazia
     */
    T extrairMax() throws NoSuchElementException;

    /**
     * Retorna o elemento de maior prioridade sem removê-lo.
     *
     * @return o elemento na raiz da heap
     * @throws NoSuchElementException se a heap estiver vazia
     */
    T peek() throws NoSuchElementException;

    /**
     * Verifica se a heap está vazia.
     *
     * @return {@code true} se não houver elementos, {@code false} caso contrário
     */
    boolean isEmpty();

    /**
     * Retorna o número de elementos na heap.
     *
     * @return quantidade de elementos
     */
    int size();
}