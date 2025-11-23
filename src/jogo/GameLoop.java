package jogo;

import java.util.ArrayList;
import java.util.List;
import model.Mapa;
import model.economia.Banco;
import model.inimigos.Inimigos;
import model.inimigos.InimigosGolem;
import model.inimigos.InimigosGolemitas;
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

    public void tick() {
        System.out.println("\n--- Tick " + tick + " | Onda " + ondas.getIndiceOndaAtual() + " ---");
     
        List<Inimigos> inimigosParaAdicionar = new ArrayList<>();

        
        for (int i = 0; i < inimigosAtivos.size(); i++) {
            Inimigos inimigo = inimigosAtivos.get(i);    
            inimigo.atualizarEfeitos();

            if (inimigo.estaMorto()) {
                double recompensa = inimigo.getRecompensaPorKill();
                double novoSaldo = banco.getSaldo() + recompensa;
                banco.setSaldo(novoSaldo); 
                
                if (inimigo instanceof InimigosGolem) {
                    inimigosParaAdicionar.add(new InimigosGolemitas());
                    inimigosParaAdicionar.add(new InimigosGolemitas());
                    System.out.println("\n Foram Spawnados 2 golemitas (morte por veneno)");
                }
                
                inimigosAtivos.remove(i);
                System.out.println("\nInimigo morto pelo veneno");
                System.out.println(
                    "\nInimigo morto! +" + recompensa + " moedas. Saldo=" + novoSaldo
                );
                i--;
                continue;
            }

            boolean aindaNoCaminho = true;
            if (!inimigo.estaCongelado()) {
                aindaNoCaminho = inimigo.movimentoInimigo(mapa);
            } 
            else {
                System.out.println("\nInimigo está sob efeito de slow.");
            }
            
            if (!aindaNoCaminho) {
                vidaBase = vidaBase - inimigo.getDanoBase();
                inimigosAtivos.remove(i);
                i--; 
            }
        }

        
        if (vidaBase <= 0) {
            jogoAtivo = false;
            System.out.println("\nGAME OVER!!");
            return;
        }

        
        for (int j = 0; j < torresAtivas.size(); j++) {
            Torre torre = torresAtivas.get(j);
            torre.atualizarCooldown();

            if (torre.podeAtirar()) {
                List<Inimigos> alvos = torre.inimigosNoAlcance(inimigosAtivos);
                Inimigos alvo = torre.proximoAlvo(alvos);
                if (alvo != null) {
                    Projetil proj = torre.atirar(alvo);
                    if (proj != null) {
                        projeteisAtivos.add(proj);
                        System.out.println("\nTorre atirou no inimigo em " + alvo.getPosicaoAtual());
                    }
                }
            }
        }

        
        for (int i = 0; i < projeteisAtivos.size(); i++) {
            Projetil p = projeteisAtivos.get(i);
            p.atualizarPosicao();

            if (p.colidir()) {
                Inimigos alvo = p.getAlvo();

                if (alvo != null && inimigosAtivos.contains(alvo)) {
                    boolean morreu = alvo.receberDano(p);
                    p.aplicarEfeitosNoAlvo();
                    if (morreu) {
                        double recompensa = alvo.getRecompensaPorKill();
                        double novoSaldo = banco.getSaldo() + recompensa;
                        System.out.println(
                            "\nInimigo morto! +" + recompensa + " moedas. Saldo=" + novoSaldo
                        );

                        banco.setSaldo(novoSaldo);

                        if (alvo instanceof InimigosGolem) {
                            inimigosParaAdicionar.add(new InimigosGolemitas());
                            inimigosParaAdicionar.add(new InimigosGolemitas());
                            System.out.println("\n Foram Spawnados 2 golemitas");
                        }

                        inimigosAtivos.remove(alvo);
                    }
                }
                projeteisAtivos.remove(i);
                i--;
            }
        }


        inimigosAtivos.addAll(inimigosParaAdicionar);
        List<Inimigos> novos = ondas.spawnNasOnda(tick);
        if (!novos.isEmpty()) {
            for (int i = 0; i < novos.size(); i++) {
                inimigosAtivos.add(novos.get(i));
            }
        }
        
        if (ondas.ultimaOndaTerminou(inimigosAtivos.isEmpty(), projeteisAtivos.isEmpty())) {
            jogoAtivo = false;
            System.out.println("\nVITÓRIA!");
            return;
        }

        System.out.println("Tick " + tick + 
        " | Onda: " + ondas.getIndiceOndaAtual() +
        " | Vida da base: " + vidaBase + 
        " | Inimigos ativos: " + inimigosAtivos.size());

        tick++;
    }

    public void rodarUmaOnda() {

        while (jogoAtivo && 
            !ondas.ondaConcluida(inimigosAtivos.isEmpty(), projeteisAtivos.isEmpty())) {

            tick();

            if (ondas.ultimaOndaTerminou(inimigosAtivos.isEmpty(), projeteisAtivos.isEmpty())) {
                jogoAtivo = false;
                System.out.println("\nVITÓRIA!");
                return;
            }
        }

        if (jogoAtivo) {
            System.out.println("\n--- Fim da Onda " + ondas.getIndiceOndaAtual() + " ---");
        }
    }



    public void adicionarTorre(Torre t) {
        if (t != null) {
            torresAtivas.add(t);
        }
    }

    public List listaAtivaTorres(){
        return torresAtivas;
    }

    public boolean SituacaoDoJogo(){
        return this.jogoAtivo;
    }

    public int getVidaBase(){
        return vidaBase;
    }

    public void run(){
        ondas.iniciarOnda();
        while (jogoAtivo) {
            tick();
        }
    }

}
