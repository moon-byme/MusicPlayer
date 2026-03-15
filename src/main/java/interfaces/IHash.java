package interfaces;

/**
 * Interface para Tabela Hash com Chaining.
 * Implementada por: (a definir)
 * Uso no sistema (minha sugestão): busca rápida por artista ou gênero em O(1)
 */
public interface IHash<K, V> {
    void put(K chave, V valor);

    V get(K chave);

    boolean remove(K chave);

    boolean containsKey(K chave);

    int size();
}