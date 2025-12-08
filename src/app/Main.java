package app;

import javax.swing.SwingUtilities;
import jogo.GameLoop;
import jogo.WaveManager;
import model.Mapa;
import model.economia.Banco;
import model.economia.Loja;
import ui.GameWindow;
import ui.StartWindow;

public class Main {

    public static void main(String[] args) {

        System.out.println(">> INICIANDO MAIN GUI <<");  // DEBUG

        Mapa mapa = new Mapa(22, 43);
        Banco banco = new Banco(25);
        WaveManager ondas = new WaveManager();
        GameLoop game = new GameLoop(mapa, banco, ondas, 10);
        Loja loja = new Loja();

        SwingUtilities.invokeLater(() -> {
            new StartWindow(game, loja);
        });
    }
}
