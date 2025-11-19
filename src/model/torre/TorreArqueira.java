package model.torre;

import model.Posicao;
import model.inimigos.Inimigos;

public class TorreArqueira extends Torre {

    public static final double CUSTO = 10;

    public TorreArqueira(Posicao posicao) {
        super(posicao, 7, 1, CUSTO, 0, 3);
    }

    @Override
    protected void aplicarMelhoriasDoNivel() {
        if (nivel == 2) {
            this.dano += 1;
            this.alcance += 1;
            this.intervaloTiro -= 1;
        } else if (nivel == 3) {
            this.dano += 2;
            this.alcance += 1;
        }
    }

    @Override
    public Projetil atirar(Inimigos alvo) {
    if (!podeAtirar() || alvo == null) return null;

    Projetil p = new Projetil(this.dano, 1.0, this.posicao, alvo);

    System.out.println("\n Torre Arqueira disparou projétil em " + alvo.getPosicaoAtual());

    this.cooldown = this.intervaloTiro;
    return p;
}

}
