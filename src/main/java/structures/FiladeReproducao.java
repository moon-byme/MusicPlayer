package structures;

import java.util.ArrayList;
import java.util.List;
import model.Musica;

public class FiladeReproducao {
    private Pilha<Musica> p1 = new Pilha<>();
    private Pilha<Musica> p2 = new Pilha<>();
    private int tamanho = 0; // ADICIONADO: controla o tamanho da fila

    public void enqueue(Musica m) {
        p1.push(m);
        tamanho++; // ADICIONADO
    }

    public Musica dequeue() {
        if (p2.isEmpty()) {
            while (!p1.isEmpty()) {
                p2.push(p1.pop());
            }
        }

        if (p2.isEmpty()) {
            return null;
        }

        tamanho--; // ADICIONADO
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
            Musica m = dequeue();
            System.out.println(posicao + ". " + m.getTitulo());
            temp.enqueue(m);
            posicao++;
        }

        // Restaura a fila original
        while (!temp.isEmpty()) {
            enqueue(temp.dequeue());
        }
    }

    /**
     * Retorna todas as músicas da fila em uma List sem destruí-la.
     */
    public List<Musica> toList() {
        List<Musica> lista = new ArrayList<>();
        FiladeReproducao temp = new FiladeReproducao();
        while (!isEmpty()) {
            Musica m = dequeue();
            lista.add(m);
            temp.enqueue(m);
        }
        while (!temp.isEmpty()) {
            enqueue(temp.dequeue());
        }
        return lista;
    }

}
