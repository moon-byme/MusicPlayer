package structures;

import interfaces.IArvore;
import model.Musica;
import java.util.ArrayList;
import java.util.List;

/**
 * Árvore AVL autobalanceável para organizar músicas em ordem alfabética por
 * título.
 *
 * <p>
 * Garante altura O(log n) após cada inserção ou remoção via rotações simples
 * (LL, RR) e duplas (LR, RL). Chave de ordenação: título e, em caso de empate,
 * artista
 * (ambos case-insensitive).
 * </p>
 *
 * @author Isabelle
 * @see interfaces.IArvore
 * @see model.Musica
 */
public class ArvoreAVL implements IArvore<Musica> {

    private NoAVL raiz; // nosso root
    private int tamanho; // quantidade de músicas

    private class NoAVL {
        Musica musica; // dado usado: música
        NoAVL esquerda; // o filho esquerdo seria para músicas com título menor, como 21 da gracie
        NoAVL direita; // o filho direito seria para músicas com titulo maior, como ballad.. da olivia
        int altura; // =1 pra folha

        NoAVL(Musica musica) {
            this.musica = musica;
            this.altura = 1; // folha
        }
    }

    // criando arvore vazia:
    public ArvoreAVL() {
        this.raiz = null;
        this.tamanho = 0;
    }

    // MÉTODOS AUXILIARES ABAIXO :)

    private int altura(NoAVL no) {
        return no == null ? 0 : no.altura;
        // Se o nó=null retorna 0, se não a altura dele
    }

