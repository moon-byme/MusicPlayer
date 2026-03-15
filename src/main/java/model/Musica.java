package model;

import java.util.Objects;

/**
 * Domínio: representa uma música no sistema.
 * Comparable por título — usado pela ArvoreAVL para indexação e busca em O(log
 * n).
 */
public final class Musica implements Comparable<Musica> {

    private final String titulo;
    private final String artista;
    private final String genero;
    private final int duracao; // duração em segundos
    private int plays; // contador de reproduções

    public Musica(String titulo, String artista, String genero, int duracao) {
        if (titulo == null || titulo.isBlank())
            throw new IllegalArgumentException("Título não pode ser vazio.");
        if (artista == null || artista.isBlank())
            throw new IllegalArgumentException("Artista não pode ser vazio.");
        if (duracao < 0)
            throw new IllegalArgumentException("Duração não pode ser negativa.");

        this.titulo = titulo.trim();
        this.artista = artista.trim();
        this.genero = (genero != null) ? genero.trim() : "Desconhecido";
        this.duracao = duracao;
        this.plays = 0;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public String getGenero() {
        return genero;
    }

    public int getDuracao() {
        return duracao;
    }

    public int getPlays() {
        return plays;
    }

    public void incrementarPlays() {
        this.plays++;
    }

    /** Duração formatada como mm:ss */
    public String getDuracaoFormatada() {
        return String.format("%02d:%02d", duracao / 60, duracao % 60);
    }

    /**
     * Ordenação por título e artista (case-insensitive) — usada pela ArvoreAVL.
     * Inclui artista como desempate para evitar colisões de músicas diferentes com
     * mesmo título.
     * NÃO usa plays: o ranking de plays é feito pela HeapBinaria com Comparator
     * próprio.
     */
    @Override
    public int compareTo(Musica outra) {
        int cmp = this.titulo.compareToIgnoreCase(outra.titulo);
        if (cmp != 0)
            return cmp;
        return this.artista.compareToIgnoreCase(outra.artista);
    }

    /**
     * Compara se este objeto é igual a outro Musica.
     * Dois objetos Musica são iguais se tiverem o mesmo título e artista
     * (case-insensitive).
     * 
     * @param obj objeto a ser comparado
     * @return true se os objetos forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Musica))
            return false;
        Musica outra = (Musica) obj;
        return this.titulo.equalsIgnoreCase(outra.titulo) &&
                this.artista.equalsIgnoreCase(outra.artista);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo.toLowerCase(), artista.toLowerCase());
    }

    @Override
    public String toString() {
        return String.format("%-35s | %-20s | %-12s | %s | plays: %d",
                titulo, artista, genero, getDuracaoFormatada(), plays);
    }
}
