package model;

/**
 * Domínio: metadados de uma playlist.
 * A fila interna de reprodução é gerenciada pelo RepositorioPlaylists
 * (separação de responsabilidades).
 * A classe é imutável, garantindo que o nome e a descrição não possam ser
 * alterados após a criação da playlist.
 */
public final class Playlist {

    private final String nome;
    private final String descricao;

    public Playlist(String nome, String descricao) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da playlist não pode ser nulo ou vazio");
        }
        this.nome = nome.trim();
        this.descricao = (descricao != null) ? descricao.trim() : "";
    }

    public Playlist(String nome) {
        this(nome, "");
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

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
        return String.format("Playlist: %s — %s", nome, descricao.isEmpty() ? "(sem descrição)" : descricao);
    }
}
