package structures;

/**
 * Pilha genérica (LIFO) implementada com lista encadeada de {@link No nós}.
 *
 * <p>
 * Utilizada como estrutura base do {@link HistoricoMusicas} e internamente
 * pela {@link FiladeReproducao} (algoritmo de duas pilhas).
 * </p>
 *
 * @param <T> tipo dos elementos armazenados
 * @author Carla Nascimento
 */
public class Pilha<T> {
	private No<T> topo;

	/**
	 * Empilha um elemento no topo.
	 *
	 * @param dado elemento a ser inserido
	 */
	public void push(T dado) {
		No<T> novoNo = new No<>(dado);
		novoNo.setProximo(topo);
		topo = novoNo;
	}

	/**
	 * Remove e retorna o elemento do topo.
	 *
	 * @return elemento do topo, ou {@code null} se a pilha estiver vazia
	 */
	public T pop() {
		if (topo == null)
			return null;

		T topoRemovido = topo.getDado();
		topo = topo.getProximo();
		return topoRemovido;
	}

	/**
	 * Exibe todos os elementos da pilha do topo para a base,
	 * precedidos pelo título informado.
	 *
	 * @param titulo cabeçalho exibido antes dos elementos
	 */
	public void exibir(String titulo) {
		System.out.println("\n==== " + titulo + " ====");
		No<T> atual = topo;

		if (atual == null)
			System.out.println("\nHistorico Vázio.");

		while (atual != null) {
			System.out.println("-> " + atual.getDado());
			atual = atual.getProximo();
		}
	}

	/**
	 * Verifica se a pilha está vazia.
	 *
	 * @return {@code true} se não houver elementos
	 */
	public boolean isEmpty() {
		return topo == null;
	}
}
