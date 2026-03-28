package main;

import model.Musica;
import reproducer.Reprodutor;
import structures.ArvoreAVL;
import testejavafx.ReprodutorAudioFX;
import util.CarregadorMusicasCompleto;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static ReprodutorAudioFX reprodutorAudio = new ReprodutorAudioFX();

    private static Reprodutor reprodutor = new Reprodutor();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        carregarMusicasExemplo();
        exibirMenu();
    }

    private static void carregarMusicasExemplo() {
        System.out.println("Carregando músicas...");
        List<Musica> musicas = CarregadorMusicasCompleto.carregarTodasMusicas();
        for (Musica m : musicas) {
            reprodutor.adicionarMusica(m);
        }
        System.out.println("✓ " + reprodutor.listarTodasMusicas().size() + " músicas carregadas.\n");
    }

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
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void listarMusicas() {
        ArvoreAVL catalogo = reprodutor.getCatalogo();
        List<Musica> musicas = catalogo.inOrder();
        if (musicas.isEmpty()) {
            System.out.println("Nenhuma m\u00fasica no cat\u00e1logo.");
        } else {
            System.out.println("\n===== CAT\u00c1LOGO COMPLETO =====");
            for (int i = 0; i < musicas.size(); i++) {
                System.out.println((i + 1) + ". " + musicas.get(i).getTitulo() +
                        " - " + musicas.get(i).getArtista() +
                        " (" + musicas.get(i).getDuracaoFormatada() + ")");
            }
        }
    }

    private static void listarPorArtista() {
        System.out.print("Digite o nome do artista: ");
        String nome = scanner.nextLine().trim();
        reprodutor.listarPorArtista(nome);
    }

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

    private static void tocarProxima() {
        if (reprodutor.filaVazia()) {
            System.out.println("Fila vazia. Adicione músicas antes de tocar.");
            return;
        }
        Musica proxima = reprodutor.obterProximaDaFila();
        if (proxima != null) {
            boolean tocou = reprodutorAudio.tocarMusica(proxima);
            if (tocou) {
                proxima.incrementarPlays();
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

    private static void voltarMusica() {
        reprodutor.voltarMusica();
        // Opcional: mostrar a música atual após voltar
        reprodutor.musicaAtual();
    }

    private static void mostrarRanking() {
        reprodutor.mostrarRanking();
    }

    private static int lerInteiro() {
        while (!scanner.hasNextInt()) {
            System.out.print("Digite um número: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // consome quebra de linha
        return valor;
    }
}