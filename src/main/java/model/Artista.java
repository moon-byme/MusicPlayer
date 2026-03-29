package model;

/**
 * Representa um artista musical no sistema.
 *
 * Classe imutável de domínio — uma vez criado, o nome do artista não pode ser
 * alterado.
 * A {@link structures.HashArtistas} usa {@code String} como chave (e não
 * {@code Artista}),
 * evitando dependência de {@code hashCode()} desta classe para o funcionamento
 * da hash.
 *
 * @author Lethycia
 * @author Carla Nascimento
 */
public final class Artista {

    private final String nome;

    /**
     * Cria um novo artista com o nome informado.
     *
     * @param nome nome do artista; não pode ser nulo ou vazio
     * @throws IllegalArgumentException se o nome for nulo ou vazio
     */
    public Artista(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do artista não pode ser nulo ou vazio");
        }
        this.nome = nome.trim();
    }

    /**
     * Retorna o nome do artista.
     *
     * @return nome do artista
     */
    public String getNome() {
        return nome;
    }

    /**
     * Dois artistas são iguais se tiverem o mesmo nome (case-sensitive).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Artista))
            return false;
        Artista artista = (Artista) o;
        return nome.equals(artista.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }
}
