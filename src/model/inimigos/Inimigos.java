package model.inimigos;

import model.Mapa;
import model.Posicao;
import model.torre.Projetil;

public class Inimigos {

    //Atributos

    private int vidaInimigo;
    private int velocidade;
    private int indiceCaminho = 0;
    private Posicao posicaoAtual;
    private int danoBase;

    // Construtor

    public Inimigos(int vidaInimigo, int velocidade){ 
        this.vidaInimigo = vidaInimigo;
        this.velocidade = velocidade;
        this.danoBase = 1; 

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
        
    // Getters

    public Posicao getPosicaoAtual(){
        return posicaoAtual;
    }

    public int getDanoBase() {
        return danoBase;
    }

}




