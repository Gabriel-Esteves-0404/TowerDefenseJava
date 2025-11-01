package model.torre;

import model.Posicao;

public class TorreArqueira extends Torre {
    public static final int CUSTO = 20;

    public TorreArqueira(Posicao posicao) {
        super(posicao, 2, 1.5, CUSTO, 0.0, 3);
    }
}
