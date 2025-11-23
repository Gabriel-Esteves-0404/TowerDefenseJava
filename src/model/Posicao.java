package model;

import model.excecoes.CoordenadaInvalidaException;

public class Posicao {

    // Atributos

    private final int linha;
    private final int coluna;

    // Construtor

    public Posicao(int linha, int coluna) throws CoordenadaInvalidaException{
        if(linha < 0){
            throw new CoordenadaInvalidaException("Linha inválida: "+ linha);
        }
        if(coluna < 0){
            throw new CoordenadaInvalidaException("Coluna inválida: " + coluna);
        }
        this.linha = linha;
        this.coluna = coluna;
        }

    // Métodos

    public int getLinha() {
        return linha;
    }
    
    public int getColuna() {
        return coluna;
    }

    public String toString() {
        return "(" + getLinha() + "," + getColuna() + ")"; 
    }

    // Sobrescrever o equals, para comparação do conteúdo

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Posicao)) return false;
        Posicao outra = (Posicao) obj;
            return this.linha == outra.linha && this.coluna == outra.coluna;
    }
    @Override
    public int hashCode() {
        int resultado = 17;          // valor inicial, primo
        resultado = 31 * resultado + linha;   // mistura a linha
        resultado = 31 * resultado + coluna;  // mistura a coluna
        return resultado;
    }
}