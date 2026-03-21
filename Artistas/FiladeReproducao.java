package Artistas;

public class FiladeReproducao {
	private Pilha<Musicas> p1 = new Pilha<>(); // Entrada
    private Pilha<Musicas> p2 = new Pilha<>();
    
    	public void enqueue(Musicas m) {
            p1.push(m);
        }

        public Musicas dequeue() {
            if (p2.isEmpty()) {
                while (!p1.isEmpty()) {
                    p2.push(p1.pop());
                }
            }
            
            if (p2.isEmpty()) {
                return null;
            }
            
            return p2.pop();
        }
        
        // no main n esquecer que assim que a musica sair da fila e for tocada precisa entrar na pilha de historico

}
