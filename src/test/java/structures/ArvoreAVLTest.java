package structures;

import model.Musica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para a Árvore AVL.
 * 
 * 
 * primeiro teste: Inserção e busca de músicas
 * segundo teste: Inserir música duplicada (não deve inserir)
 * terceiro teste: Buscar música por título
 * quarto teste: Buscar título inexistente (retorna null)
 * quinto teste: Percurso in-order (ordem alfabética)
 * sexto teste: Verificar se árvore está vazia
 * sétimo teste: Remover música existente
 * oitavo teste: Altura balanceada após 100 inserções (O(log n))
 *
 * @author Isabelle
 */

class ArvoreAVLTest {

    private ArvoreAVL arvore;
    private Musica musicaA;
    private Musica musicaB;
    private Musica musicaC;

    // criando uma arvore vazia pra fazer os testes:
    @BeforeEach
    void setUp() {
        arvore = new ArvoreAVL();
        musicaA = new Musica("A", "Artista A", "Rock", 180);
        musicaB = new Musica("B", "Artista B", "Pop", 200);
        musicaC = new Musica("C", "Artista C", "MPB", 150);
    }

    // 1: insere nome da musica e busca
    // resultado ok = true
    @Test
    void testInserirEBuscar() {
        arvore.inserir(musicaA);
        assertTrue(arvore.buscar(musicaA), "Música deveria ser encontrada após inserção");
    }

    // 2: inserir mesma musica >=2,
    // resultado ok = tamanho da arvore igual
    @Test
    void testInserirDuplicado() {
        arvore.inserir(musicaA);
        int tamanhoAntes = arvore.size();
        arvore.inserir(musicaA);
        assertEquals(tamanhoAntes, arvore.size(), "Música duplicada não deve ser inserida");
    }

    // 3:busca o nome e encontra corretamente
    // resultado ok = musica encontrada pelo titulo
    @Test
    void testBuscarPorTitulo() {
        arvore.inserir(musicaB);
        Musica encontrada = arvore.buscarPorTitulo("B");
        assertNotNull(encontrada, "Música deveria ser encontrada pelo título");
        assertEquals("B", encontrada.getTitulo());
    }

    // 4:busca nome de musica que não existe
    // resultado ok = null
    @Test
    void testBuscarPorTituloInexistente() {
        Musica encontrada = arvore.buscarPorTitulo("Inexistente");
        assertNull(encontrada, "Música inexistente não deve ser encontrada");
    }

    // 5: percurdo in-order retorna as musicas em ordem alfabetica
    // resultado ok = lista ordenada por titulo
    @Test
    void testInOrder() {
        arvore.inserir(musicaC); // C
        arvore.inserir(musicaA); // A
        arvore.inserir(musicaB); // B

        var lista = arvore.inOrder();

        assertEquals("A", lista.get(0).getTitulo());
        assertEquals("B", lista.get(1).getTitulo());
        assertEquals("C", lista.get(2).getTitulo());
    }

    // 6: arvore nova que antes tava vazia,
    // depois de inserir uma musica deve estar cheia
    // resultado ok = true no vazio, depois false
    @Test
    void testIsEmpty() {
        assertTrue(arvore.isEmpty(), "Árvore nova deve estar vazia");
        arvore.inserir(musicaA);
        assertFalse(arvore.isEmpty(), "Árvore com música não deve estar vazia");
    }

    // 7: remove uma musica que existe, busca e não encontra
    // resultado ok = false
    @Test
    void testRemover() {
        arvore.inserir(musicaA);
        arvore.inserir(musicaB);

        arvore.remover(musicaA);

        assertFalse(arvore.buscar(musicaA), "Música removida não deve ser encontrada");
        assertTrue(arvore.buscar(musicaB), "Outras músicas devem permanecer");
    }

    // 8: inserir 100 musicas e verificar se a altura é pequena (O(log n))
    // resultado ok = altura < 15 (pois log2(100) ≈ 7, então 2*7+1 = 15)
    @Test
    void testArvoreBalanceada() {
        for (int i = 0; i < 100; i++) {
            Musica m = new Musica("M" + i, "Artista", "Rock", 180);
            arvore.inserir(m);
        }

        int altura = arvore.getAltura();
        System.out.println("Altura da árvore com 100 elementos: " + altura);

        assertTrue(altura < 15, "Altura " + altura + " é aceitável para 100 elementos");
    }
}