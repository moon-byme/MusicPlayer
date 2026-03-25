package Artistas;

public class Musicas {
    private String nome;
    private String artista; //adicionei a string de artista  
    private String genero;
    private int duracao;
    private int plays;

    
    public Musicas(String nome, String genero) {
        this.nome = nome;
        this.artista = "Desconhecido";
        this.genero = genero;
        this.duracao = 180;
        this.plays = 0;
    }
    
    // lethycia, aqui foi uma adição para usar no carregador de músicas:
    public Musicas(String nome, String artista, String genero, int duracao) {
        this.nome = nome;
        this.artista = artista;
        this.genero = genero;
        this.duracao = duracao;
        this.plays = 0;
    }
    //termina aqui

    public String getNome() { return nome; }
    public String getArtista() { return artista; }  //adicionei o ngc de artistas
    public String getGenero() { return genero; }
    public int getDuracao() { return duracao; }
    public int getPlays() { return plays; }
    
    
    public void setNome(String nome) { this.nome = nome; }
    public void setArtista(String artista) { this.artista = artista; }
    
    public void incrementarPlays() { this.plays++; }
    
    public String getDuracaoFormatada() {
        return String.format("%02d:%02d", duracao / 60, duracao % 60);
    }
    
    @Override
    public String toString() {
        return nome + " - " + artista + " (" + plays + " plays)";
    }
}