    private void atualizarAltura(NoAVL no) {
        if (no != null) {
            no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));
            // a altura de um nó é baseado na altura de um dos filhos
        }
    }

    private int fatorBalanceamento(NoAVL no) {
        return no == null ? 0 : altura(no.esquerda) - altura(no.direita);
    }

    // rotação simples a direita
    private NoAVL rotacaoDireita(NoAVL y) {
        NoAVL x = y.esquerda;
        NoAVL T2 = x.direita;

        x.direita = y;
        y.esquerda = T2;

        atualizarAltura(y);
        atualizarAltura(x);

        return x;
    }

    // rotação simples a esquerda
    private NoAVL rotacaoEsquerda(NoAVL x) {
        NoAVL y = x.direita;
        NoAVL T2 = y.esquerda;

        y.esquerda = x;
        x.direita = T2;

        atualizarAltura(x);
        atualizarAltura(y);

        return y;
    }

    private NoAVL balancear(NoAVL no) {
        if (no == null)
            return null;

        atualizarAltura(no);
        int fb = fatorBalanceamento(no);

        // Caso LL: filho da esquerda mais pesado
        if (fb > 1 && fatorBalanceamento(no.esquerda) >= 0)
            return rotacaoDireita(no);

        // Caso LR: joelhinho apontando para a direita (<)
        if (fb > 1 && fatorBalanceamento(no.esquerda) < 0) {
            no.esquerda = rotacaoEsquerda(no.esquerda);
            return rotacaoDireita(no);
        }

        // Caso RR: filho da direita mais pesado
        if (fb < -1 && fatorBalanceamento(no.direita) <= 0)
            return rotacaoEsquerda(no);

        // Caso RL: joelhinho apontando para a esquerda (>)
        if (fb < -1 && fatorBalanceamento(no.direita) > 0) {
            no.direita = rotacaoDireita(no.direita);
            return rotacaoEsquerda(no);
        }

        return no;
    }

    // METODOS DE INSERÇÃO ABAIXO :)

    /**
     * Insere uma música na árvore. Músicas com mesmo título e artista são
     * ignoradas.
     * Rebalanceia automaticamente após a inserção — O(log n).
     *
     * @param musica música a ser inserida; ignorada se {@code null}
     */
    @Override
    public void inserir(Musica musica) {
        if (musica == null)
            return;
        raiz = inserirRecursivo(raiz, musica);
    }

    private NoAVL inserirRecursivo(NoAVL atual, Musica musica) {
        if (atual == null) {// posição de inserção vazia
            tamanho++;
            return new NoAVL(musica);
        }

        // compara pelo título da música primeiro
        int cmp = musica.getTitulo().compareToIgnoreCase(atual.musica.getTitulo());

        // se títulos iguais, compara pelo artista
        if (cmp == 0) {
            cmp = musica.getArtista().compareToIgnoreCase(atual.musica.getArtista());
        }

        if (cmp < 0)
            atual.esquerda = inserirRecursivo(atual.esquerda, musica);
        else if (cmp > 0)
            atual.direita = inserirRecursivo(atual.direita, musica);
        else
            return atual; // música exata (título + artista) já existe

        return balancear(atual);
    }

    // METODOS DE BUSCA ABAIXO (O(logn)) :)

    /**
     * Verifica se a música existe na árvore (busca por título + artista) — O(log
     * n).
     *
     * @param musica música a procurar
     * @return {@code true} se encontrada
     */
    @Override
    public boolean buscar(Musica musica) {
        return buscarRecursivo(raiz, musica);
    }

    private boolean buscarRecursivo(NoAVL atual, Musica musica) {
        if (atual == null)
            return false;

        int cmp = musica.getTitulo().compareToIgnoreCase(atual.musica.getTitulo());

        // se títulos iguais, compara pelo artista
        if (cmp == 0) {
            cmp = musica.getArtista().compareToIgnoreCase(atual.musica.getArtista());
        }

        if (cmp < 0)
            return buscarRecursivo(atual.esquerda, musica);
        else if (cmp > 0)
            return buscarRecursivo(atual.direita, musica);
        else
            return true;
    }

    /**
     * Busca música pelo título (para usar no reprodutor), BEM MAIS EASY
     */
    public Musica buscarPorTitulo(String titulo) {
        return buscarPorTituloRecursivo(raiz, titulo);
    }

    private Musica buscarPorTituloRecursivo(NoAVL atual, String titulo) {
        if (atual == null)
            return null;

        int cmp = titulo.compareToIgnoreCase(atual.musica.getTitulo());

        if (cmp < 0)
            return buscarPorTituloRecursivo(atual.esquerda, titulo);
        else if (cmp > 0)
            return buscarPorTituloRecursivo(atual.direita, titulo);
        else
            return atual.musica;
    }

    // MÉTODOS DE REMOVER ABAIXO(O(log n)) :)

    /**
     * Remove a música da árvore e rebalanceia — O(log n).
     * Se a música possuir dois filhos, usa o sucessor in-order (mínimo da subárvore
     * direita).
     *
     * @param musica música a remover; ignorada se {@code null}
     */
    @Override
    public void remover(Musica musica) {
        if (musica == null)
            return;
        raiz = removerRecursivo(raiz, musica);
    }

    private NoAVL removerRecursivo(NoAVL atual, Musica musica) {
        if (atual == null)
            return null;

        int cmp = musica.getTitulo().compareToIgnoreCase(atual.musica.getTitulo());

        // se títulos iguais, compara pelo artista
        if (cmp == 0) {
            cmp = musica.getArtista().compareToIgnoreCase(atual.musica.getArtista());
        }

        if (cmp < 0)
            atual.esquerda = removerRecursivo(atual.esquerda, musica);
        else if (cmp > 0)
            atual.direita = removerRecursivo(atual.direita, musica);
        else {
            tamanho--;

            // Nó sendo folha ou tem apenas um filho:
            if (atual.esquerda == null)
                return atual.direita;
            else if (atual.direita == null)
                return atual.esquerda;

            // Nó com dois filhos:
            NoAVL sucessor = encontrarMinimo(atual.direita);// menor da sub-arvore direita
            atual.musica = sucessor.musica;
            atual.direita = removerRecursivo(atual.direita, sucessor.musica);
        }

        return balancear(atual);
    }

    // menor nó:
    private NoAVL encontrarMinimo(NoAVL no) {
        while (no.esquerda != null)
            no = no.esquerda;
        return no;
    }

    /**
     * Retorna todas as músicas em percurso in-order (esquerda → raiz → direita),
     * produzindo uma lista em ordem alfabética por título — O(n).
     *
     * @return lista ordenada de músicas
     */
    @Override
    public List<Musica> inOrder() {
        List<Musica> lista = new ArrayList<>();
        inOrderRecursivo(raiz, lista);
        return lista;
    }

    private void inOrderRecursivo(NoAVL no, List<Musica> lista) {
        if (no != null) {
            inOrderRecursivo(no.esquerda, lista);
            lista.add(no.musica);
            inOrderRecursivo(no.direita, lista);
        }
    }

    // os auxiliares gerais:

    @Override
    public boolean isEmpty() {
        return raiz == null;
    }

    public int size() {
        return tamanho;
    }

    public int getAltura() {
        return altura(raiz);
    }
}
