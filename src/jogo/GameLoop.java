package jogo;

import java.util.ArrayList;
import java.util.List;
import model.Mapa;
import model.economia.Banco;
import model.inimigos.Inimigos;
import model.torre.Projetil;
import model.torre.Torre;

public class GameLoop {
    private Mapa mapa;
    private Banco banco;
    private WaveManager ondas;
    private List<Inimigos> inimigosAtivos;
    private List<Torre> torresAtivas;
    private List<Projetil> projeteisAtivos;
    private int vidaBase;
    private int tick;
    private boolean jogoAtivo;

    public GameLoop(Mapa mapa, Banco banco, WaveManager ondas, int vidaBase){
        this.mapa = mapa;
        this.banco = banco;
        this.ondas = ondas;
        this.vidaBase = vidaBase;
        this.inimigosAtivos = new ArrayList<Inimigos>();
        this.torresAtivas = new ArrayList<Torre>();
        this.projeteisAtivos = new ArrayList<Projetil>();
        this.jogoAtivo = true;
        this.tick = 0;
    }

    public void run(){
        ondas.iniciarPrimeiraOnda(0);
        while (jogoAtivo) {
            tick();
        }
    }

    public void tick() {
        System.out.println("\n--- Tick " + tick + " ---");
        List<Inimigos> novos = ondas.spawnsDoTick(tick, mapa);
        if (novos != null && novos.size() > 0) {
            for (int i = 0; i < novos.size(); i++) {
                inimigosAtivos.add(novos.get(i));
            }
        }

        for (int i = 0; i < inimigosAtivos.size(); i++) {
            Inimigos inimigo = inimigosAtivos.get(i);
            boolean aindaNoCaminho = inimigo.movimentoInimigo(mapa);
            if (aindaNoCaminho == false) {
                vidaBase = vidaBase - inimigo.getDanoBase();
                inimigosAtivos.remove(i);
                i--;
            }
        }

        if (vidaBase <= 0) {
            jogoAtivo = false;
            return;
        }

        for (int j = 0; j < torresAtivas.size(); j++) {
            Torre torre = torresAtivas.get(j);
            torre.atualizarCooldown(1);
            if (torre.podeAtirar()) {
                List<Inimigos> alvos = torre.inimigosNoAlcance(inimigosAtivos);
                Inimigos alvo = torre.proximoAlvo(alvos);
                if (alvo != null) {
                boolean morreu = torre.atirar(alvo);
                System.out.println("Torre atirou no inimigo em " + alvo.getPosicaoAtual());
                if (morreu) {
                    System.out.println("Inimigo morto! +" + ondas.getRecompensaPorKill() + " moedas. Saldo=" + banco.getSaldo());
                    inimigosAtivos.remove(alvo);
                

                }
            }


                }
            }
        

        if (ondas.ondaConcluida(inimigosAtivos.size() == 0, true)) {
            jogoAtivo = false;
            System.out.println("VITÓRIA!");
            return;
        }
        System.out.println("Tick " + tick + 
                   " | Vida da base: " + vidaBase + 
                   " | Inimigos ativos: " + inimigosAtivos.size());


        tick++;
    }
    public void adicionarTorre(Torre t) {
    if (t != null) {
        torresAtivas.add(t);
    }
}

}
