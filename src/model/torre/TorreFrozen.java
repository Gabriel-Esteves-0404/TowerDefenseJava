package model.torre;

import model.Posicao;
import model.inimigos.Inimigos;

public class TorreFrozen extends Torre {

    public static final double CUSTO = 20.0;

    public TorreFrozen(Posicao posicao) {
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

        int duracaoCongelamento = switch (nivel) {
            case 1 -> 2;
            case 2 -> 3;
            case 3 -> 4;
            default -> 2;
        };

        Projetil p = new Projetil(this.dano, 1.0, this.posicao, alvo);
        p.configurarCongelamento(duracaoCongelamento);

        System.out.println("\n Torre Frozen disparou projétil congelante em " + alvo.getPosicaoAtual());

        this.cooldown = intervaloTiro;
        return p;
    }
}
