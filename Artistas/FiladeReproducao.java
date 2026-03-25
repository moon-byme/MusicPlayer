package Artistas;

public class FiladeReproducao {
    private Pilha<Musicas> p1 = new Pilha<>();
    private Pilha<Musicas> p2 = new Pilha<>();
    private int tamanho = 0;  // ADICIONADO: controla o tamanho da fila
    
    public void enqueue(Musicas m) {
        p1.push(m);
        tamanho++;  // ADICIONADO
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
        
        tamanho--;  // ADICIONADO
        return p2.pop();
    }
    
   
    /**
     * Verifica se a fila está vazia.
     * Necessário para o Reprodutor saber se há músicas para tocar.
     */
    public boolean isEmpty() {
        return tamanho == 0;
    }
    
    /**
     * Retorna o número de músicas na fila.
     * Útil para exibir estatísticas.
     */
    public int size() {
        return tamanho;
    }
    
    /**
     * Exibe todas as músicas da fila sem destruí-la.
     * Cria uma fila temporária para percorrer os elementos.
     */
    public void exibir() {
        System.out.println("\n===== FILA DE REPRODUÇÃO =====");
        if (isEmpty()) {
            System.out.println("Fila vazia.");
            return;
        }
        
        // Cria fila temporária para exibir
        FiladeReproducao temp = new FiladeReproducao();
        int posicao = 1;
        
        while (!isEmpty()) {
            Musicas m = dequeue();
            System.out.println(posicao + ". " + m.getNome());
            temp.enqueue(m);
            posicao++;
        }
        
        // Restaura a fila original
        while (!temp.isEmpty()) {
            enqueue(temp.dequeue());
        }
    }
    
}
