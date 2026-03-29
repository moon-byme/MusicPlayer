package model;

import structures.No;

/**
 * Representa uma playlist de músicas.
 *
 * Armazena metadados (nome e descrição) e uma lista encadeada de músicas
 * implementada com {@link structures.No}. Suporta adição no final, remoção
 * por título e consumo sequencial (dequeue) das músicas.
 *
 * Duas playlists são consideradas iguais se tiverem o mesmo nome.
 *
 * @author Lethycia
 */
public class Playlist {

    // Metadados (da versão nova)
    private final String nome;
    private final String descricao;

    // Lista encadeada (da versão antiga)
    private No<Musica> inicio;
    private No<Musica> fim;
    private int tamanho;

    // ==================== CONSTRUTORES ====================

    /**
     * Cria uma playlist com nome e descrição.
     *
     * @param nome      nome da playlist; não pode ser nulo ou vazio
     * @param descricao descrição da playlist; pode ser nula (será string vazia)
     * @throws IllegalArgumentException se o nome for nulo ou vazio
     */
    public Playlist(String nome, String descricao) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da playlist não pode ser nulo ou vazio");
        }
        this.nome = nome.trim();
        this.descricao = (descricao != null) ? descricao.trim() : "";
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    /**
     * Cria uma playlist apenas com nome, sem descrição.
     *
     * @param nome nome da playlist
     */
    public Playlist(String nome) {
        this(nome, "");
    }

    /**
     * Retorna o nome da playlist.
     *
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a descrição da playlist.
     *
     * @return descrição, ou string vazia se não informada
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna o número de músicas na playlist.
     *
     * @return quantidade de músicas
     */
    public int getTamanho() {
        return tamanho;
    }

    /**
     * Verifica se a playlist está vazia.
     *
     * @return {@code true} se não houver músicas
     */
    public boolean isEmpty() {
        return tamanho == 0;
    }

    // ==================== MÉTODOS DE MÚSICAS ====================

    /**
     * Adiciona uma música ao final da playlist.
     *
     * @param musica a música a ser adicionada; ignorada se {@code null}
     */
    public void adicionarMusica(Musica musica) {
        if (musica == null)
            return;

        No<Musica> novoNo = new No<>(musica);

        if (inicio == null) {
            inicio = novoNo;
            fim = novoNo;
        } else {
            fim.setProximo(novoNo);
            fim = novoNo;
        }

        tamanho++;
    }

    /**
     * Remove a primeira música cujo título seja igual ao informado
     * (case-insensitive).
     *
     * @param titulo título da música a remover
     * @return {@code true} se removida, {@code false} se não encontrada
     */
    public boolean removerMusica(String titulo) {
        if (inicio == null)
            return false;

        // Caso especial: primeiro elemento
        if (inicio.getDado().getTitulo().equalsIgnoreCase(titulo)) {
            inicio = inicio.getProximo();
            if (inicio == null) {
                fim = null;
            }
            tamanho--;
            return true;
        }

        // Buscar e remover
        No<Musica> atual = inicio;
        No<Musica> anterior = null;

        while (atual != null && !atual.getDado().getTitulo().equalsIgnoreCase(titulo)) {
            anterior = atual;
            atual = atual.getProximo();
        }

        if (atual != null) {
            anterior.setProximo(atual.getProximo());
            if (atual == fim) {
                fim = anterior;
            }
            tamanho--;
            return true;
        }

        return false;
    }

    /**
     * Retorna a primeira música da playlist sem removê-la.
     *
     * @return primeira música, ou {@code null} se a playlist estiver vazia
     */
    public Musica peekPrimeira() {
        return (inicio != null) ? inicio.getDado() : null;
    }

    /**
     * Remove e retorna a primeira música da playlist.
     *
     * @return primeira música, ou {@code null} se a playlist estiver vazia
     */
    public Musica dequeuePrimeira() {
        if (inicio == null)
            return null;

        Musica musica = inicio.getDado();
        inicio = inicio.getProximo();

        if (inicio == null) {
            fim = null;
        }

        tamanho--;
        return musica;
    }

    /**
     * Verifica se a playlist contém uma música com o título informado
     * (case-insensitive).
     *
     * @param titulo título a buscar
     * @return {@code true} se encontrada, {@code false} caso contrário
     */
    public boolean contemMusica(String titulo) {
        No<Musica> atual = inicio;
        while (atual != null) {
            if (atual.getDado().getTitulo().equalsIgnoreCase(titulo)) {
                return true;
            }
            atual = atual.getProximo();
        }
        return false;
    }

    // ==================== EXIBIÇÃO ====================

    /**
     * Exibe todas as músicas da playlist formatadas no console.
     */
    public void exibir() {
        System.out.println("\n===== PLAYLIST: " + nome.toUpperCase() + " =====");

        if (!descricao.isEmpty()) {
            System.out.println("Descrição: " + descricao);
        }

        if (inicio == null) {
            System.out.println("Playlist vazia.");
            return;
        }

        No<Musica> atual = inicio;
        int posicao = 1;

        while (atual != null) {
            Musica m = atual.getDado();
            System.out.println(posicao + ". " + m.getTitulo() + " - " + m.getArtista() +
                    " (" + m.getDuracaoFormatada() + ")");
            atual = atual.getProximo();
            posicao++;
        }

        System.out.println("Total: " + tamanho + " músicas");
    }

    // ==================== EQUALS, HASHCODE, TOSTRING ====================

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Playlist))
            return false;
        Playlist playlist = (Playlist) o;
        return nome.equals(playlist.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Playlist: %s (%d músicas)", nome, tamanho);
    }
}
