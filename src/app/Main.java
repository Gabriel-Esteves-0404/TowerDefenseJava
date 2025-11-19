package app;

import jogo.GameLoop;
import jogo.WaveManager;
import model.Mapa;
import model.Posicao;
import model.economia.Banco;
import model.economia.Loja;
import model.torre.*;

public class Main {
    public static void main(String[] args) {
        Mapa mapa = new Mapa(8, 12); 
        Banco banco = new Banco(100.0);
        WaveManager ondas = new WaveManager();
        GameLoop jogo = new GameLoop(mapa, banco, ondas, 10);

        Loja loja = new Loja();

        System.out.println("Saldo inicial: " + banco.getSaldo());
        System.out.println("Iniciando configuração das torres...\n");
        Torre poison = loja.comprarTorrePoison(banco, new Posicao(3, 5));
        jogo.adicionarTorre(poison);
        Torre frozen = loja.comprarTorreFrozen(banco, new Posicao(3, 3));
        jogo.adicionarTorre(frozen);
        Torre arqueira1 = loja.comprarTorreArqueira(banco, new Posicao(3,10));
        jogo.adicionarTorre(arqueira1);
        Torre arqueira2 = loja.comprarTorreArqueira(banco, new Posicao(5,11));
        jogo.adicionarTorre(arqueira2);

        System.out.println("\nSaldo após compra das torres: " + banco.getSaldo());
        poison.melhorar(banco);
        frozen.melhorar(banco);
        arqueira1.melhorar(banco);

        System.out.println("\nSaldo após upgrades: " + banco.getSaldo());
        System.out.println("\n===== INICIANDO GAMELOOP TESTE =====\n");
        jogo.run();

        System.out.println("\n===== JOGO ENCERRADO =====");
        System.out.println("Saldo final: " + banco.getSaldo());
    }
}
