package model.torre;

import model.Posicao;

public class ImpactoVisual {
    private final Posicao posicao;
    private final String origem;
    private int ticksRestantes;

    public ImpactoVisual(Posicao posicao, String origem, int duracaoTicks) {
        this.posicao = posicao;
        this.origem = origem;
        this.ticksRestantes = duracaoTicks;
    }

    public void tick() {
        ticksRestantes--;
    }

    public boolean expirou() {
        return ticksRestantes <= 0;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public String getOrigem() {
        return origem;
    }
}
