package Artistas;

public class Pilha<T> {
	private No<T> topo;
	
	public void push(T dado) {
		No<T> novoNo = new No<>(dado);
		novoNo.setProximo(topo);
		topo = novoNo;
	}
	
	public T pop() {
		if (topo == null) return null;
		
		T topoRemovido = topo.getDado();
		topo = topo.getProximo();
		return topoRemovido;
	}
	
	public void exibir(String titulo) {
		System.out.println("\n==== " + titulo + " ====");
		System.out.println("\n====== Historico de Artistas ======");
		No<T> atual = topo;
		
		if (atual == null) System.out.println("\nHistorico Vázio.");
		
		while(atual != null) {
			System.out.println("-> " + atual.getDado());
			atual = atual.getProximo();
		}
	}
	
	public boolean isEmpty() {
		return topo == null;
	}
}
