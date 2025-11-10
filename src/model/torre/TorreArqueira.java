package model.torre;

import model.Posicao;

public class TorreArqueira extends Torre {
    public static final double CUSTO = 20;

    public TorreArqueira(Posicao posicao) {
        super(posicao, 2, 1, CUSTO, 0, 3);
    }
}
