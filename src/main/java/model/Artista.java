package model;

/**
 * O Artista é objeto de domínio, mas a TabelaHash usa String como chave (não
 * Artista),
 * eliminando risco de hashCode() mal implementado.
 * A classe é imutável, garantindo segurança em coleções e simplicidade de uso.
 * Sugestão de uso: armazenar Artista em uma coleção (ex: ArrayList) para
 * exibição, mas indexar músicas por título na ArvoreAVL usando String.
 * Implementação simples, sem necessidade de comparação ou ordenação — o nome do
 * artista é apenas um atributo descritivo das músicas.
 */
public final class Artista {

    private final String nome;

    public Artista(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do artista não pode ser nulo ou vazio");
        }
        this.nome = nome.trim();
    }

    public String getNome() {
        return nome;
    }

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
