package ui;

/**
 * Controlador JavaFX da interface gráfica do Reprodutor de Músicas.
 *
 * Gerencia todos os eventos de botões, exibição de cards (músicas, artistas,
 * playlists, histórico e ranking) e comunicação com as camadas de domínio
 * ({@link reproducer.Reprodutor}) e de áudio ({@link player.ReprodutorAudioFX}).
 *
 * <p>Principais responsabilidades:</p>
 * <ul>
 *   <li>Carregar o catálogo de músicas via {@link player.CarregadorMusicasCompleto}</li>
 *   <li>Gerenciar a fila de reprodução local e acionar a reprodução de áudio</li>
 *   <li>Atualizar a barra inferior com título, artista, progresso e tempo</li>
 *   <li>Renderizar cards dinâmicos para cada modo de visualização</li>
 *   <li>Executar buscas por título ou artista</li>
 * </ul>
 *
 * @author Lethycia
 * @see reproducer.Reprodutor
 * @see player.ReprodutorAudioFX
 * @see MusicPlayerUI
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.Musica;
import player.CarregadorMusicasCompleto;
import player.ReprodutorAudioFX;
import reproducer.Reprodutor;
import testejavafx.ReprodutorAudioFX;
import util.CarregadorMusicasCompleto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MusicPlayerController implements Initializable {

    @FXML
    private Button btnArtistas;
    @FXML
    private Button btnMusicasCatalogo;
    @FXML
    private Button btnTocando;
    @FXML
    private Button btnLista;
    @FXML
    private Button btnHistorico;
    @FXML
    private Button btnMaisTocadas;
    @FXML
    private Button btnSair;

    @FXML
    private Label lblTitulo;
    @FXML
    private FlowPane gridMusicas;
    @FXML
    private ScrollPane scrollMusicas;

    @FXML
    private TextField txtBusca;
    @FXML
    private RadioButton rbTitulo;
    @FXML
    private RadioButton rbArtista;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnLimparBusca;

    @FXML
    private Label lblAgora;
    @FXML
    private Label lblTempo;
    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnTocar;
    @FXML
    private Button btnProxima;
    @FXML
    private Button btnParar;
    @FXML
    private ImageView imgPlayerThumb;
    @FXML
    private Label lblNomeMusica;
    @FXML
    private Label lblArtistaPlayer;
    @FXML
    private Label lblTempoAtual;
    @FXML
    private Label lblTempoTotal;
    @FXML
    private Slider sliderProgresso;

    // Dados
    private final Reprodutor reprodutor = new Reprodutor();
    private final ReprodutorAudioFX player = new ReprodutorAudioFX();
    private final ObservableList<Musica> musicas = FXCollections.observableArrayList();
    private final List<Musica> fila = new ArrayList<>();
    private String modoAtual = "songs";
    private Timeline timerTempo;

    /**
     * Inicializa o controlador após o FXML ser carregado.
     * Carrega músicas, configura botões, inicia o timer de progresso
     * e verifica a disponibilidade da pasta de áudio.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarMusicas();
        configurarBotoes();
        iniciarTimer();
        exibirMusicas();
        if (!player.temPastaMusicas()) {
            javafx.application.Platform.runLater(this::perguntarPastaMusicas);
        }
    }

    /**
     * Exibe um diálogo pedindo ao usuário que selecione a pasta
     * com os arquivos MP3, caso ela não seja encontrada automaticamente.
     */
    private void perguntarPastaMusicas() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pasta de músicas");
        alert.setHeaderText("Arquivos MP3 não encontrados");
        alert.setContentText(
                "A pasta 'Músicas do Projeto' não foi encontrada automaticamente.\n\n" +
                        "Deseja selecionar a pasta onde estão os arquivos MP3?");

        ButtonType btnSelecionar = new ButtonType("Selecionar pasta");
        ButtonType btnSemAudio = new ButtonType("Continuar sem áudio");
        alert.getButtonTypes().setAll(btnSelecionar, btnSemAudio);

        alert.showAndWait().ifPresent(resposta -> {
            if (resposta == btnSelecionar) {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Selecione a pasta com as subpastas de artistas");
                Stage stage = (Stage) lblTitulo.getScene().getWindow();
                java.io.File pasta = chooser.showDialog(stage);
                if (pasta != null) {
                    player.setCaminhoBase(pasta.getAbsolutePath());
                    lblAgora.setText("📁 Pasta selecionada: " + pasta.getName());
                }
            }
        });
    }

    /**
     * Inicia um {@link javafx.animation.Timeline} que atualiza a barra de
     * progresso e os rótulos de tempo a cada segundo durante a reprodução.
     */
    private void iniciarTimer() {
        timerTempo = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            MediaPlayer mp = player.getMediaPlayer();
            if (mp != null) {
                double atual = mp.getCurrentTime().toSeconds();
                double total = mp.getTotalDuration() != null ? mp.getTotalDuration().toSeconds() : 0;
                lblTempoAtual.setText(formatarTempo((int) atual));
                if (total > 0) {
                    lblTempoTotal.setText(formatarTempo((int) total));
                    if (!sliderProgresso.isValueChanging()) {
                        sliderProgresso.setValue((atual / total) * 100.0);
                    }
                }
            }
        }));
        timerTempo.setCycleCount(Timeline.INDEFINITE);
        timerTempo.play();
        sliderProgresso.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                MediaPlayer mp = player.getMediaPlayer();
                if (mp != null && mp.getTotalDuration() != null) {
                    double total = mp.getTotalDuration().toSeconds();
                    mp.seek(javafx.util.Duration.seconds((sliderProgresso.getValue() / 100.0) * total));
                }
            }
        });
    }

    private String formatarTempo(int segundos) {
        return String.format("%02d:%02d", segundos / 60, segundos % 60);
    }

    /** Carrega todas as músicas do catálogo e popula a lista observável. */
    private void carregarMusicas() {
        List<Musica> carre = CarregadorMusicasCompleto.carregarTodasMusicas();
        carre.forEach(reprodutor::adicionarMusica);
        musicas.setAll(reprodutor.listarTodasMusicas());
    }

    /** Configura os handlers de todos os botões da interface. */
    private void configurarBotoes() {
        btnMusicasCatalogo.setOnAction(e -> {
            modoAtual = "songs";
            lblTitulo.setText("🎵 Músicas");
            exibirMusicas();
        });

        btnArtistas.setOnAction(e -> {
            modoAtual = "artists";
            lblTitulo.setText("👥 Artistas");
            exibirArtistas();
        });

        btnTocando.setOnAction(e -> {
            modoAtual = "playing";
            lblTitulo.setText("▶ Reproduzindo");
            exibirTocando();
        });

        btnLista.setOnAction(e -> {
            modoAtual = "queue";
            lblTitulo.setText("📋 Lista de Reprodução");
            exibirFila();
        });

        btnHistorico.setOnAction(e -> {
            modoAtual = "history";
            lblTitulo.setText("⏱ Tocados Recentemente");
            exibirHistorico();
        });

        btnMaisTocadas.setOnAction(e -> {
            modoAtual = "top";
            lblTitulo.setText("📈 Mais Tocadas");
            exibirMaisTocadas();
        });

        btnTocar.setOnAction(e -> {
            if (player.estaTocando()) {
                player.pausar();
                btnTocar.setText("▶");
                lblAgora.setText("⏸ Pausado");
            } else if (!fila.isEmpty()) {
                tocarProxima();
            } else if (reprodutor.getMusicaAtual() != null) {
                player.retomar();
                btnTocar.setText("⏸");
                lblAgora.setText("▶ Tocando: " + reprodutor.getMusicaAtual().getTitulo()
                        + " - " + reprodutor.getMusicaAtual().getArtista());
            }
        });

        btnProxima.setOnAction(e -> {
            if (!fila.isEmpty()) {
                tocarProxima();
            }
        });

        btnVoltar.setOnAction(e -> {
            Musica anterior = reprodutor.voltarMusicaUI();
            if (anterior != null) {
                atualizarPlayerBar(anterior);
                boolean sucesso = player.tocarMusica(anterior);
                if (sucesso) {
                    btnTocar.setText("⏸");
                    if (modoAtual.equals("playing"))
                        exibirTocando();
                } else {
                    lblNomeMusica.setText("Erro ao tocar: " + anterior.getTitulo());
                    lblArtistaPlayer.setText("");
                }
            } else {
                lblNomeMusica.setText("⏮ Nenhuma música no histórico");
                lblArtistaPlayer.setText("");
            }
        });

        btnParar.setOnAction(e -> {
            if (player != null) {
                player.pausar();
                btnTocar.setText("▶");
            }
        });

        btnBuscar.setOnAction(e -> executarBusca());
        txtBusca.setOnAction(e -> executarBusca());

        btnLimparBusca.setOnAction(e -> {
            txtBusca.clear();
            modoAtual = "songs";
            lblTitulo.setText("🎵 Músicas");
            exibirMusicas();
        });

        btnSair.setOnAction(e -> System.exit(0));
    }

    /**
     * Executa a busca pelo termo digitado no campo de texto,
     * filtrando por título ou artista conforme o RadioButton selecionado.
     */
    private void executarBusca() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty())
            return;

        List<Musica> resultado;
        if (rbArtista.isSelected()) {
            resultado = reprodutor.buscarMusicasPorArtista(termo);
            lblTitulo.setText("🔍 Artista: \"" + termo + "\" — " + resultado.size() + " música(s)");
        } else {
            resultado = reprodutor.buscarMusicasPorTitulo(termo);
            lblTitulo.setText("🔍 Título: \"" + termo + "\" — " + resultado.size() + " música(s)");
        }

        modoAtual = "search";
        gridMusicas.getChildren().clear();

        if (resultado.isEmpty()) {
            Label semResultado = new Label("Nenhum resultado encontrado para \"" + termo + "\"");
            semResultado.setStyle("-fx-font-size: 14; -fx-text-fill: #999;");
            gridMusicas.getChildren().add(semResultado);
            return;
        }

        for (Musica m : resultado) {
            gridMusicas.getChildren().add(criarCartaoMusica(m));
        }
    }

    /** Exibe todos os cards de músicas do catálogo na grade central. */
    private void exibirMusicas() {
        gridMusicas.getChildren().clear();
        for (Musica m : musicas) {
            gridMusicas.getChildren().add(criarCartaoMusica(m));
        }
    }

    /** Exibe um card por artista único encontrado no catálogo. */
    private void exibirArtistas() {
        gridMusicas.getChildren().clear();
        // Agrupar por artista
        var artistasMap = new java.util.HashMap<String, Musica>();
        for (Musica m : musicas) {
            artistasMap.putIfAbsent(m.getArtista(), m);
        }
        for (Musica m : artistasMap.values()) {
            gridMusicas.getChildren().add(criarCartaoArtista(m.getArtista()));
        }
    }

    /** Exibe o card grande da música atualmente em reprodução. */
    private void exibirTocando() {
        gridMusicas.getChildren().clear();
        Musica atual = reprodutor.getMusicaAtual();
        if (atual != null) {
            gridMusicas.getChildren().add(criarCartaoGrande(atual));
        } else {
            Label label = new Label("Nenhuma música tocando");
            gridMusicas.getChildren().add(label);
        }
    }

    /** Exibe os cards da fila de reprodução atual em ordem de posição. */
    private void exibirFila() {
        gridMusicas.getChildren().clear();
        if (fila.isEmpty()) {
            Label label = new Label("Fila vazia");
            gridMusicas.getChildren().add(label);
        } else {
            for (int i = 0; i < fila.size(); i++) {
                Musica m = fila.get(i);
                gridMusicas.getChildren().add(criarCartaoFila(i + 1, m));
            }
        }
    }

    /** Exibe o histórico de músicas tocadas (mais recente primeiro). */
    private void exibirHistorico() {
        gridMusicas.getChildren().clear();

        // Usa a HistoricoMusicas (pilha) real — mais recente primeiro
        List<Musica> tocadas = reprodutor.getHistoricoMusicas().toList();

        if (tocadas.isEmpty()) {
            Label label = new Label("📜 Nenhuma música tocada ainda");
            label.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 13;");
            gridMusicas.getChildren().add(label);
        } else {
            for (int i = 0; i < tocadas.size(); i++) {
                gridMusicas.getChildren().add(criarCartaoHistorico(i + 1, tocadas.get(i)));
            }
        }
    }

    /** Exibe o ranking das músicas com mais reproduções em ordem decrescente. */
    private void exibirMaisTocadas() {
        gridMusicas.getChildren().clear();

        List<Musica> ranking = new ArrayList<>();
        for (Musica m : musicas) {
            if (m.getPlays() > 0) {
                ranking.add(m);
            }
        }
        ranking.sort((a, b) -> Integer.compare(b.getPlays(), a.getPlays()));

        if (ranking.isEmpty()) {
            Label label = new Label("🎵 Nenhuma música tocada ainda");
            gridMusicas.getChildren().add(label);
        } else {
            for (int i = 0; i < ranking.size(); i++) {
                gridMusicas.getChildren().add(criarCartaoHistorico(i + 1, ranking.get(i)));
            }
        }
    }

    /**
     * Cria um card visual para uma música, com imagem do artista (ou cor
     * de fallback), título, nome do artista e botão para adicionar à fila.
     *
     * @param musica música a ser exibida
     * @return {@link javafx.scene.layout.VBox} com o card pronto
     */
    private VBox criarCartaoMusica(Musica musica) {
        VBox cartao = new VBox(5);
        cartao.setStyle(
                "-fx-background-color: #2A2A2A; -fx-background-radius: 8; -fx-padding: 10;");
        cartao.setPrefWidth(140);

        // Tenta carregar imagem do artista
        ImageView thumb = new ImageView();
        thumb.setFitWidth(120);
        thumb.setFitHeight(100);
        thumb.setPreserveRatio(false);

        Image img = carregarImagemArtista(musica.getArtista());
        if (img != null) {
            thumb.setImage(img);
            cartao.getChildren().add(thumb);
        } else {
            // Fallback: cor
            Rectangle rect = new Rectangle(120, 100);
            rect.setFill(Color.web(String.format("#%06x", musica.getArtista().hashCode() & 0xffffff)));
            cartao.getChildren().add(rect);
        }

        Label titulo = new Label(musica.getTitulo());
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 11; -fx-text-fill: #FFFFFF;");
        titulo.setWrapText(true);

        Label artista = new Label(musica.getArtista());
        artista.setStyle("-fx-font-size: 10; -fx-text-fill: #B3B3B3;");

        Button btnAdicionar = new Button("+ Adicionar");
        btnAdicionar.setPrefWidth(120);
        btnAdicionar.setStyle(
                "-fx-font-size: 10; -fx-background-color: #E8587A; -fx-text-fill: #FFFFFF; -fx-background-radius: 4; -fx-cursor: hand;");
        btnAdicionar.setOnAction(e -> {
            fila.add(musica);
            lblAgora.setText("✓ Adicionado: " + musica.getTitulo());
        });

        cartao.getChildren().addAll(titulo, artista, btnAdicionar);
        return cartao;
    }

    /**
     * Cria um card visual para um artista, com foto ou cor de fallback.
     *
     * @param artista nome do artista
     * @return {@link javafx.scene.layout.VBox} com o card pronto
     */
    private VBox criarCartaoArtista(String artista) {
        VBox cartao = new VBox(5);
        cartao.setStyle(
                "-fx-background-color: #2A2A2A; -fx-background-radius: 8; -fx-padding: 10;");
        cartao.setPrefWidth(140);

        // Tenta carregar imagem do artista
        ImageView thumb = new ImageView();
        thumb.setFitWidth(120);
        thumb.setFitHeight(100);
        thumb.setPreserveRatio(false);

        Image img = carregarImagemArtista(artista);
        if (img != null) {
            thumb.setImage(img);
            cartao.getChildren().add(thumb);
        } else {
            // Fallback: cor
            Rectangle rect = new Rectangle(120, 100);
            rect.setFill(Color.web(String.format("#%06x", artista.hashCode() & 0xffffff)));
            cartao.getChildren().add(rect);
        }

        Label nome = new Label(artista);
        nome.setStyle("-fx-font-weight: bold; -fx-font-size: 11; -fx-text-fill: #FFFFFF;");
        nome.setWrapText(true);

        cartao.getChildren().add(nome);
        return cartao;
    }

    /**
     * Cria um card expandido (400px) para a música em reprodução,
     * exibindo imagem maior, título, artista e duração formatada.
     *
     * @param musica música em reprodução
     * @return {@link javafx.scene.layout.VBox} com o card grande
     */
    private VBox criarCartaoGrande(Musica musica) {
        VBox cartao = new VBox(10);
        cartao.setPadding(new Insets(20));
        cartao.setStyle(
                "-fx-background-color: #2A2A2A; -fx-background-radius: 10; -fx-padding: 20;");
        cartao.setPrefWidth(400);

        // Tenta carregar imagem do artista
        ImageView thumb = new ImageView();
        thumb.setFitWidth(200);
        thumb.setFitHeight(200);
        thumb.setPreserveRatio(false);

        Image img = carregarImagemArtista(musica.getArtista());
        if (img != null) {
            thumb.setImage(img);
            cartao.getChildren().add(thumb);
        } else {
            // Fallback: cor
            Rectangle rect = new Rectangle(200, 200);
            rect.setFill(Color.web(String.format("#%06x", musica.getArtista().hashCode() & 0xffffff)));
            cartao.getChildren().add(rect);
        }

        Label titulo = new Label(musica.getTitulo());
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: #FFFFFF;");

        Label artista = new Label(musica.getArtista());
        artista.setStyle("-fx-font-size: 14; -fx-text-fill: #B3B3B3;");

        Label duracao = new Label("Duração: " + musica.getDuracaoFormatada());
        duracao.setStyle("-fx-font-size: 12; -fx-text-fill: #B3B3B3;");

        cartao.getChildren().addAll(titulo, artista, duracao);
        return cartao;
    }

    /**
     * Cria um card para um item da fila de reprodução com número de posição.
     *
     * @param posicao posição do item na fila (base 1)
     * @param musica  música na fila
     * @return {@link javafx.scene.layout.VBox} com o card da fila
     */
    private VBox criarCartaoFila(int posicao, Musica musica) {
        VBox cartao = new VBox(5);
        cartao.setStyle(
                "-fx-background-color: #2A2A2A; -fx-background-radius: 8; -fx-padding: 10;");
        cartao.setPrefWidth(160);

        Label numero = new Label(posicao + ".");
        numero.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-text-fill: #E8587A;");

        Label titulo = new Label(musica.getTitulo());
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 11; -fx-text-fill: #FFFFFF;");
        titulo.setWrapText(true);

        Label artista = new Label(musica.getArtista());
        artista.setStyle("-fx-font-size: 10; -fx-text-fill: #B3B3B3;");

        cartao.getChildren().addAll(numero, titulo, artista);
        return cartao;
    }

    /**
     * Cria um card para o histórico ou ranking, exibindo posição,
     * título, artista e contagem de reproduções.
     *
     * @param posicao posição na lista (base 1)
     * @param musica  música do histórico ou ranking
     * @return {@link javafx.scene.layout.VBox} com o card
     */
    private VBox criarCartaoHistorico(int posicao, Musica musica) {
        VBox cartao = new VBox(5);
        cartao.setStyle(
                "-fx-background-color: #2A2A2A; -fx-background-radius: 8; -fx-padding: 10;");
        cartao.setPrefWidth(160);

        Label numero = new Label(posicao + ".");
        numero.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-text-fill: #E8587A;");

        Label titulo = new Label(musica.getTitulo());
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 11; -fx-text-fill: #FFFFFF;");
        titulo.setWrapText(true);

        Label artista = new Label(musica.getArtista());
        artista.setStyle("-fx-font-size: 10; -fx-text-fill: #B3B3B3;");

        Label plays = new Label("▶ " + musica.getPlays() + " vezes");
        plays.setStyle("-fx-font-size: 10; -fx-text-fill: #E8587A; -fx-font-weight: bold;");

        cartao.getChildren().addAll(numero, titulo, artista, plays);
        return cartao;
    }

    /**
     * Atualiza os controles da barra inferior (nome, artista, imagem,
     * slider e labels de tempo) com os dados da música informada.
     *
     * @param musica música a exibir; se {@code null}, reseta todos os controles
     */
    private void atualizarPlayerBar(Musica musica) {
        if (musica == null) {
            lblNomeMusica.setText("Nenhuma música selecionada");
            lblArtistaPlayer.setText("");
            sliderProgresso.setValue(0);
            lblTempoAtual.setText("0:00");
            lblTempoTotal.setText("0:00");
            imgPlayerThumb.setImage(null);
            return;
        }
        lblNomeMusica.setText(musica.getTitulo());
        lblArtistaPlayer.setText(musica.getArtista());
        lblTempoAtual.setText("0:00");
        lblTempoTotal.setText(musica.getDuracaoFormatada());
        sliderProgresso.setValue(0);
        imgPlayerThumb.setImage(carregarImagemArtista(musica.getArtista()));
    }

    /**
     * Remove a primeira música da fila, atualiza a barra do player,
     * inicia a reprodução de áudio e registra a música como tocada
     * no {@link reproducer.Reprodutor}.
     */
    private void tocarProxima() {
        if (fila.isEmpty()) {
            return;
        }

        Musica proxima = fila.remove(0);
        atualizarPlayerBar(proxima);
        boolean sucesso = player.tocarMusica(proxima);

        if (sucesso) {
            reprodutor.marcarComoTocada(proxima);
            btnTocar.setText("⏸");
            if (modoAtual.equals("playing"))
                exibirTocando();
        } else {
            lblNomeMusica.setText("Erro ao tocar: " + proxima.getTitulo());
            lblArtistaPlayer.setText("");
        }
    }

    /**
     * Tenta carregar a imagem do artista a partir dos recursos do classpath
     * ({@code /images/artistas/}), tentando extensões {@code .jpg} e {@code .png}.
     *
     * @param nomeArtista nome do artista (será normalizado para lowercase com
     *                    hífens)
     * @return {@link javafx.scene.image.Image} carregada, ou {@code null} se não
     *         encontrada
     */
    private Image carregarImagemArtista(String nomeArtista) {
        if (nomeArtista == null || nomeArtista.isEmpty()) {
            return null;
        }

        // Normaliza o nome: lowercase, substitui espaços por hífens
        String nomeFoto = nomeArtista
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", "-");

        // Tenta diferentes formas de carregar a imagem
        String[] caminhosPossiveis = {
                "/images/artistas/" + nomeFoto + ".jpg",
                "/images/artistas/" + nomeFoto + ".png",
                "images/artistas/" + nomeFoto + ".jpg",
                "images/artistas/" + nomeFoto + ".png"
        };

        for (String caminho : caminhosPossiveis) {
            try {
                // Tenta com getResource
                URL recurso = getClass().getResource(caminho);
                if (recurso != null) {
                    Image img = new Image(recurso.toExternalForm());
                    if (!img.isError()) {
                        System.out.println("✓ Imagem carregada: " + nomeArtista + " -> " + caminho);
                        return img;
                    }
                }

                // Tenta com ClassLoader
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                recurso = cl.getResource(caminho.startsWith("/") ? caminho.substring(1) : caminho);
                if (recurso != null) {
                    Image img = new Image(recurso.toExternalForm());
                    if (!img.isError()) {
                        System.out.println("✓ Imagem carregada (ClassLoader): " + nomeArtista + " -> " + caminho);
                        return img;
                    }
                }
            } catch (Exception e) {
                System.out.println("Debug: Erro ao tentar " + caminho + " - " + e.getMessage());
            }
        }

        System.out.println("⚠️ Imagem não encontrada para: " + nomeArtista + " (tentou: /" + nomeFoto + ".jpg)");
        return null; // Retorna null - usará cor como fallback
    }
}
