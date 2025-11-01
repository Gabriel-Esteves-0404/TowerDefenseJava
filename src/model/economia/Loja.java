package model.economia;

import model.Posicao;
import model.torre.Torre;
import model.torre.TorreArqueira;

public class Loja {

    public Torre comprarTorreNormal(Banco banco, Posicao posicao) {
        int custo = 10; // custo fixo da torre normal
        if (banco.getSaldo() >= custo) {
            banco.setSaldo(banco.getSaldo() - custo);
            return new Torre(posicao, 1, 2.0, custo, 0.0, 2);
        } else {
            System.out.println("Saldo insuficiente para comprar torre normal.");
            return null;
        }
    }

    public TorreArqueira comprarTorreArqueira(Banco banco, Posicao posicao) {
        int custo = TorreArqueira.CUSTO;
        if (banco.getSaldo() >= custo) {
            banco.setSaldo(banco.getSaldo() - custo);
            return new TorreArqueira(posicao);
        } else {
            System.out.println("Saldo insuficiente para comprar torre arqueira.");
            return null;
        }
    }
}
