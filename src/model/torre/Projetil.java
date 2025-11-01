package model.torre;
import model.Posicao;
import model.inimigos.Inimigos;

public class Projetil {
    private int dano;
    private double velocidade;
    private Posicao posicaoInicial;
    private Inimigos alvo;

    public Projetil(int dano, double velocidade, Posicao posicaoInicial, Inimigos alvo){
        this.dano = dano;
        this.velocidade = velocidade;
        this.posicaoInicial = posicaoInicial;
        this.alvo = alvo;
    }
    public Posicao atualizarPosicao(){
        double distancia = Math.sqrt(Math.pow(this.alvo.getPosicaoAtual().getColuna() - this.posicaoInicial.getColuna(),2) + Math.pow(this.alvo.getPosicaoAtual().getLinha() - this.posicaoInicial.getLinha(),2));

        double dcol = alvo.getPosicaoAtual().getColuna() - this.posicaoInicial.getColuna();

        double dlinha = alvo.getPosicaoAtual().getLinha() - this.posicaoInicial.getLinha();

        double normalcol = dcol / distancia;
        double normallinha = dlinha / distancia;

        int linhaInt = (int) Math.round(this.posicaoInicial.getLinha() + normallinha * this.velocidade);

        int colunaInt = (int) Math.round(this.posicaoInicial.getColuna() + normalcol * this.velocidade);

        this.posicaoInicial = new Posicao(linhaInt, colunaInt);

        return posicaoInicial;
    }

    public boolean colidir(){
        if(alvo.getPosicaoAtual().equals(this.posicaoInicial)){
            return true;
        }
        return false;
    }




    public int getDano(){
        return dano;
    }


}
