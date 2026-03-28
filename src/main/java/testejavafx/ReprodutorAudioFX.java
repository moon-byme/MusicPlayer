package testejavafx;

import model.Musica;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFileFormat;
import java.io.ByteArrayInputStream;
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
    private Musica musicaAtual;
    private boolean tocando;
    private String caminhoBase;
    private boolean inicializado = false;

    public ReprodutorAudioFX() {
        try {
            new JFXPanel(); // inicia o JavaFX
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

    public boolean temPastaMusicas() {
        return caminhoBase != null;
    }

    public void setCaminhoBase(String caminho) {
        this.caminhoBase = caminho;
        System.out.println("📁 Pasta de músicas definida: " + caminho);
    }

    /**
     * TOCA UMA MÚSICA 🎵
     * Se tiver JavaFX e o arquivo existir, toca de verdade.
     * Senão, simula a reprodução.
     */
    public boolean tocarMusica(Musica musica) {
        if (musica == null)
            return false;

        // se não tem JavaFX ou não achou a pasta, simula
        if (!inicializado || caminhoBase == null) {
            return tocarSimulado(musica);
        }

        try {
            parar();

            // monta o caminho do arquivo
            String caminhoArquivo = caminhoBase + "/" + musica.getArtista() + "/" + musica.getTitulo() + ".mp3";
            File arquivo = new File(caminhoArquivo);

            // tenta com .MP3 maiúsculo
            if (!arquivo.exists()) {
                caminhoArquivo = caminhoBase + "/" + musica.getArtista() + "/" + musica.getTitulo() + ".MP3";
                arquivo = new File(caminhoArquivo);
            }

            // se não achou, simula
            if (!arquivo.exists()) {
                System.out.println("🎵 (SIMULAÇÃO) " + musica.getTitulo() + " - " + musica.getArtista());
                return tocarSimulado(musica);
            }

            // toca de verdade!
            Media media = new Media(arquivo.toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnReady(() -> {
                System.out.println("\n🎵 TOCANDO: " + musica.getTitulo());
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
     * Se o JavaFX estiver disponível, gera um WAV temporário para o MediaPlayer
     * funcionar de verdade (play/pause/timer corretos).
     */
    private boolean tocarSimulado(Musica musica) {
        System.out.println("\n🎵 (SEM MP3) TOCANDO: " + musica.getTitulo());
        System.out.println("   Artista: " + musica.getArtista());

        this.musicaAtual = musica;
        this.tocando = true;

        if (inicializado) {
            try {
                int duracao = musica.getDuracao() > 0 ? musica.getDuracao() : 180;
                File wavTemp = gerarArquivoWavTemporario(duracao);
                Media media = new Media(wavTemp.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnReady(() -> mediaPlayer.play());
                mediaPlayer.setOnEndOfMedia(() -> parar());
                return true;
            } catch (Exception e) {
                System.out.println("⚠️ Não foi possível gerar áudio temporário: " + e.getMessage());
            }
        }

        // fallback: simula no console apenas
        new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(500);
                    if (!tocando)
                        break;
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
     * Gera um arquivo WAV temporário com a duração real da música.
     * Tom de 440 Hz nos primeiros 2 segundos, silêncio no restante.
     * Usa 8-bit mono a 22050 Hz para manter tamanho razoável (~3.9 MB / 3 min).
     */
    private File gerarArquivoWavTemporario(int duracaoSegundos) throws Exception {
        int sampleRate = 22050;
        int totalSamples = sampleRate * duracaoSegundos;
        byte[] data = new byte[totalSamples];

        // Tom audível nos primeiros 2 segundos
        int samplesComTom = Math.min(sampleRate * 2, totalSamples);
        for (int i = 0; i < samplesComTom; i++) {
            data[i] = (byte) (60 * Math.sin(2 * Math.PI * 440.0 * i / sampleRate));
        }
        // Restante já é silêncio (zeros)

        AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, false);
        AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(data), format, totalSamples);

        File tmp = File.createTempFile("eda_audio_", ".wav");
        tmp.deleteOnExit();
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, tmp);
        return tmp;
    }

    /**
     * * PAUSA A MÚISCA ⏸
     */
    public void pausar() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            this.tocando = false;
        } else {
            // modo simulação
            this.tocando = false;
        }
    }

    /**
     * RETOMA A MÚISCA ▶
     */
    public void retomar() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play();
            this.tocando = true;
        } else {
            // modo simulação
            this.tocando = true;
        }
    }

    /**
     * VERIFICA SE ESTÁ TOCANDO
     */
    public boolean estaTocando() {
        if (mediaPlayer != null) {
            return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
        }
        return this.tocando;
    }

    /**
     * * PARA A MÚSICA ⏹
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
    public Musica getMusicaAtual() {
        return musicaAtual;
    }

    /**
     * RETORNA O MEDIAPLAYER PARA MONITORAR PROGRESSO
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}