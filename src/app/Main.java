package app;

import java.util.List;
import java.util.Scanner;
import jogo.AssinaturasConsole;
import jogo.GameLoop;
import jogo.WaveManager;
import model.Mapa;
import model.Posicao;
import model.economia.Banco;
import model.economia.Loja;
import model.excecoes.NivelMaximoException;
import model.excecoes.SaldoInsuficienteException;
import model.torre.Torre;
import model.torre.TorreArqueira;
import model.torre.TorreFrozen;
import model.torre.TorrePoison;

public class Main extends AssinaturasConsole{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        Mapa mapa = new Mapa(9, 9);
        Banco banco = new Banco(50); 
        WaveManager ondas = new WaveManager();
        GameLoop game = new GameLoop(mapa, banco, ondas, 10);
        Loja loja = new Loja();
        AssinaturasConsole console = new AssinaturasConsole();

        System.out.println("=== Tower Defense - Console ===");

        boolean sair = false;

        int opcao;
        while (game.SituacaoDoJogo() && !sair) {
            mostrarHUD(game, banco, ondas);
            AssinaturasConsole.mostrarMapaCompleto(mapa, game.listaAtivaTorres());
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Construir torre");
            System.out.println("2 - Upar torre");
            System.out.println("3 - Iniciar próxima onda");
            System.out.println("4 - Sair");

        while(true){
        opcao = console.lerInt(sc);
            if(opcao <= 4 && opcao >= 1){
                break;
            }
            System.out.println("Digite um número válido, ENTRE (1 e 4)");
        }

            switch (opcao) {
                case 1 -> {
                    Posicao pos = console.escolherPosicaDaTorre(mapa);
                    if(pos == null) {
                        System.out.println("Posição Inválida");
                        break;
                    }

                    System.out.println("Qual tipo de torre deseja construir?");
                    System.out.println("1 - Torre Arqueira (custo: " + TorreArqueira.CUSTO + ")");
                    System.out.println("2 - Torre Poison  (custo: " + TorrePoison.CUSTO + ")");
                    System.out.println("3 - Torre Frozen  (custo: " + TorreFrozen.CUSTO + ")");
                    System.out.println("4 - Cancelar");

                    int escolhaTipo;
                    while (true) {
                        escolhaTipo = console.lerInt(sc);
                        if (escolhaTipo >= 1 && escolhaTipo <= 4) break;
                        System.out.println("Digite um valor válido (1 a 4).");
                    }
                        if (escolhaTipo == 4) {
                            System.out.println("Construção cancelada.");
                            break;
                        }
                    Torre novaTorre = null;
                    switch (escolhaTipo) {
                        case 1 -> novaTorre = loja.comprarTorreArqueira(banco, pos);
                        case 2 -> novaTorre = loja.comprarTorrePoison(banco, pos);
                        case 3 -> novaTorre = loja.comprarTorreFrozen(banco, pos);
                    }
                    if (novaTorre == null) {
                        System.out.println("Não foi possível construir a torre.");
                        break;
                    }
                    
                    game.adicionarTorre(novaTorre);
                    System.out.println("Torre construída em (" +
                            pos.getLinha() + "," + pos.getColuna() + ").");

                    }

                case 2 -> {
                    List<Torre> torres = game.listaAtivaTorres();
                    if (torres.isEmpty()) {
                        System.out.println("Não há torres para upar.");
                        break;
                    }

                    System.out.println("Escolha a torre para upar:");

                    for (int i = 0; i < torres.size(); i++) {
                        Torre t = torres.get(i);
                        System.out.println(i + " - " + t.getClass().getSimpleName()
                                + " | pos=(" + t.getPosicao().getLinha() + "," + t.getPosicao().getColuna() + ")"
                                + " | nível=" + t.getNivel());
                    }

                    int escolha;
                    while (true) {
                        escolha = console.lerInt(sc);
                        if (escolha >= 0 && escolha < torres.size()) {
                            break;
                        }
                        System.out.println("Índice inválido, escolha um valor entre 0 e " + (torres.size() - 1));
                    }

                    Torre torreEscolhida = torres.get(escolha);

                    try {
                        torreEscolhida.melhorar(banco);  
                        System.out.println("Torre upada para nível " + torreEscolhida.getNivel());
                    } catch (NivelMaximoException e) {
                        System.out.println("Essa torre já está no nível máximo.");
                    } catch (SaldoInsuficienteException e) {
                        System.out.println("Moedas insuficientes para upar essa torre.");
                    }
                }

                case 3 -> {
                    if (ondas.getIndiceOndaAtual() == 0) {
                        ondas.iniciarOnda();
                    } else {
                        ondas.proximaOnda();
                    }

                    game.rodarUmaOnda();
                }
                case 4 ->{
                    sair = true;
                    System.out.println("Saindo do jogo...");
                }

                default -> System.out.println("Opção inválida.");
            }
        }
        
        if (!game.SituacaoDoJogo()) {
            System.out.println("Jogo encerrado (fim de partida).");
        } 
        else if (sair) {
            System.out.println("Jogo encerrado pelo jogador.");
        } 
        else {
            System.out.println("Jogo encerrado.");
        }
    }
}
