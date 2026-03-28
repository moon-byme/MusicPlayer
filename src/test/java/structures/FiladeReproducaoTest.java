package structures;

import model.Musica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para a Fila de Reprodução 
 * (FIFO com duas pilhas).
 * 
 * primeiro teste:Enqueue adiciona ao final
 * segundo teste: Dequeue remove do início
 * terceiro teste: Ordem FIFO é mantida
 */
class FiladeReproducaoTest {

    private FiladeReproducao fila;
    private Musica musica1;
    private Musica musica2;
    private Musica musica3;

    @BeforeEach
    void setUp() {
        fila = new FiladeReproducao();
        musica1 = new Musica("Música 1", "Artista", "Rock", 180);
        musica2 = new Musica("Música 2", "Artista", "Pop", 200);
        musica3 = new Musica("Música 3", "Artista", "MPB", 150);
    }

   
    //1: enqueue adiciona no final(Firt in), dequeue remove do inicio (first out)
    //resultado ok = a saida é igual a entrada
    @Test
    void testFIFO() {
        fila.enqueue(musica1);
        fila.enqueue(musica2);
        fila.enqueue(musica3);
        
        assertEquals(musica1, fila.dequeue(), "Primeira a entrar deve ser primeira a sair");
        assertEquals(musica2, fila.dequeue(), "Segunda a entrar");
        assertEquals(musica3, fila.dequeue(), "Terceira a entrar");
    }

    
    //1: tenta dequeue em fila vazia
    //resultado ok = null
    @Test
    void testDequeueFilaVazia() {
        assertNull(fila.dequeue(), "Dequeue em fila vazia deve retornar null");
    }

   
    //3: o is empty ta funcionando? 
    //fila vazia antes, adiciona uma musica, depois ta vazia?
    //resultado ok = true, depois false
    @Test
    void testIsEmpty() {
        assertTrue(fila.isEmpty(), "Fila nova deve estar vazia");
        fila.enqueue(musica1);
        assertFalse(fila.isEmpty(), "Fila com música não deve estar vazia");
    }

    //4: o size ta funcionando?
    //resultado ok = 0, depois 1, depois 2, depois 1
    @Test
    void testSize() {
        assertEquals(0, fila.size());
        fila.enqueue(musica1);
        assertEquals(1, fila.size());
        fila.enqueue(musica2);
        assertEquals(2, fila.size());
        fila.dequeue();
        assertEquals(1, fila.size());
    }
}