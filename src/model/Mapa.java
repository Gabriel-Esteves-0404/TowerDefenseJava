package model;

import java.util.ArrayList;

public class Mapa {

    private final int linhas;
    private final int colunas;
    public boolean[][] mapa;
    private final ArrayList<Posicao> caminhoInimigo = new ArrayList<>();
    private final ArrayList<Posicao> areaBase = new ArrayList<>();
    private Posicao entradaBase;
    private Posicao base;

    public Mapa(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.mapa = new boolean[linhas][colunas];
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                mapa[i][j] = true;
            }
        }

        int linhaInicialBase = Math.max(1, linhas - 8);
        int colunaInicialBase = colunas - 7; 

        configurarAreaBase(linhaInicialBase, colunaInicialBase);
        construirCaminhoPrincipal(linhaInicialBase, colunaInicialBase);
    }

    private void construirCaminhoPrincipal(int linhaInicialBase, int colunaInicialBase) {
        int linhaSpawn = clamp(linhas / 2, 1, linhas - 2); 
        int colunaSpawn = 0;

        int colunaCurvaA = clamp((int) Math.round(colunas * 0.25), 2, colunaInicialBase - 5);
        int colunaCurvaB = clamp((int) Math.round(colunas * 0.50), colunaCurvaA + 2, colunaInicialBase - 4);
        int colunaCurvaC = clamp((int) Math.round(colunas * 0.75), colunaCurvaB + 2, colunaInicialBase - 3);
        int linhaAlta = clamp((int) Math.round(linhas * 0.3), 2, linhaSpawn - 2);
        int linhaBaixa = clamp((int) Math.round(linhas * 0.7), linhaAlta + 2, linhas - 3);

        int linhaEntrada = linhaBaixa;

        int[][] pontos = new int[][]{
                {linhaSpawn, colunaSpawn},
                {linhaSpawn, colunaCurvaA},
                {linhaAlta, colunaCurvaA},
                {linhaAlta, colunaCurvaB},
                {linhaBaixa, colunaCurvaB},
                {linhaBaixa, colunaCurvaC},
                {linhaEntrada, colunaCurvaC},
                {linhaEntrada, Math.max(0, colunaInicialBase - 1)},
                {linhaEntrada, colunaInicialBase},
                {linhaEntrada, Math.min(colunas - 1, colunaInicialBase + 1)} 
        };

        for (int i = 0; i < pontos.length - 1; i++) {
            int[] atual = pontos[i];
            int[] prox = pontos[i + 1];
            if (atual[0] == prox[0]) {
                caminhoHorizontal(atual[0], atual[1], prox[1]);
            } else if (atual[1] == prox[1]) {
                caminhoVertical(atual[1], atual[0], prox[0]);
            }
        }
    }

    private void configurarAreaBase(int linhaInicialBase, int colunaInicialBase) {
        int linhaFinalBase = clamp(linhaInicialBase + 5, linhaInicialBase, linhas - 1);
        int colunaFinalBase = clamp(colunaInicialBase + 5, colunaInicialBase, colunas - 1); 

        for (int r = linhaInicialBase; r <= linhaFinalBase; r++) {
            for (int c = colunaInicialBase; c <= colunaFinalBase; c++) {
                areaBase.add(new Posicao(r, c));
            }
        }

        entradaBase = new Posicao(linhaInicialBase + 3, Math.max(0, colunaInicialBase - 1));
        base = new Posicao(linhaInicialBase + 3, colunaInicialBase + 3);
    }

    private void adicionarCaminho(int linha, int coluna) {
        Posicao p = new Posicao(linha, coluna);
        if (caminhoInimigo.isEmpty() || !caminhoInimigo.get(caminhoInimigo.size() - 1).equals(p)) {
            caminhoInimigo.add(p);
        }
        marcarCaminhoLargo(linha, coluna);
    }

    private void marcarCaminhoLargo(int linha, int coluna) {
        Posicao p = new Posicao(linha, coluna);
        if (!areaBase.contains(p)) mapa[linha][coluna] = false;
        if (coluna - 1 >= 0 && !areaBase.contains(new Posicao(linha, coluna - 1))) mapa[linha][coluna - 1] = false;
        if (coluna + 1 < colunas && !areaBase.contains(new Posicao(linha, coluna + 1))) mapa[linha][coluna + 1] = false;
        if (linha - 1 >= 0 && !areaBase.contains(new Posicao(linha - 1, coluna))) mapa[linha - 1][coluna] = false;
        if (linha + 1 < linhas && !areaBase.contains(new Posicao(linha + 1, coluna))) mapa[linha + 1][coluna] = false;
    }

    private void caminhoHorizontal(int linha, int colInicio, int colFim) {
        if (colInicio <= colFim) {
            for (int c = colInicio; c <= colFim; c++) adicionarCaminho(linha, c);
        } else {
            for (int c = colInicio; c >= colFim; c--) adicionarCaminho(linha, c);
        }
    }

    private void caminhoVertical(int coluna, int linInicio, int linFim) {
        if (linInicio <= linFim) {
            for (int l = linInicio; l <= linFim; l++) adicionarCaminho(l, coluna);
        } else {
            for (int l = linInicio; l >= linFim; l--) adicionarCaminho(l, coluna);
        }
    }

    public boolean estaDentro(Posicao posicao) {
        return (0 <= posicao.getLinha() && posicao.getLinha() < this.linhas &&
                0 <= posicao.getColuna() && posicao.getColuna() < this.colunas);
    }

    public boolean ehConstruivel(Posicao pos) {
        int l = pos.getLinha();
        int c = pos.getColuna();
        return mapa[l][c];
    }

    public boolean podeConstruir(Posicao pos) {
        return ehConstruivel(pos) && !areaBase.contains(pos);
    }

    public Posicao getSpawn() {
        return caminhoInimigo.get(0);
    }

    public Posicao getBase() {
        return base;
    }

    public ArrayList<Posicao> getCaminho() {
        return caminhoInimigo;
    }

    public boolean isAreaBase(Posicao pos) {
        return areaBase.contains(pos);
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    private int clamp(int valor, int minimo, int maximo) {
        return Math.max(minimo, Math.min(maximo, valor));
    }
}
