package app;

import jogo.GameLoop;
import jogo.WaveManager;
import model.Mapa;
import model.Posicao;
import model.economia.Banco;
import model.economia.Loja;
import model.torre.Torre;
import model.torre.TorreArqueira;

public class Main {
    public static void main(String[] args) {
        Mapa mapa = new Mapa(5, 10);
        Banco banco = new Banco(50);
        WaveManager ondas = new WaveManager();
        GameLoop jogo = new GameLoop(mapa, banco, ondas, 5);

        Loja loja = new Loja();

        Torre t1 = loja.comprarTorreNormal(banco, new Posicao(1, 2));
        jogo.adicionarTorre(t1);

        TorreArqueira t2 = loja.comprarTorreArqueira(banco, new Posicao(3, 4));
        jogo.adicionarTorre(t2);

        jogo.run();
    }
}
