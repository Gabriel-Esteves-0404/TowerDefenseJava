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
        if (indiceCaminho >= mapa.getCaminho().size()) {
            System.out.println("Game Over!");
            return false;
        } else {
            posicaoAtual = mapa.getCaminho().get(indiceCaminho);
            System.out.println("Inimigo em: " + posicaoAtual); // <-- mostra a posição atual
            indiceCaminho = indiceCaminho + this.velocidade;
            return true;
        }
    }
        
    
    public Posicao getPosicaoAtual(){
        return posicaoAtual;
    }
    
    public boolean receberDano(Projetil projetil){
        this.vidaInimigo -= projetil.getDano();
        if(this.vidaInimigo <= 0){
        return true;
        } 
            return false;
    } 
    public int getDanoBase() {
        return danoBase;
    }
}




