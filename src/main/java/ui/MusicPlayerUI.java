package ui;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import model.Musica;
import reproducer.Reprodutor;
import testejavafx.CarregadorMusicasCompleto;
import testejavafx.ReprodutorAudioFX;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerUI extends Application {

    private final Reprodutor reprodutor = new Reprodutor();
    private final ReprodutorAudioFX player = new ReprodutorAudioFX();
    private final ObservableList<Musica> musicas = FXCollections.observableArrayList();
    private final List<Musica> fila = new ArrayList<>();
    private final Label lblStatus = new Label("Nenhuma música tocando");
    private MediaPlayer mediaPlayer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        carregarMusicas();

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabs.getTabs().addAll(
                new Tab("Músicas", criarTabMusicas()),
                new Tab("Fila", criarTabFila()),
                new Tab("Agora tocando", criarTabNowPlaying()));

        BorderPane root = new BorderPane(tabs);
        root.setPadding(new Insets(10));
        root.setBottom(lblStatus);

        stage.setTitle("MusicPlayer GUI");
        stage.setScene(new Scene(root, 850, 580));
        stage.show();
    }

    private void carregarMusicas() {
        List<Musica> carre = CarregadorMusicasCompleto.carregarTodasMusicas();
        carre.forEach(reprodutor::adicionarMusica);
        musicas.setAll(reprodutor.listarTodasMusicas());
    }

    private VBox criarTabMusicas() {
        TableView<Musica> tabela = new TableView<>(musicas);

        TableColumn<Musica, String> titulo = new TableColumn<>("Título");
        titulo.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getTitulo()));

        TableColumn<Musica, String> artista = new TableColumn<>("Artista");
        artista.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getArtista()));

        TableColumn<Musica, String> tempo = new TableColumn<>("Duração");
        tempo.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getDuracaoFormatada()));

        tabela.getColumns().addAll(titulo, artista, tempo);
        tabela.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Button btnAdd = new Button("Adicionar à fila");
        btnAdd.setOnAction(ev -> {
            Musica selecionada = tabela.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                fila.add(selecionada);
                lblStatus.setText("Adicionado à fila: " + selecionada.getTitulo());
            }
        });

        return new VBox(8, tabela, btnAdd);
    }

    private VBox criarTabFila() {
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setPrefHeight(350);

        Button btnAtualiza = new Button("Atualizar fila");
        btnAtualiza.setOnAction(e -> atualizarFila(area));

        Button btnProxima = new Button("▶ Tocar próxima");
        btnProxima.setOnAction(e -> {
            tocarProxima();
            atualizarFila(area);
        });

        Button btnVoltar = new Button("⏮ Voltar");
        btnVoltar.setOnAction(e -> {
            reprodutor.voltarMusica();
            atualizarStatus();
            atualizarFila(area);
        });

        HBox botoes = new HBox(8, btnAtualiza, btnProxima, btnVoltar);
        botoes.setPadding(new Insets(8, 0, 8, 0));

        return new VBox(8, area, botoes);
    }

    private VBox criarTabNowPlaying() {
        Label t = new Label("Título: -");
        Label a = new Label("Artista: -");

        Button btnParar = new Button("Parar");
        btnParar.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            lblStatus.setText("Tocador parado");
        });

        return new VBox(8, t, a, btnParar);
    }

    private void atualizarFila(TextArea area) {
        if (fila.isEmpty()) {
            area.setText("Fila vazia");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fila.size(); i++) {
            Musica m = fila.get(i);
            sb.append(i + 1).append(". ").append(m.getArtista()).append(" - ").append(m.getTitulo()).append("\n");
        }
        area.setText(sb.toString());
    }

    private void tocarProxima() {
        if (fila.isEmpty()) {
            lblStatus.setText("Fila vazia");
            return;
        }

        Musica proxima = fila.remove(0);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        boolean sucesso = player.tocarMusica(proxima);
        if (sucesso) {
            reprodutor.marcarComoTocada(proxima);
            lblStatus.setText("▶ Tocando: " + proxima.getTitulo());
        } else {
            lblStatus.setText("Erro ao tocar " + proxima.getTitulo());
        }
    }

    private void atualizarStatus() {
        Musica atual = reprodutor.getMusicaAtual();
        if (atual == null) {
            lblStatus.setText("Nenhuma música tocando");
        } else {
            lblStatus.setText("▶ " + atual.getTitulo());
        }
    }
}
