package structures;

/**
 * Nó genérico para estruturas encadeadas (pilha, fila, hash e histórico).
 *
 * <p>
 * Armazena um dado do tipo {@code T} e uma referência para o próximo nó,
 * permitindo a construção de listas simplesmente encadeadas.
 * </p>
 *
 * @param <T> tipo do dado armazenado no nó
 * @author Carla Nascimento
 */
public class No<T> {
	private T dado;
	private No<T> proximo;

	/**
	 * Cria um novo nó com o dado informado e sem próximo.
	 *
	 * @param dado valor a ser armazenado
	 */
	public No(T dado) {
		this.dado = dado;
		this.proximo = null;
	}

	/**
	 * Retorna o dado armazenado neste nó.
	 *
	 * @return dado do nó
	 */
	public T getDado() {
		return dado;
	}

	/**
	 * Retorna o próximo nó da cadeia.
	 *
	 * @return referência ao próximo nó, ou {@code null} se for o último
	 */
	public No<T> getProximo() {
		return proximo;
	}

	/**
	 * Define o próximo nó da cadeia.
	 *
	 * @param proximo nó que deve vir após este
	 */
	public void setProximo(No<T> proximo) {
		this.proximo = proximo;
	}

}
