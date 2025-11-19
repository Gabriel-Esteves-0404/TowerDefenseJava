package model.torre;

import model.Posicao;
import model.inimigos.Inimigos;

public class Projetil {
    private int dano;
    private double velocidade;
    private Posicao posicaoInicial;
    private Inimigos alvo;
    private boolean temVeneno;
    private int danoVenenoPorTick;
    private int duracaoVeneno;
    private boolean temCongelamento;
    private int duracaoCongelamento;

    public Projetil(int dano, double velocidade, Posicao posicaoInicial, Inimigos alvo){
        this.dano = dano;
        this.velocidade = velocidade;
        this.posicaoInicial = posicaoInicial;
        this.alvo = alvo;
    }

    public void configurarVeneno(int danoPorTick, int duracao) {
        this.temVeneno = true;
        this.danoVenenoPorTick = danoPorTick;
        this.duracaoVeneno = duracao;
    }

    public void configurarCongelamento(int duracao) {
        this.temCongelamento = true;
        this.duracaoCongelamento = duracao;
    }

    public void aplicarEfeitosNoAlvo() {
        if (temVeneno) {
            alvo.aplicarVeneno(danoVenenoPorTick, duracaoVeneno);
            System.out.println("\n Projétil venenoso aplicou veneno no inimigo em " + alvo.getPosicaoAtual());
        }
        if (temCongelamento) {
            alvo.aplicarCongelamento(duracaoCongelamento);
            System.out.println("\n Projétil congelante aplicou congelamento no inimigo em " + alvo.getPosicaoAtual());
        }
    }

    public Posicao atualizarPosicao(){
        double distancia = Math.sqrt(
            Math.pow(this.alvo.getPosicaoAtual().getColuna() - this.posicaoInicial.getColuna(), 2) +
            Math.pow(this.alvo.getPosicaoAtual().getLinha()  - this.posicaoInicial.getLinha(), 2)
        );

        if (distancia == 0) {
            return posicaoInicial;
        }

        double dcol   = alvo.getPosicaoAtual().getColuna() - this.posicaoInicial.getColuna();
        double dlinha = alvo.getPosicaoAtual().getLinha()  - this.posicaoInicial.getLinha();

        double normalcol   = dcol / distancia;
        double normallinha = dlinha / distancia;

        int linhaInt = (int) Math.round(this.posicaoInicial.getLinha() + normallinha * this.velocidade);
        int colunaInt = (int) Math.round(this.posicaoInicial.getColuna() + normalcol * this.velocidade);

        this.posicaoInicial = new Posicao(linhaInt, colunaInt);

        return posicaoInicial;
    }

    public boolean colidir(){
        return alvo.getPosicaoAtual().equals(this.posicaoInicial);
    }

    public int getDano(){
        return dano;
    }

    public Inimigos getAlvo() {
        return alvo;
    }
}
