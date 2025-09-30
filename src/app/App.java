package app;

import model.Inimigos;
import model.Mapa;

public class App {
    public static void main(String[] args) {
        Mapa mapa = new Mapa(5, 10);           // mapa 5x10
        Inimigos inimigo = new Inimigos(10, 1);  // vida 10, velocidade 1

        System.out.println("Spawn: " + mapa.getSpawn());
        System.out.println("Base:  " + mapa.getBase());
        System.out.println("---- Início ----");

        while (true) {
    inimigo.movimentoInimigos(mapa);

    if (inimigo.getPosicaoAtual() != null 
        && inimigo.getPosicaoAtual().equals(mapa.getBase())) {
        // já chegou — NÃO imprime posição e encerra
        break;
    }

    if (inimigo.getPosicaoAtual() != null) {
        System.out.println("Inimigo em: " + inimigo.getPosicaoAtual());
    }
}

    }
}
