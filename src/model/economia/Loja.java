package model.economia;

import model.Posicao;
import model.torre.Torre;
import model.torre.TorreArqueira;

public class Loja {

    public Torre comprarTorreNormal(Banco banco, Posicao posicao) {
        double custo = 10.0; // custo fixo da torre normal
        if (banco.getSaldo() >= custo) {
            banco.setSaldo(banco.getSaldo() - custo);
            Torre torre = new Torre(posicao, 1, 5, custo, 0, 2);
            return torre;
        } else {
            System.out.println("Saldo insuficiente para comprar torre normal.");
            return null;
        }
    }

    public TorreArqueira comprarTorreArqueira(Banco banco, Posicao posicao) {
        double custo = TorreArqueira.CUSTO;
        if (banco.getSaldo() >= custo) {
            banco.setSaldo(banco.getSaldo() - custo);
            return new TorreArqueira(posicao);
        } else {
            System.out.println("Saldo insuficiente para comprar torre arqueira.");
            return null;
        }
    }
}
