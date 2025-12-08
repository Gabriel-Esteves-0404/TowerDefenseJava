package jogo;

import java.util.ArrayList;
import java.util.List;
import model.Mapa;
import model.Posicao;
import model.economia.Banco;
import model.inimigos.Inimigos;
import model.inimigos.InimigosGolem;
import model.inimigos.InimigosGolemitas;
import model.torre.ImpactoVisual;
import model.torre.Projetil;
import model.torre.Torre;
import model.torre.TorreArqueira;

public class GameLoop {
    private Mapa mapa;
    private Banco banco;
    private WaveManager ondas;
    private List<Inimigos> inimigosAtivos;
    private List<Torre> torresAtivas;
    private List<Projetil> projeteisAtivos;
    private List<ImpactoVisual> impactosVisuais;
    private int vidaBase;
    private final int vidaBaseMax;
    private int tick;
    private boolean jogoAtivo;
    private boolean vitoria;
    private boolean ondaEmAndamento;

    public GameLoop(Mapa mapa, Banco banco, WaveManager ondas, int vidaBase){
        this.mapa = mapa;
        this.banco = banco;
        this.ondas = ondas;
        this.vidaBase = vidaBase;
        this.vidaBaseMax = vidaBase;
        this.inimigosAtivos = new ArrayList<>();
        this.torresAtivas = new ArrayList<>();
        this.projeteisAtivos = new ArrayList<>();
        this.impactosVisuais = new ArrayList<>();
        this.jogoAtivo = true;
        this.tick = 0;
        this.ondaEmAndamento = false;
    }

    public void tick() {
        System.out.println("\n--- Tick " + tick + " | Onda " + ondas.getIndiceOndaAtual() + " ---");

        List<Inimigos> inimigosParaAdicionar = new ArrayList<>();

        // atualiza inimigos
        for (int i = 0; i < inimigosAtivos.size(); i++) {
            Inimigos inimigo = inimigosAtivos.get(i);
            inimigo.atualizarEfeitos();
            inimigo.atualizarAnimacao();

            if (inimigo.estaMorto()) {
                tratarMorteInimigo(inimigo, inimigosParaAdicionar);
                inimigosAtivos.remove(i);
                i--;
                continue;
            }

            boolean aindaNoCaminho = true;
            if (!inimigo.estaCongelado()) {
                aindaNoCaminho = inimigo.movimentoInimigo(mapa);
            } else {
                System.out.println("\nInimigo esta sob efeito de slow.");
            }

            // 1.4) chegou na base
            if (!aindaNoCaminho) {
                System.out.println("Inimigo chegou na base ao sair do caminho! Dano: "
                        + inimigo.getDanoBase()
                        + " | Tipo: " + inimigo.getClass().getSimpleName());

                vidaBase -= inimigo.getDanoBase();
                if (vidaBase < 0) vidaBase = 0;
                System.out.println("Vida da base agora = " + vidaBase);

                inimigosAtivos.remove(i);
                i--;
            }
        }
        //checa gamer over
        if (vidaBase <= 0) {
            jogoAtivo = false;
            System.out.println("\nGAME OVER!!");
            this.vitoria = false;
            return;
        }

        // torres
        for (int j = 0; j < torresAtivas.size(); j++) {
            Torre torre = torresAtivas.get(j);
            torre.atualizarCooldown();

            List<Inimigos> alvos = torre.inimigosNoAlcance(inimigosAtivos);
            Inimigos alvo = torre.proximoAlvo(alvos);

            if (torre instanceof TorreArqueira arq) {
                arq.atualizarMira(alvo);
            }

            if (torre.podeAtirar() && alvo != null) {
                Projetil proj = torre.atirar(alvo);
                if (proj != null) {
                    projeteisAtivos.add(proj);
                    System.out.println("\nTorre atirou no inimigo em " + alvo.getPosicaoAtual());
                }
            }

            torre.tickAnimacao();
        }

        // projeteis
        for (int i = 0; i < projeteisAtivos.size(); i++) {
            Projetil p = projeteisAtivos.get(i);

            p.atualizarPosicao();

            if (!p.isAtivo()) {
                projeteisAtivos.remove(i);
                i--;
                continue;
            }

            if (p.colidir()) {
                Inimigos alvo = p.getAlvo();

                if (alvo != null && inimigosAtivos.contains(alvo)) {
                    boolean morreu = alvo.receberDano(p);
                    p.aplicarEfeitosNoAlvo();
                    if (morreu) {
                        tratarMorteInimigo(alvo, inimigosParaAdicionar);
                        inimigosAtivos.remove(alvo);
                    }
                }
                impactosVisuais.add(new ImpactoVisual(alvo.getPosicaoAtual(), p.getOrigem(), 1));
                projeteisAtivos.remove(i);
                i--;
            }
        }

        // adciona inimigos
        inimigosAtivos.addAll(inimigosParaAdicionar);

        List<Inimigos> novos = ondas.spawnNasOnda(tick);
        if (!novos.isEmpty()) {
            posicionarSpawnsIniciais(novos);
            inimigosAtivos.addAll(novos);
        }

        if (ondas.ondaConcluida(inimigosAtivos.isEmpty(), projeteisAtivos.isEmpty())) {
            ondaEmAndamento = false;

            if (ondas.ultimaOndaTerminou(inimigosAtivos.isEmpty(), projeteisAtivos.isEmpty())) {
                jogoAtivo = false;
                vitoria = true;
                System.out.println("\nVITORIA!");
                return;
            }
        }

 
        System.out.println("Tick " + tick +
                " | Onda: " + ondas.getIndiceOndaAtual() +
                " | Vida da base: " + vidaBase +
                " | Inimigos ativos: " + inimigosAtivos.size());

        tick++;
        
        for (int i = 0; i < impactosVisuais.size(); i++) {
            ImpactoVisual imp = impactosVisuais.get(i);
            imp.tick();
            if (imp.expirou()) {
                impactosVisuais.remove(i);
                i--;
            }
        }
    }

