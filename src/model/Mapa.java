package model;
import java.util.*; 
public class Mapa {

    // Atributos

    private final int linhas;
    private final int colunas;
    public boolean [][] mapa; // dimensão do mapa
    private final ArrayList<Posicao>caminhoInimigo = new ArrayList<>(); // Array dinâmico das coordenadas do caminho dos inimigos

    // Construtor

    public Mapa (int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.mapa = new boolean[linhas][colunas]; // criação da grid
        for(int i=0; i<linhas; i++) {
            for (int j=0;j<colunas;j++){
                mapa[i][j] = true; // tornando tudo construível
            }
        int linhaMeio = linhas / 2; // ex: 10/2 = 5
        caminhoHorizontal(linhaMeio, 0, 3); 
        caminhoVertical(3, linhaMeio - 1, 2);
        caminhoHorizontal(2, 4, 7);
        caminhoVertical(7, 3, linhaMeio); 
        caminhoHorizontal(linhaMeio, 8, colunas - 1);

    }
}
        // Métodos

                private void adicionarCaminho(int linha, int coluna) {
            Posicao p = new Posicao(linha, coluna);
            caminhoInimigo.add(p);
            mapa[linha][coluna] = false; // false = não construível (caminho)
        }

        private void caminhoHorizontal(int linha, int colInicio, int colFim) {
            if (colInicio <= colFim) {
                for (int c = colInicio; c <= colFim; c++) {
                    adicionarCaminho(linha, c);
                }
            } else {
                for (int c = colInicio; c >= colFim; c--) {
                    adicionarCaminho(linha, c);
                }
            }
        }

        private void caminhoVertical(int coluna, int linInicio, int linFim) {
            if (linInicio <= linFim) {
                for (int l = linInicio; l <= linFim; l++) {
                    adicionarCaminho(l, coluna);
                }
            } else {
                for (int l = linInicio; l >= linFim; l--) {
                    adicionarCaminho(l, coluna);
                }
            }
        }



        public boolean estaDentro (Posicao posicao) {
            return (0 <= posicao.getLinha() && posicao.getLinha() < this.linhas && 0 <= posicao.getColuna() && posicao.getColuna() < this.colunas); 
        } 
        public boolean ehConstruivel(Posicao pos) {
            int l = pos.getLinha();
            int c = pos.getColuna();
            return mapa[l][c]; // true = pode construir
        }


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