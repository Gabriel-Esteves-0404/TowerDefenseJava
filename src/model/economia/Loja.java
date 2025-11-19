package model.economia;

import model.Posicao;
import model.torre.TorreArqueira;
import model.torre.TorreFrozen;
import model.torre.TorrePoison;

public class Loja {

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
    
    public TorrePoison comprarTorrePoison(Banco banco, Posicao posicao) {
        double custo = TorreArqueira.CUSTO;
        if (banco.getSaldo() >= custo) {
            banco.setSaldo(banco.getSaldo() - custo);
            return new TorrePoison(posicao);
        } else {
            System.out.println("Saldo insuficiente para comprar torre arqueira.");
            return null;
        }
    }
    
    public TorreFrozen comprarTorreFrozen(Banco banco, Posicao posicao) {
        double custo = TorreArqueira.CUSTO;
        if (banco.getSaldo() >= custo) {
            banco.setSaldo(banco.getSaldo() - custo);
            return new TorreFrozen(posicao);
        } else {
            System.out.println("Saldo insuficiente para comprar torre arqueira.");
            return null;
        }
    }
}
