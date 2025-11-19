package model.torre;

import model.Posicao;
import model.inimigos.Inimigos;

public class TorrePoison extends Torre {

    public static final double CUSTO = 15.0;

    public TorrePoison(Posicao posicao) {
        super(posicao, 5, 2, CUSTO, 0, 3);
        this.maxNivel = 3;
    }

    @Override
    protected void aplicarMelhoriasDoNivel() {
        switch (nivel) {
            case 2 -> {
                this.dano += 1;
                this.alcance += 1;
            }
            case 3 -> {
                this.dano += 1;
                this.intervaloTiro = Math.max(1, this.intervaloTiro - 1);
            }
        }
    }

    @Override
    public Projetil atirar(Inimigos alvo) {
        if (!podeAtirar() || alvo == null) return null;

        int duracaoVeneno;
        int danoPorTick;

        switch (nivel) {
            case 1 -> {
                duracaoVeneno = 2;
                danoPorTick = Math.max(1, this.dano / 2);
            }
            case 2 -> {
                duracaoVeneno = 3;
                danoPorTick = this.dano;
            }
            case 3 -> {
                duracaoVeneno = 4;
                danoPorTick = this.dano + 1;
            }
            default -> {
                duracaoVeneno = 2;
                danoPorTick = this.dano / 2;
            }
        }

        Projetil p = new Projetil(this.dano, 1.0, this.posicao, alvo);
        p.configurarVeneno(danoPorTick, duracaoVeneno);

        System.out.println("\n Torre Poison disparou projétil venenoso em " + alvo.getPosicaoAtual());

        this.cooldown = intervaloTiro;
        return p;
    }
}
