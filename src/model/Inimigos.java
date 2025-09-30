package model;


public class Inimigos {

    //Atributos

    private int vidaInimigo;
    private int velocidade;
    private int indiceCaminho = 0;
    private Posicao posicaoAtual;

    // Construtor

    public Inimigos(int vidaInimigo, int velocidade){ 
        this.vidaInimigo = vidaInimigo;
        this.velocidade = velocidade;

    }

    //Métodos

public void movimentoInimigos(Mapa mapa) {
    // Já terminou em tick anterior? nada a fazer
    if (indiceCaminho >= mapa.getCaminho().size()) return;

    // posição atual neste tick
    posicaoAtual = mapa.getCaminho().get(indiceCaminho);

    // chegou na base agora?
    if (posicaoAtual.equals(mapa.getBase())) {
        System.out.println("(Game Over)");
        indiceCaminho = mapa.getCaminho().size(); // trava no fim
        return;
    }

    // anda (mínimo 1 passo)
    indiceCaminho += Math.max(1, velocidade);

    // se ao avançar passou do fim, considera que atingiu a base
    if (indiceCaminho >= mapa.getCaminho().size()) {
        posicaoAtual = mapa.getBase();
        System.out.println("(Game Over)");
    }
}

    public Posicao getPosicaoAtual(){
        return posicaoAtual;
    }
}
    


