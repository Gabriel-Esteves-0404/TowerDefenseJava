package model.inimigos;

import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import model.Mapa;
import model.Posicao;
import model.torre.Projetil;

public abstract class Inimigos {


    protected int vidaInimigo;
    protected int vidaMaxima;
    protected int velocidade;
    protected int indiceCaminho = 0;
    protected Posicao posicaoAtual;
    private boolean pausaAlternada = false;
    protected double multiplicadorVelocidade = 1.0;
    protected int danoBase;
    protected int recompensaPorKill;
    protected int ticksCongelado = 0;
    protected int ticksEnvenenado = 0;
    protected int danoVenenoPorTick = 0;
    protected Image[] framesWalk;
    protected int frameIndex = 0;
    protected int animTick = 0;

    public Inimigos(int vidaInimigo, int velocidade, int danoBase, int recompensaPorKill){
        this.vidaInimigo = vidaInimigo;
        this.vidaMaxima   = vidaInimigo;
        this.velocidade   = velocidade;
        this.danoBase     = danoBase;
        this.recompensaPorKill = recompensaPorKill;
    }

    protected void carregarFramesComNumeroFinal(String prefixo, int quantidade) {
        framesWalk = new Image[quantidade];
        for (int i = 1; i <= quantidade; i++) {
            String num = (i < 10 ? "0" + i : "" + i); // 01, 02, ...
            String path = "assets/inimigos/" + prefixo + num + ".png";
            java.io.File f = new java.io.File(path);
            System.out.println("Carregando " + path + " exists=" + f.exists() + " abs=" + f.getAbsolutePath());
            framesWalk[i - 1] = new ImageIcon(f.getAbsolutePath()).getImage();
        }
    }

    protected void carregarFramesComParenteses(String prefixo, int quantidade) {
        framesWalk = new Image[quantidade];
        for (int i = 1; i <= quantidade; i++) {
            String pathPadrao = "assets/inimigos/" + prefixo + "(" + i + ").png";
            java.io.File fPadrao = new java.io.File(pathPadrao);
            if (!fPadrao.exists()) {
                String pathEspaco = pathPadrao.replace("_(", "_ (");
                java.io.File fEspaco = new java.io.File(pathEspaco);
                System.out.println("Fallback espaco " + pathEspaco + " exists=" + fEspaco.exists() + " abs=" + fEspaco.getAbsolutePath());
                if (fEspaco.exists()) {
                    framesWalk[i - 1] = new ImageIcon(fEspaco.getAbsolutePath()).getImage();
                    continue;
                }

                String pathSemUnderscore = pathPadrao.replace("_(", "(");
                java.io.File fSemUnderscore = new java.io.File(pathSemUnderscore);
                System.out.println("Fallback sem underscore " + pathSemUnderscore + " exists=" + fSemUnderscore.exists() + " abs=" + fSemUnderscore.getAbsolutePath());
                if (fSemUnderscore.exists()) {
                    framesWalk[i - 1] = new ImageIcon(fSemUnderscore.getAbsolutePath()).getImage();
                    continue;
                }
            }
            System.out.println("Carregando " + pathPadrao + " exists=" + fPadrao.exists() + " abs=" + fPadrao.getAbsolutePath());
            framesWalk[i - 1] = new ImageIcon(fPadrao.getAbsolutePath()).getImage();
        }
    }

    public void atualizarAnimacao() {
        if (framesWalk == null || framesWalk.length == 0) return;

        int steps = 2;
        for (int i = 0; i < steps; i++) {
            frameIndex++;
            if (frameIndex >= framesWalk.length) {
                frameIndex = 0;
            }
        }
    }

    public Image getSpriteAtual() {
        if (framesWalk == null || framesWalk.length == 0) return null;
        return framesWalk[frameIndex];
    }


    public boolean movimentoInimigo(Mapa mapa) {
        ArrayList<Posicao> caminho = mapa.getCaminho();

        if (indiceCaminho >= caminho.size()) {
            posicaoAtual = mapa.getBase();
            System.out.println(getClass().getSimpleName()
                    + " chegou na base em " + posicaoAtual);
            return false;
        }

        posicaoAtual = caminho.get(indiceCaminho);

        pausaAlternada = !pausaAlternada;
        if (pausaAlternada) {
            return indiceCaminho < caminho.size();
        }

        double passoBase = Math.max(1, velocidade - 1) * multiplicadorVelocidade;
        int passo = Math.max(1, (int) Math.round(passoBase));
        indiceCaminho += passo;

        return indiceCaminho < caminho.size();
    }

    public boolean receberDano(Projetil projetil){
        this.vidaInimigo -= projetil.getDano();
        if (this.vidaInimigo < 0) this.vidaInimigo = 0;
        return this.vidaInimigo <= 0;
    }

    public void aplicarCongelamento(int duracaoTicks) {
        ticksCongelado = Math.max(ticksCongelado, duracaoTicks);
        System.out.println(getClass().getSimpleName() +
            " foi congelado por " + ticksCongelado + " ticks.");
    }

    public void aplicarVeneno(int danoPorTick, int duracaoTicks) {
        ticksEnvenenado = Math.max(ticksEnvenenado, duracaoTicks);
        danoVenenoPorTick = danoPorTick;

        System.out.println(getClass().getSimpleName() +
            " foi envenenado. Dano por tick: " + danoVenenoPorTick +
            ", duracao: " + ticksEnvenenado + " ticks.");
    }

    public void atualizarEfeitos() {
        if (ticksEnvenenado > 0) {
            this.vidaInimigo -= danoVenenoPorTick;
            if (this.vidaInimigo < 0) this.vidaInimigo = 0;

            System.out.println(getClass().getSimpleName() +
                " tomou " + danoVenenoPorTick +
                " de dano de veneno. Vida atual: " + this.vidaInimigo);

            ticksEnvenenado--;

            if (ticksEnvenenado == 0) {
                System.out.println(getClass().getSimpleName() +
                    " nao esta mais envenenado.");
            }
        }

        if (ticksCongelado > 0) {
            ticksCongelado--;

            System.out.println(getClass().getSimpleName() +
                " esta congelado (" + ticksCongelado + " ticks restantes).");

            if (ticksCongelado == 0) {
                System.out.println(getClass().getSimpleName() +
                    " nao esta mais congelado.");
            }
        }
    }

    public int getVidaAtual() {
        return vidaInimigo;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getDanoBase() {
        return danoBase;
    }

    public int getRecompensaPorKill(){
        return recompensaPorKill;
    }

    public boolean estaMorto() {
        return this.vidaInimigo <= 0;
    }

    public boolean estaCongelado() {
        return this.ticksCongelado > 0;
    }

    public Posicao getPosicaoAtual() {
        return posicaoAtual;
    }

    public int getIndiceCaminho() {
        return indiceCaminho;
    }

    public void posicionarEm(Posicao pos, int indiceCaminhoAtual) {
        this.posicaoAtual = pos;
        this.indiceCaminho = indiceCaminhoAtual;
    }
}
