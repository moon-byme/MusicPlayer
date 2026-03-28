package structures;

import model.Musica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para a Pilha (LIFO com lista encadeada).
 * 
 * primeiro teste: Push adiciona no topo
 * segundo teste: Pop remove do topo (LIFO)
 */
class PilhaTest {

    private Pilha<Musica> pilha;
    private Musica musica1;
    private Musica musica2;

    @BeforeEach
    void setUp() {
        pilha = new Pilha<>();
        musica1 = new Musica("Música 1", "Artista", "Rock", 180);
        musica2 = new Musica("Música 2", "Artista", "Pop", 200);
    }

    
    //1: push adiciona no topo(Firt in), pop remove do topo (last out)
    //resultado ok = a saida é igual a entrada, mas invertida
    @Test
    void testLIFO() {
        pilha.push(musica1);
        pilha.push(musica2);
        
        assertEquals(musica2, pilha.pop(), "Última a entrar deve ser primeira a sair");
        assertEquals(musica1, pilha.pop(), "Primeira a entrar");
    }

    //2: Pop em pilha vazia retorna null
    //resultado ok = null
    @Test
    void testPopPilhaVazia() {
        assertNull(pilha.pop(), "Pop em pilha vazia deve retornar null");
    }

    //3: isEmpty funciona?
    //cria pilha nova, verifica se ta vazia, depois adiciona uma musica, verifica se ta vazia de novo
    //resultado ok = true para pilha vazia, false para pilha com elementos
    @Test
    void testIsEmpty() {
        assertTrue(pilha.isEmpty());
        pilha.push(musica1);
        assertFalse(pilha.isEmpty());
        pilha.pop();
        assertTrue(pilha.isEmpty());
    }
}