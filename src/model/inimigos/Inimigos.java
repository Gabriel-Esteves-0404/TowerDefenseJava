package model.inimigos;

import model.Mapa;
import model.Posicao;
import model.torre.Projetil;

public abstract class Inimigos {

    //Atributos

    protected  int vidaInimigo;
    protected int velocidade;
    protected int indiceCaminho = 0;
    protected Posicao posicaoAtual;
    protected int danoBase;
    protected int recompensaPorKill;
    protected int ticksCongelado = 0;
    protected int ticksEnvenenado = 0;
    protected int danoVenenoPorTick = 0;


    // Construtor

    public Inimigos(int vidaInimigo, int velocidade, int danoBase, int recompensaPorKill){ 
        this.vidaInimigo = vidaInimigo;
        this.velocidade = velocidade;
        this.danoBase = danoBase;
        this.recompensaPorKill = recompensaPorKill;

    }

    //Métodos

    public boolean movimentoInimigo(Mapa mapa) {
        if (indiceCaminho > mapa.getCaminho().size()-1) {
            posicaoAtual = mapa.getBase();
            return false;
        }

        posicaoAtual = mapa.getCaminho().get(indiceCaminho);
        indiceCaminho = indiceCaminho + this.velocidade;
        return true;
    
    }

        public boolean receberDano(Projetil projetil){
        this.vidaInimigo -= projetil.getDano();
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
            ", duração: " + ticksEnvenenado + " ticks.");
    }


    public void atualizarEfeitos() {
        if (ticksEnvenenado > 0) {
            this.vidaInimigo -= danoVenenoPorTick;

            System.out.println(getClass().getSimpleName() + 
                " tomou " + danoVenenoPorTick + 
                " de dano de veneno. Vida atual: " + this.vidaInimigo);

            ticksEnvenenado--;

            if (ticksEnvenenado == 0) {
                System.out.println(getClass().getSimpleName() + 
                    " não está mais envenenado.");
            }
        }

        if (ticksCongelado > 0) {
            ticksCongelado--;

            System.out.println(getClass().getSimpleName() + 
                " está congelado (" + ticksCongelado + " ticks restantes).");

            if (ticksCongelado == 0) {
                System.out.println(getClass().getSimpleName() + 
                    " não está mais congelado.");
            }
        }
    }

    public Posicao getPosicaoAtual(){
        return posicaoAtual;
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


}




