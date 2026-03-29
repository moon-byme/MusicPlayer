package interfaces;

/**
 * Interface para Tabela Hash com encadeamento (Chaining).
 *
 * <p>
 * Define o contrato de uma tabela hash genérica que resolve colisões
 * por encadeamento (listas ligadas em cada bucket).
 * Complexidade média O(1) para inserção, busca e remoção.
 * </p>
 *
 * <p>
 * No sistema de música, é usada para agrupar músicas por artista,
 * permitindo busca rápida por nome do artista.
 * </p>
 *
 * <p>
 * Implementada por: {@link structures.HashArtistas}
 * </p>
 *
 * @param <K> tipo da chave (ex: nome do artista como {@code String})
 * @param <V> tipo do valor armazenado (ex: {@link model.Artista})
 * @author Lethycia
 */
public interface IHash<K, V> {

    /**
     * Insere ou substitui um valor associado a uma chave.
     *
     * @param chave a chave de indexação
     * @param valor o valor a ser armazenado
     */
    void put(K chave, V valor);

    /**
     * Retorna o valor associado à chave informada.
     *
     * @param chave a chave de busca
     * @return o valor correspondente, ou {@code null} se não encontrado
     */
    V get(K chave);

    /**
     * Remove o par chave-valor da tabela.
     *
     * @param chave a chave a ser removida
     * @return {@code true} se removido com sucesso, {@code false} se não encontrado
     */
    boolean remove(K chave);

    /**
     * Verifica se uma chave está presente na tabela.
     *
     * @param chave a chave a ser verificada
     * @return {@code true} se encontrada, {@code false} caso contrário
     */
    boolean containsKey(K chave);

    /**
     * Retorna o número de pares chave-valor armazenados.
     *
     * @return quantidade de entradas na tabela
     */
    int size();
}