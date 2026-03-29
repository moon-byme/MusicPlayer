package model;

import java.util.Objects;

/**
 * Representa uma música no sistema.
 *
 * Classe imutável de domínio, exceto pelo contador de reproduções
 * ({@code plays}).
 * Implementa {@link Comparable} por título e artista (case-insensitive),
 * sendo usada diretamente pela {@link structures.ArvoreAVL} para ordenação e
 * busca em O(log n).
 * O ranking por plays é tratado separadamente pela
 * {@link structures.HeapBinaria}.
 *
 * @author Lethycia
 * @author Carla Nascimento
 */
public final class Musica implements Comparable<Musica> {

    private final String titulo;
    private final String artista;
    private final String genero;
    private final int duracao; // duração em segundos
    private int plays; // contador de reproduções

    /**
     * Cria uma nova música com os dados informados.
     *
     * @param titulo  título da música; não pode ser nulo ou vazio
     * @param artista nome do artista; não pode ser nulo ou vazio
     * @param genero  gênero musical; se nulo, será definido como "Desconhecido"
     * @param duracao duração em segundos; não pode ser negativa
     * @throws IllegalArgumentException se título, artista ou duração forem
     *                                  inválidos
     */
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

    /**
     * Retorna o título da música.
     *
     * @return título
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Retorna o nome do artista.
     *
     * @return artista
     */
    public String getArtista() {
        return artista;
    }

    /**
     * Retorna o gênero musical.
     *
     * @return gênero
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Retorna a duração da música em segundos.
     *
     * @return duração em segundos
     */
    public int getDuracao() {
        return duracao;
    }

    /**
     * Retorna o número de vezes que a música foi reproduzida.
     *
     * @return contador de plays
     */
    public int getPlays() {
        return plays;
    }

    /**
     * Incrementa em 1 o contador de reproduções.
     * Deve ser chamado apenas por {@link reproducer.Reprodutor#marcarComoTocada}.
     */
    public void incrementarPlays() {
        this.plays++;
    }

    /**
     * Retorna a duração formatada como {@code mm:ss}.
     *
     * @return string no formato "03:45"
     */
    public String getDuracaoFormatada() {
        return String.format("%02d:%02d", duracao / 60, duracao % 60);
    }

    /**
     * Ordenação por título e artista (case-insensitive), usada pela
     * {@link structures.ArvoreAVL}.
     * O artista serve como desempate para músicas com mesmo título.
     * Não considera plays — o ranking é responsabilidade da
     * {@link structures.HeapBinaria}.
     *
     * @param outra a música a ser comparada
     * @return negativo, zero ou positivo conforme ordenação alfabética
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
