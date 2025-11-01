package model.economia;

public class Banco {
    private int saldo;

    public Banco(int saldo){
        this.saldo = saldo;
    }

    public boolean podeDebitar(int valor){
        if(this.saldo  >= valor){
            return true;
        }
        return false;
    }
    public int debitar(int valor){
        this.saldo -= valor;
        return saldo;
    }
    public int creditar(int valor){
        this.saldo += valor;
        return saldo;
    }
    public int getSaldo(){
        return saldo;
    }
    public void setSaldo(int novoSaldo) {
        this.saldo = novoSaldo;
    }
}
