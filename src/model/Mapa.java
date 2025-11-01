package model;
import java.util.*; 
public class Mapa {

    // Atributos

    private final int linhas;
    private final int colunas;
    private boolean [][] mapa; // dimensão do mapa
    private ArrayList<Posicao>caminhoInimigo = new ArrayList<>(); // Array dinâmico das coordenadas do caminho dos inimigos

    // Construtor

    public Mapa (int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        if (linhas <= 0 || colunas <= 0 || colunas < 2){
            throw new IllegalArgumentException("Linhas e colunas devem ser não-negativos.");
        }
        this.mapa = new boolean[linhas][colunas]; // criação da grid
        for(int i=0; i<linhas; i++) {
            for (int j=0;j<colunas;j++){
                mapa[i][j] = true; // tornando tudo construível
            }
        }
        this.caminhoInimigo = new ArrayList<>(); // Criando caminhos dos inimigos
        int spawnCaminho = linhas/2; // Tornando caminho inicialmente no meio do mapa par
        for(int k=0;k<colunas;k++) {
                caminhoInimigo.add(new Posicao(spawnCaminho,k));
                mapa[spawnCaminho][k] = false; // Tornando os caminhos como False ( não podem construir)
        }
    }

        // Métodos

        public boolean estaDentro (Posicao posicao) {
            if (0 <= posicao.getLinha() && posicao.getLinha() < this.linhas && 0 <= posicao.getColuna() && posicao.getColuna() < this.colunas) {
                return true;
            }
            return false; 
        } // retorna true se a célula estiver dentro do mapa

        // getters

        public Posicao getSpawn () {
            return caminhoInimigo.get(0);         
        }

        public Posicao getBase(){
            return caminhoInimigo.get((caminhoInimigo.size())-1);
        }

        public ArrayList<Posicao> getCaminho() {
            return caminhoInimigo;
        }

        public int getLinhas(){
            return linhas;
        }
        public int getColunas(){
            return colunas;
        }
    }