package testejavafx;

import Artistas.*;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TesteReprodutorJavaFX {
    
    private static Reprodutor reprodutor;
    private static ReprodutorAudioFX player;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        reprodutor = new Reprodutor();
        player = new ReprodutorAudioFX();
        scanner = new Scanner(System.in);
        
        System.out.println("=========================================");
        System.out.println("   🎵 SISTEMA DE MÚSICA COM JAVAFX 🎵     ");
        System.out.println("=========================================");
        
        carregarTodasMusicas();
        
        boolean continuar = true;
        while (continuar) {
            exibirMenu();
            String opcao = scanner.nextLine();
            
            switch (opcao) {
                case "1": listarArtistas(); break;
                case "2": listarTodasMusicas(); break;
                case "3": adicionarNaFila(); break;
                case "4": reprodutor.exibirFila(); break;
                case "5": tocarMusica(); break;
                case "6": reprodutor.voltarMusica(); break;
                case "7": reprodutor.musicaAtual(); break;
                case "8": reprodutor.exibirHistorico(); break;
                case "9": reprodutor.mostrarRanking(); break;
                case "0":
                    continuar = false;
                    player.parar();
                    System.out.println("\n👋 Até mais!");
                    break;
                default: System.out.println("Opção inválida!");
            }
        }
        scanner.close();
    }
    
    private static void carregarTodasMusicas() {
        System.out.println("\n📀 CARREGANDO MÚSICAS...");
        System.out.println("-----------------------------------------");
        
        List<Musicas> musicas = CarregadorMusicasCompleto.carregarTodasMusicas();
        
        for (Musicas m : musicas) {
            reprodutor.adicionarMusica(m);
        }
        
        System.out.println("-----------------------------------------");
        System.out.println("✅ " + musicas.size() + " músicas carregadas!");
        
        long artistas = musicas.stream().map(Musicas::getArtista).distinct().count();
        System.out.println("🎵 " + artistas + " artistas disponíveis!");
    }
    
    private static void exibirMenu() {
        System.out.println("\n=========================================");
        System.out.println("1 - Listar artistas");
        System.out.println("2 - Listar todas as músicas");
        System.out.println("3 - Adicionar música à fila");
        System.out.println("4 - Ver fila");
        System.out.println("5 - ▶ Tocar próxima");
        System.out.println("6 - ⏮ Voltar");
        System.out.println("7 - Música atual");
        System.out.println("8 - Histórico");
        System.out.println("9 - Ranking");
        System.out.println("0 - Sair");
        System.out.print("Opção: ");
    }
    
    private static void listarArtistas() {
        System.out.println("\n===== ARTISTAS =====");
        List<Musicas> musicas = reprodutor.listarTodasMusicas();
        String ultimo = "";
        for (Musicas m : musicas) {
            if (!m.getArtista().equals(ultimo)) {
                System.out.println("• " + m.getArtista());
                ultimo = m.getArtista();
            }
        }
    }
    
    private static void listarTodasMusicas() {
        System.out.println("\n===== TODAS AS MÚSICAS =====");
        List<Musicas> musicas = reprodutor.listarTodasMusicas();
        String ultimoArtista = "";
        int count = 1;
        
        for (Musicas m : musicas) {
            if (!m.getArtista().equals(ultimoArtista)) {
                System.out.println("\n📀 " + m.getArtista() + ":");
                ultimoArtista = m.getArtista();
            }
            System.out.println("   " + count + ". " + m.getNome());
            count++;
        }
        System.out.println("\nTotal: " + musicas.size() + " músicas");
    }
    
    private static void adicionarNaFila() {
        System.out.print("\nDigite o título: ");
        String busca = scanner.nextLine();
        
        List<Musicas> encontradas = reprodutor.buscarMusicasPorTitulo(busca);
        
        if (encontradas.isEmpty()) {
            System.out.println("Nenhuma música encontrada.");
            return;
        }
        
        for (int i = 0; i < encontradas.size(); i++) {
            Musicas m = encontradas.get(i);
            System.out.println((i+1) + ". " + m.getArtista() + " - " + m.getNome());
        }
        
        System.out.print("Escolha: ");
        try {
            int escolha = Integer.parseInt(scanner.nextLine()) - 1;
            if (escolha >= 0 && escolha < encontradas.size()) {
                reprodutor.adicionarNaFila(encontradas.get(escolha));
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida!");
        }
    }
    
    private static void tocarMusica() {
        if (reprodutor.filaVazia()) {
            System.out.println("Fila vazia! Adicione músicas.");
            return;
        }
        
        Musicas proxima = reprodutor.obterProximaDaFila();
        if (proxima != null) {
            proxima.incrementarPlays();
            reprodutor.marcarComoTocada(proxima);
            player.tocarMusica(proxima);
            player.aguardarTermino();
        }
    }
}