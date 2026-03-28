package structures;

import model.Artista;
import model.Musica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para a Tabela Hash de Artistas.
 * 
 * primeiro teste: Inserção e busca de artistas
 * segundo teste: Busca de músicas por artista
 * terceiro teste: Busca parcial por nome
 */
class HashArtistasTest {

    private HashArtistas hash;
    private Musica musica1;
    private Musica musica2;
    private Artista artista1;

    @BeforeEach
    void setUp() {
        hash = new HashArtistas(10);
        musica1 = new Musica("Numb", "Linkin Park", "Rock", 207);
        musica2 = new Musica("In the End", "Linkin Park", "Rock", 216);
        artista1 = new Artista("Linkin Park");
    }

    //1: insere o nome do artista na hash e busca por ele
    //resultado ok = true'
    @Test
    void testInserirArtistaEBuscar() {
        hash.insert(artista1);
        Artista encontrado = hash.search("Linkin Park");
        assertNotNull(encontrado, "Artista deveria ser encontrado");
        assertEquals("Linkin Park", encontrado.getNome());
    }

    //2: busca por um artista que não ta na hash
    //resultado ok = null
    @Test
    void testBuscarArtistaInexistente() {
        Artista encontrado = hash.search("Inexistente");
        assertNull(encontrado, "Artista inexistente não deve ser encontrado");
    }

    //3: insere duas musicas do mesmo artista, 
    // depois busca por ele e espera encontrar as musicas
    //resultado ok = lista com as musicas do artista
    @Test
    void testInserirMusicaEBuscarPorArtista() {
        hash.inserir(musica1);
        hash.inserir(musica2);
        
        var musicas = hash.buscarMusicas("Linkin Park");
        
        assertNotNull(musicas, "Deveria encontrar músicas");
        assertEquals(2, musicas.size());
    }

    //4: insere uma musica, depois busca por 
    // apenas uma parte do nome do artista
    //resultado ok = lista com a musica do artista
    @Test
    void testBuscarPorNomeParcial() {
        hash.inserir(musica1);
        
        var resultados = hash.buscarMusicasPorNomeParcial("Linkin");
        assertEquals(1, resultados.size());
        assertEquals("Numb", resultados.get(0).getTitulo());
    }
}