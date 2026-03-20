package Artistas;

public class No<T> {
	private T dado;
	private No<T> proximo;
	
	public No(T dado) {
		this.dado = dado;
		this.proximo = null;
	}

	public T getDado() {
		return dado;
	}

	public No<T> getProximo() {
		return proximo;
	}

	public void setProximo(No<T> proximo) {
		this.proximo = proximo;
	}
	
	
}
