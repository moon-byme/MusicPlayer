package main;

import model.Musica;
import player.CarregadorMusicasCompleto;
import player.ReprodutorAudioFX;
import reproducer.Reprodutor;
import structures.ArvoreAVL;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Ponto de entrada do Reprodutor de Música em modo terminal.
 *
 * Inicializa o sistema, carrega o catálogo de músicas e exibe
 * o menu interativo para o usuário navegar pelas funcionalidades.
 *
 * Estruturas de dados utilizadas indiretamente via {@link Reprodutor}:
 * - {@link structures.ArvoreAVL} — catálogo de músicas (busca e listagem)
 * - {@link structures.FiladeReproducao} — fila de reprodução (FIFO)
 * - {@link structures.HistoricoMusicas} — histórico (LIFO / pilha)
 * - {@link structures.HeapBinaria} — ranking de mais tocadas (max-heap)
 * - {@link structures.HashArtistas} — agrupamento por artista (hash)
 *
 * @author Lethycia
 */
public class Main {

    private static ReprodutorAudioFX reprodutorAudio = new ReprodutorAudioFX();
    private static Reprodutor reprodutor = new Reprodutor();
    private static Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));

    /**
     * Método principal: carrega as músicas e inicia o menu interativo.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        carregarMusicasExemplo();
        exibirMenu();
    }

    /**
     * Carrega todas as músicas disponíveis no catálogo.
     * Tenta carregar da pasta local; se não encontrar, usa músicas embutidas.
     */
    private static void carregarMusicasExemplo() {
        System.out.println("Carregando músicas...");
        List<Musica> musicas = CarregadorMusicasCompleto.carregarTodasMusicas();
        for (Musica m : musicas) {
            reprodutor.adicionarMusica(m);
        }
        System.out.println("✓ " + reprodutor.listarTodasMusicas().size() + " músicas carregadas.\n");
    }

    /**
     * Exibe o menu principal em loop até o usuário escolher sair (opção 0).
     */
    private static void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Listar todas as músicas");
            System.out.println("2. Buscar música por título");
            System.out.println("3. Adicionar música à fila");
            System.out.println("4. Tocar próxima música");
            System.out.println("5. Voltar música");
            System.out.println("6. Mostrar ranking (mais tocadas)");
            System.out.println("7. Exibir fila de reprodução");
            System.out.println("8. Exibir histórico");
            System.out.println("9. Música atual");
            System.out.println("10. Listar músicas por artista");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            opcao = lerInteiro();

            switch (opcao) {
                case 1 -> listarMusicas();
                case 2 -> buscarMusica();
                case 3 -> adicionarNaFila();
                case 4 -> tocarProxima();
                case 5 -> voltarMusica();
                case 6 -> mostrarRanking();
                case 7 -> reprodutor.exibirFila();
                case 8 -> reprodutor.exibirHistorico();
                case 9 -> reprodutor.musicaAtual();
                case 10 -> listarPorArtista();
                case 0 -> {
                    System.out.println("Encerrando...");
                    javafx.application.Platform.exit();
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    /**
     * Lista todas as músicas do catálogo em ordem alfabética por título.
     * Usa o percurso in-order da {@link ArvoreAVL}.
     */
    private static void listarMusicas() {
        ArvoreAVL catalogo = reprodutor.getCatalogo();
        List<Musica> musicas = catalogo.inOrder();
        if (musicas.isEmpty()) {
            System.out.println("Nenhuma música no catálogo.");
        } else {
            System.out.println("\n===== CATÁLOGO COMPLETO =====");
            for (int i = 0; i < musicas.size(); i++) {
                System.out.println((i + 1) + ". " + musicas.get(i).getTitulo() +
                        " - " + musicas.get(i).getArtista() +
                        " (" + musicas.get(i).getDuracaoFormatada() + ")");
            }
        }
    }

    /**
     * Lista todas as músicas de um artista usando a
     * {@link structures.HashArtistas}.
     */
    private static void listarPorArtista() {
        System.out.print("Digite o nome do artista: ");
        String nome = scanner.nextLine().trim();
        reprodutor.listarPorArtista(nome);
    }

    /**
     * Busca músicas cujo título contenha o termo digitado (busca parcial).
     */
    private static void buscarMusica() {
        System.out.print("Digite o título (ou parte): ");
        String termo = scanner.nextLine().trim();
        List<Musica> resultados = reprodutor.buscarMusicasPorTitulo(termo);
        if (resultados.isEmpty()) {
            System.out.println("Nenhuma música encontrada.");
        } else {
            System.out.println("\n===== RESULTADOS =====");
            for (Musica m : resultados) {
                System.out.println("- " + m.getTitulo() + " (" + m.getArtista() + ")");
            }
        }
    }

    /**
     * Busca pelo título exato e adiciona a música à fila de reprodução.
     */
    private static void adicionarNaFila() {
        System.out.print("Digite o título exato da música: ");
        String titulo = scanner.nextLine().trim();
        Musica musica = reprodutor.buscarMusica(titulo);
        if (musica == null) {
            System.out.println("Música não encontrada.");
        } else {
            reprodutor.adicionarNaFila(musica);
        }
    }

    /**
     * Remove a próxima música da fila e a reproduz.
     * Registra a reprodução no histórico e atualiza o ranking.
     */
    private static void tocarProxima() {
        if (reprodutor.filaVazia()) {
            System.out.println("Fila vazia. Adicione músicas antes de tocar.");
            return;
        }
        Musica proxima = reprodutor.obterProximaDaFila();
        if (proxima != null) {
            boolean tocou = reprodutorAudio.tocarMusica(proxima);
            if (tocou) {
                reprodutor.marcarComoTocada(proxima);
                System.out.println("▶ Reproduzindo: " + proxima.getTitulo());
                reprodutorAudio.aguardarTermino();
            } else {
                System.out.println("Não foi possível tocar a música.");
            }
        } else {
            System.out.println("Erro ao obter próxima música.");
        }
    }

    /**
     * Volta para a música anterior usando o histórico (pilha).
     * Exibe a música atual após a operação.
     */
    private static void voltarMusica() {
        reprodutor.voltarMusica();
        reprodutor.musicaAtual();
    }

    /**
     * Exibe o ranking das músicas mais tocadas usando a
     * {@link structures.HeapBinaria}.
     */
    private static void mostrarRanking() {
        reprodutor.mostrarRanking();
    }

    /**
     * Lê um inteiro do terminal com validação de entrada.
     * Descarta tokens inválidos até receber um número.
     *
     * @return o inteiro lido
     */
    private static int lerInteiro() {
        while (!scanner.hasNextInt()) {
            System.out.print("Digite um número: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // consome a quebra de linha restante
        return valor;
    }
}