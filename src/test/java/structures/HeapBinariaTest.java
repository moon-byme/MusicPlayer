package structures;

import model.Musica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para a Heap Binária (Max-Heap).
 * 
 * primeiro teste: Inserção mantém a propriedade de max-heap
 * segundo teste: Extração do máximo retorna o elemento correto
 * terceiro teste: Peek olha o topo sem remover
 */
class HeapBinariaTest {

    private HeapBinaria heap;
    private Musica musica1Play;
    private Musica musica2Plays;
    private Musica musica5Plays;

    @BeforeEach
    void setUp() {
        heap = new HeapBinaria();
        
        musica1Play = new Musica("1Play", "Artista", "Rock", 180);
        musica1Play.incrementarPlays(); // 1 play
        
        musica2Plays = new Musica("2Plays", "Artista", "Pop", 200);
        musica2Plays.incrementarPlays();
        musica2Plays.incrementarPlays(); // 2 plays
        
        musica5Plays = new Musica("5Plays", "Artista", "MPB", 150);
        for (int i = 0; i < 5; i++) {
            musica5Plays.incrementarPlays(); // 5 plays
        }
    }

    //1: insere as musicas na heap e extrai o maximo
    //resultado ok = a musica com mais plays é a primeira a sair
    @Test
    void testInserirEExtrairMax() {
        heap.inserir(musica1Play);
        heap.inserir(musica2Plays);
        heap.inserir(musica5Plays);
        
        assertEquals(musica5Plays, heap.extrairMax(), "Mais tocada (5 plays) deve sair primeiro");
        assertEquals(musica2Plays, heap.extrairMax(), "Segunda mais tocada (2 plays)");
        assertEquals(musica1Play, heap.extrairMax(), "Menos tocada (1 play)");
    }

    
    //2: insere as musicas na heap e usa peek para verificar o topo
    //resultado ok = peek retorna a musica mais tocada, e a heap continua com os
    @Test
    void testPeek() {
        heap.inserir(musica1Play);
        heap.inserir(musica5Plays);
        
        assertEquals(musica5Plays, heap.peek(), "Peek deve retornar a mais tocada");
        assertEquals(2, heap.size(), "Peek não deve remover elementos");
    }

   
    //3: tenta extrair max de heap vazia
    //resultado ok = lança exceção
    @Test
    void testExtrairMaxHeapVazia() {
        assertThrows(java.util.NoSuchElementException.class, () -> {
            heap.extrairMax();
        }, "Extrair de heap vazia deve lançar exceção");
    }

    
    //4: insere uma musica sem plays e verifica se ela ta no topo
    //resultado ok = a musica sem plays é a primeira a sair
    @Test
    void testInserirMusicaSemPlays() {
        Musica m = new Musica("Zero", "Artista", "Rock", 180);
        heap.inserir(m);
        
        assertEquals(m, heap.peek());
        assertEquals(1, heap.size());
    }

   

    //5: insere musicas com plays 3, 1, 5, 2, 4 e extrai max
    //resultado ok = a ordem de saída é 5, 4, 3, 2, 1
    @Test
    void testOrdemDecrescente() {
        // Insere músicas com plays: 3, 1, 5, 2, 4
        Musica m3 = criarMusicaComPlays("M3", 3);
        Musica m1 = criarMusicaComPlays("M1", 1);
        Musica m5 = criarMusicaComPlays("M5", 5);
        Musica m2 = criarMusicaComPlays("M2", 2);
        Musica m4 = criarMusicaComPlays("M4", 4);
        
        heap.inserir(m3);
        heap.inserir(m1);
        heap.inserir(m5);
        heap.inserir(m2);
        heap.inserir(m4);
        
        assertEquals(5, heap.extrairMax().getPlays());
        assertEquals(4, heap.extrairMax().getPlays());
        assertEquals(3, heap.extrairMax().getPlays());
        assertEquals(2, heap.extrairMax().getPlays());
        assertEquals(1, heap.extrairMax().getPlays());
    }

    private Musica criarMusicaComPlays(String nome, int plays) {
        Musica m = new Musica(nome, "Artista", "Rock", 180);
        for (int i = 0; i < plays; i++) {
            m.incrementarPlays();
        }
        return m;
    }
}