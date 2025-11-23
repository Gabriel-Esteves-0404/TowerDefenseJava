package jogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Mapa;
import model.Posicao;
import model.economia.Banco;
import model.torre.Torre;

public class AssinaturasConsole {

    Scanner sc = new Scanner(System.in);
    
    public static void mostrarHUD(GameLoop game, Banco banco, WaveManager ondas) {
        System.out.println("\n=======================");
        System.out.println(" VIDA DA BASE: " + game.getVidaBase());
        System.out.println(" MOEDAS: " + banco.getSaldo());
        System.out.println(" ONDA ATUAL: " + ondas.getIndiceOndaAtual());
        System.out.println("=======================\n");
    }

    public int lerInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("Entrada inválida! Digite apenas números.");
            System.out.println("Digite um número inteiro:");
            sc.next();
        }
        return sc.nextInt();
    }

    public static void mostrarMapaCompleto(Mapa mapa, List<Torre> torres) {

        Posicao spawn = mapa.getSpawn();
        Posicao base = mapa.getBase();
        ArrayList<Posicao> caminho = mapa.getCaminho();
        ArrayList<Posicao> posicoesTorres = new ArrayList<>();
        for (Torre t : torres) {
            posicoesTorres.add(t.getPosicao());
        }

        System.out.println("Mapa (S=spawn, B=base, *=caminho, T=torre, C=construível");

        System.out.print("    ");
        for (int c = 0; c < mapa.getColunas(); c++) {
            System.out.print(c + " ");
        }
        System.out.println();

        for (int l = 0; l < mapa.getLinhas(); l++) {
            System.out.printf("%2d  ", l);

            for (int c = 0; c < mapa.getColunas(); c++) {

                Posicao p = new Posicao(l, c);

                char simbolo;

                if (p.equals(spawn)) {
                    simbolo = 'S';
                }
                else if (p.equals(base)) {
                    simbolo = 'B';
                }
                else if (posicoesTorres.contains(p)) {
                    simbolo = 'T';
                }
                else if (caminho.contains(p)) {
                    simbolo = 'X';
                }
                else {
                    simbolo = 'C';
                }

                System.out.print(simbolo + " ");
            }

            System.out.println();
        }

        System.out.println();
    }

    public Posicao escolherPosicaDaTorre(Mapa mapa){
        int linha = -1;
        int coluna = -1;
        Posicao p;

        System.out.println("Em qual linha deseja inserir a torre:");
        
        while(true){
        linha = lerInt(sc);
            if(linha < mapa.getLinhas() && linha > -1){
                break;
            }
            else{
                System.out.println("Digite um número válido, entre 0 e " +( mapa.getLinhas()-1));
            }
        }       

        System.out.println("Em qual coluna deseja inserir a torre:");

        while(true){
        coluna = lerInt(sc);
            if(coluna <= mapa.getColunas() && coluna > -1){
                break;
            }
            else{
                System.out.println("Digite um número válido, entre 0 e " +( mapa.getColunas()-1));
            }
        }

        p = new Posicao(linha, coluna);
        
        if (!mapa.ehConstruivel(p)) {
            System.out.println("Essa célula NÃO permite construir torre.");
            return null;
        }

        return (p);
    
    }

}

