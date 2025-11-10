package model.economia;

public class Banco {
    private double saldo;

    public Banco(double saldo){
        this.saldo = saldo;
    }

    public boolean podeDebitar(double valor){
        return(this.saldo  >= valor);
    }
    public double debitar(double valor){
        this.saldo -= valor;
        return saldo;
    }
    public double creditar(double valor){
        this.saldo += valor;
        return saldo;
    }
    public double getSaldo(){
        return saldo;
    }
    public void setSaldo(double novoSaldo) {
        this.saldo = novoSaldo;
    }
}
