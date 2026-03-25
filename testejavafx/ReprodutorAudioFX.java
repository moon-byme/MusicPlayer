package testejavafx;

import Artistas.Musicas;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/**
 * 🎵 REPRODUTOR DE ÁUDIO COM JAVAFX
 * 
 * Toca MP3 de verdade! Se não conseguir, usa modo simulação.
 * 
 * @author Isabelle
 */
public class ReprodutorAudioFX {
    
    private MediaPlayer mediaPlayer;
    private Musicas musicaAtual;
    private boolean tocando;
    private String caminhoBase;
    private boolean inicializado = false;
    
    public ReprodutorAudioFX() {
        try {
            new JFXPanel();  // inicia o JavaFX
            inicializado = true;
            System.out.println("✅ JavaFX inicializado!");
        } catch (Exception e) {
            System.out.println("⚠️ JavaFX não inicializado. Usando modo simulação.");
            inicializado = false;
        }
        
        this.tocando = false;
        
        // procura a pasta das músicas
        String userHome = System.getProperty("user.home");
        String[] caminhos = {
            userHome + "/Downloads/Músicas do Projeto/Músicas do Projeto",
            "C:/Users/" + System.getProperty("user.name") + "/Downloads/Músicas do Projeto/Músicas do Projeto",
            "./Músicas do Projeto/Músicas do Projeto"
        };
        
        for (String caminho : caminhos) {
            File teste = new File(caminho);
            if (teste.exists() && teste.isDirectory()) {
                this.caminhoBase = caminho;
                System.out.println("📁 Pasta de músicas: " + caminho);
                break;
            }
        }
        
        if (caminhoBase == null) {
            System.out.println("⚠️ Pasta de músicas não encontrada! Usando modo simulação.");
        }
    }
    
    /**
     * TOCA UMA MÚSICA 🎵
     * Se tiver JavaFX e o arquivo existir, toca de verdade.
     * Senão, simula a reprodução.
     */
    public boolean tocarMusica(Musicas musica) {
        if (musica == null) return false;
        
        // se não tem JavaFX ou não achou a pasta, simula
        if (!inicializado || caminhoBase == null) {
            return tocarSimulado(musica);
        }
        
        try {
            parar();
            
            // monta o caminho do arquivo
            String caminhoArquivo = caminhoBase + "/" + musica.getArtista() + "/" + musica.getNome() + ".mp3";
            File arquivo = new File(caminhoArquivo);
            
            // tenta com .MP3 maiúsculo
            if (!arquivo.exists()) {
                caminhoArquivo = caminhoBase + "/" + musica.getArtista() + "/" + musica.getNome() + ".MP3";
                arquivo = new File(caminhoArquivo);
            }
            
            // se não achou, simula
            if (!arquivo.exists()) {
                System.out.println("🎵 (SIMULAÇÃO) " + musica.getNome() + " - " + musica.getArtista());
                return tocarSimulado(musica);
            }
            
            // toca de verdade!
            Media media = new Media(arquivo.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            
            mediaPlayer.setOnReady(() -> {
                System.out.println("\n🎵 TOCANDO: " + musica.getNome());
                System.out.println("   Artista: " + musica.getArtista());
            });
            
            mediaPlayer.setOnEndOfMedia(() -> {
                System.out.println("\n✓ Música terminada!");
                parar();
            });
            
            mediaPlayer.play();
            this.musicaAtual = musica;
            this.tocando = true;
            
            return true;
            
        } catch (Exception e) {
            return tocarSimulado(musica);
        }
    }
    
    /**
     * MODO SIMULAÇÃO (quando não tem som real)
     */
    private boolean tocarSimulado(Musicas musica) {
        System.out.println("\n🎵 (SIMULAÇÃO) TOCANDO: " + musica.getNome());
        System.out.println("   Artista: " + musica.getArtista());
        System.out.println("   💡 Dica: Coloque os arquivos MP3 na pasta Downloads/Músicas do Projeto/");
        
        this.musicaAtual = musica;
        this.tocando = true;
        
        // simula a duração
        new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(500);
                    if (!tocando) break;
                }
                if (tocando) {
                    System.out.println("\n✓ (Simulação) Música terminada!");
                    tocando = false;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        return true;
    }
    
    /**
     * PARA A MÚSICA ⏹
     */
    public void parar() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        this.tocando = false;
    }
    
    /**
     * ESPERA A MÚSICA TERMINAR
     */
    public void aguardarTermino() {
        if (mediaPlayer != null) {
            while (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } else {
            while (tocando) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
    
    /**
     * VERIFICA SE ESTÁ TOCANDO
     */
    public boolean isTocando() {
        return tocando;
    }
    
    /**
     * PEGA A MÚSICA ATUAL
     */
    public Musicas getMusicaAtual() {
        return musicaAtual;
    }
}