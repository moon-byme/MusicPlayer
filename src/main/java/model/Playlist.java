package model;

import structures.No;

/**
 * Playlist unificada: metadados + lista de músicas.
 * Versão simples que junta as duas implementações.
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

    public Playlist(String nome) {
        this(nome, "");
    }

    // ==================== GETTERS ====================

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getTamanho() {
        return tamanho;
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }

    // ==================== MÉTODOS DE MÚSICAS ====================

    /**
     * Adiciona música ao final da playlist.
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
     * Remove música pelo título.
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
     * Retorna a primeira música (sem remover).
     */
    public Musica peekPrimeira() {
        return (inicio != null) ? inicio.getDado() : null;
    }

    /**
     * Remove e retorna a primeira música.
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
     * Verifica se contém uma música pelo título.
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
     * Exibe a playlist formatada.
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
