package model.torre;

import model.Posicao;
import model.inimigos.Inimigos;

public class Projetil {
    
    private int dano;
    private double velocidade;
    private Posicao posicaoInicial;
    private Inimigos alvo;
    private String origem;
    private double dirCol;
    private double dirLinha;
    private boolean ativo = true;
    private double x; 
    private double y; 
    private double distanciaPercorrida = 0.0;
    private final double alcanceMaximo = 30.0;
    private boolean temVeneno;
    private int danoVenenoPorTick;
    private int duracaoVeneno;

    private boolean temCongelamento;
    private int duracaoCongelamento;

    public Projetil(int dano, double velocidade, Posicao posicaoInicial, Inimigos alvo, String origem) {
        this.dano = dano;
        this.velocidade = velocidade;
        this.posicaoInicial = posicaoInicial;
        this.alvo = alvo;
        this.origem = origem;

        this.x = posicaoInicial.getColuna() + 0.5;
        this.y = posicaoInicial.getLinha() + 0.5;

        recalcularDirecao();
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
            System.out.println("\nProjetil venenoso aplicou veneno no inimigo em " + alvo.getPosicaoAtual());
        }

        if (temCongelamento) {
            alvo.aplicarCongelamento(duracaoCongelamento);
            System.out.println("\nProjetil congelante aplicou congelamento no inimigo em " + alvo.getPosicaoAtual());
        }
    }

    public Posicao atualizarPosicao() {
        if (alvo == null || alvo.getPosicaoAtual() == null || alvo.estaMorto()) {
            ativo = false;
            return posicaoInicial;
        }

        recalcularDirecao();

        double alvoX = alvo.getPosicaoAtual().getColuna() + 0.5;
        double alvoY = alvo.getPosicaoAtual().getLinha() + 0.5;

        double distAteAlvo = Math.sqrt(Math.pow(alvoX - this.x, 2) + Math.pow(alvoY - this.y, 2));
        if (distAteAlvo <= velocidade) {
            this.x = alvoX;
            this.y = alvoY;
            this.posicaoInicial = new Posicao(alvo.getPosicaoAtual().getLinha(), alvo.getPosicaoAtual().getColuna());
            return posicaoInicial;
        }

        x += dirCol * velocidade;
        y += dirLinha * velocidade;
        distanciaPercorrida += velocidade;

        if (x < 0 || y < 0 || distanciaPercorrida > alcanceMaximo) {
            ativo = false;
            return posicaoInicial;
        }

        int linhaInt  = (int) Math.floor(y);
        int colunaInt = (int) Math.floor(x);


        if (linhaInt < 0) linhaInt = 0;
        if (colunaInt < 0) colunaInt = 0;

        this.posicaoInicial = new Posicao(linhaInt, colunaInt);
        return posicaoInicial;
    }

    public boolean colidir() {
        if (alvo == null || alvo.getPosicaoAtual() == null || alvo.estaMorto()) {
            ativo = false;
            return false;
        }

        double alvoX = alvo.getPosicaoAtual().getColuna() + 0.5;
        double alvoY = alvo.getPosicaoAtual().getLinha() + 0.5;
        double dx = alvoX - this.x;
        double dy = alvoY - this.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        double raioColisao = 0.35;
        boolean bateu = dist <= raioColisao;

        if (bateu) {

            this.posicaoInicial = new Posicao(alvo.getPosicaoAtual().getLinha(),
                                              alvo.getPosicaoAtual().getColuna());
            ativo = false;
        }

        return bateu;
    }

    private void recalcularDirecao() {
        double alvoX = alvo.getPosicaoAtual().getColuna() + 0.5;
        double alvoY = alvo.getPosicaoAtual().getLinha() + 0.5;

        double dx = alvoX - this.x;
        double dy = alvoY - this.y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist == 0) {
            this.dirCol = 0;
            this.dirLinha = 0;
        } else {
            this.dirCol   = dx / dist;
            this.dirLinha = dy / dist;
        }

        if (dist < 0.5) {
            this.dirCol *= 1.2;
            this.dirLinha *= 1.2;
        }
    }

    public int getDano() {
        return dano;
    }

    public Inimigos getAlvo() {
        return alvo;
    }

    public String getOrigem() {
        return origem;
    }

    public Posicao getPosicaoAtual() {
        return posicaoInicial;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDirX() {
        return dirCol;
    }

    public double getDirY() {
        return dirLinha;
    }

    public boolean isAtivo() {
        return ativo;
    }
}