    public void adicionarTorre(Torre t) {
        if (t != null) {
            torresAtivas.add(t);
        }
    }

    public void iniciarOuProximaOnda() {
        if (ondaEmAndamento) {
            System.out.println("Onda ja em andamento. Aguarde terminar para iniciar a proxima.");
            return;
        }
        if (ondas.getIndiceOndaAtual() == 0) {
            ondas.iniciarOnda();
        } else {
            ondas.proximaOnda();
        }
        ondaEmAndamento = true;
    }

    public void run(){
        ondas.iniciarOnda();
        while (jogoAtivo) {
            tick();
        }
    }

    public List<Torre> listaAtivaTorres(){
        return torresAtivas;
    }

    public boolean SituacaoDoJogo(){
        return this.jogoAtivo;
    }

    public Mapa getMapa(){
        return mapa;
    }

    public Banco getBanco(){
        return banco;
    }

    public WaveManager getOndas(){
        return ondas;
    }

    public List<Inimigos> getInimigosAtivos(){
        return inimigosAtivos;
    }

    public List<Projetil> getProjeteisAtivos(){
        return projeteisAtivos;
    }

    public List<ImpactoVisual> getImpactosVisuais() {
        return impactosVisuais;
    }

    public boolean isOndaEmAndamento() {
        return ondaEmAndamento;
    }

    public boolean getVitoria(){
        return vitoria;
    }

    public int getVidaBase(){
        return vidaBase;
    }

    public int getVidaBaseMax() {
        return vidaBaseMax;
    }

    public int getIndiceOndaAtual(){
        return ondas.getIndiceOndaAtual();
    }

    public int getTick(){
        return tick;
    }

    private void posicionarSpawnsIniciais(List<Inimigos> novos) {
        ArrayList<Posicao> caminho = mapa.getCaminho();
        if (caminho == null || caminho.isEmpty()) return;
        for (int i = 0; i < novos.size(); i++) {
            Inimigos inimigo = novos.get(i);
            int idx = Math.min(i, caminho.size() - 1); // distribui as posições, pra não causar superposição
            Posicao pos = caminho.get(idx);
            inimigo.posicionarEm(pos, idx);
        }
    }

    private void adicionarGolemitas(Inimigos origem, List<Inimigos> listaNovos) {
        ArrayList<Posicao> caminho = mapa.getCaminho();
        if (caminho == null || caminho.isEmpty()) return;

        int idxOrigem = Math.max(0, origem.getIndiceCaminho());
        int idxFrente = Math.min(idxOrigem + 1, caminho.size() - 1);

        Posicao posOrigem = caminho.get(idxOrigem);
        Posicao posFrente = caminho.get(idxFrente);

        InimigosGolemitas g1 = new InimigosGolemitas();
        InimigosGolemitas g2 = new InimigosGolemitas();
        g1.posicionarEm(posOrigem, idxOrigem);
        g2.posicionarEm(posFrente, idxFrente);

        listaNovos.add(g1);
        listaNovos.add(g2);
    }

    private void tratarMorteInimigo(Inimigos inimigo, List<Inimigos> listaNovos) {
        double recompensa = inimigo.getRecompensaPorKill();
        double novoSaldo = banco.getSaldo() + recompensa;
        banco.setSaldo(novoSaldo);
        System.out.println("\nInimigo morto! +" + recompensa + " moedas. Saldo=" + novoSaldo);

        if (inimigo instanceof InimigosGolem) {
            adicionarGolemitas(inimigo, listaNovos);
            System.out.println("\n Foram Spawnados 2 golemitas");
        }
    }
}
