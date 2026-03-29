package structures;

import model.Musica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Testes para o Histórico de Músicas (pilha LIFO).
 *
 * primeiro teste: Histórico começa vazio
 * segundo teste: Adicionar música aumenta o tamanho
 * terceiro teste: Voltar retorna a música mais recente (LIFO)
 * quarto teste: Voltar em histórico vazio retorna null
 * quinto teste: toList retorna músicas na ordem mais recente primeiro
 * sexto teste: isEmpty retorna false após adicionar
 *
 * @author Lethycia
 */
class HistoricoMusicasTest {

    private HistoricoMusicas historico;
    private Musica musica1;
    private Musica musica2;
    private Musica musica3;

    @BeforeEach
    void setUp() {
        historico = new HistoricoMusicas();
        musica1 = new Musica("Bohemian Rhapsody", "Queen", "Rock", 354);
        musica2 = new Musica("Hotel California", "Eagles", "Rock", 391);
        musica3 = new Musica("Stairway to Heaven", "Led Zeppelin", "Rock", 482);
    }

    @Test
    void historicoComecaVazio() {
        assertTrue(historico.isEmpty());
        assertEquals(0, historico.getTamanho());
    }

    @Test
    void adicionarMusicaAumentaTamanho() {
        historico.adicionar(musica1);
        historico.adicionar(musica2);

        assertFalse(historico.isEmpty());
        assertEquals(2, historico.getTamanho());
    }

    @Test
    void voltarRetornaMusicaMaisRecenteLIFO() {
        historico.adicionar(musica1);
        historico.adicionar(musica2);
        historico.adicionar(musica3);

        assertEquals(musica3, historico.voltar());
        assertEquals(musica2, historico.voltar());
        assertEquals(musica1, historico.voltar());
    }

    @Test
    void voltarEmHistoricoVazioRetornaNull() {
        assertNull(historico.voltar());
    }

    @Test
    void toListRetornaOrdemMaisRecentePrimeiro() {
        historico.adicionar(musica1);
        historico.adicionar(musica2);
        historico.adicionar(musica3);

        List<Musica> lista = historico.toList();

        assertEquals(3, lista.size());
        assertEquals(musica3, lista.get(0));
        assertEquals(musica2, lista.get(1));
        assertEquals(musica1, lista.get(2));
    }

    @Test
    void isEmptyRetornaFalseAposAdicionar() {
        historico.adicionar(musica1);

        assertFalse(historico.isEmpty());
    }
}
