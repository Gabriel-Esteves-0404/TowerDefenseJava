package model.torre;

import java.awt.Image;
import model.Posicao;
import model.animacao.Animacao;
import model.animacao.CarregamentoSprites;
import model.inimigos.Inimigos;

public class TorreArqueira extends Torre {

    public static final double CUSTO = 10;
    private static final double VELOCIDADE_FLECHA = 1.6;
    private static final double VELOCIDADE_ROTACAO = 0.35;
    private static Posicao spawnReferencia;
    private final int danoBase = 2;
    private Animacao animDisparador;
    private double anguloAtualRad = -Math.PI / 2;
    private double anguloAlvoRad = -Math.PI / 2;

    public TorreArqueira(Posicao posicao) {
        super(posicao, 2, 2, CUSTO, 0, 3);
        this.dano = danoBase * 1;
        this.nivel = 1;
        apontarParaSpawn();
        carregarAnimacaoParaNivel(this.nivel);
    }

    private void carregarAnimacaoParaNivel(int nivel) {

        String base = "assets/torres/arqueira/disparadorArqueira/";

        String caminho;
        switch (nivel) {
            case 1 -> caminho = base + "Tower 01 - Level 01 - Weapon.png";
            case 2 -> caminho = base + "Tower 01 - Level 02 - Weapon.png";
            case 3 -> caminho = base + "Tower 01 - Level 03 - Weapon.png";
            default -> caminho = base + "Tower 01 - Level 01 - Weapon.png";
        }

        int ticksPorFrame =
            (nivel == 1) ? 3 :
            (nivel == 2) ? 2 :
                           1;

        Image[] frames = CarregamentoSprites.carregarStripHorizontal(caminho, 7);
        Image frameParado = frames != null && frames.length > 0 ? frames[0] : null;
        this.animDisparador = new Animacao(new Image[]{frameParado}, Integer.MAX_VALUE, false);
    }

    @Override
    protected void aplicarMelhoriasDoNivel() {

        if (nivel == 2) {
            this.dano = danoBase * 2;   
            this.alcance += 1;
            this.intervaloTiro -= 1;

        } else if (nivel == 3) {
            this.dano = danoBase * 3; 
            this.alcance += 1;
        }

        carregarAnimacaoParaNivel(nivel);
    }

    @Override
    public Projetil atirar(Inimigos alvo) {
        if (!podeAtirar() || alvo == null) return null;

        atualizarMira(alvo);
        Projetil p = new Projetil(this.dano, VELOCIDADE_FLECHA, this.posicao, alvo, "arqueira");
        this.cooldown = this.intervaloTiro;
        return p;
    }

    public void tickAnimacao() {
        double delta = normalizarAngulo(anguloAlvoRad - anguloAtualRad);
        double passo = Math.signum(delta) * Math.min(Math.abs(delta), VELOCIDADE_ROTACAO);
        anguloAtualRad = normalizarAngulo(anguloAtualRad + passo);
    }

    public void atualizarMira(Inimigos alvo) {
        if (alvo == null || alvo.getPosicaoAtual() == null) return;
        double alvoCol = alvo.getPosicaoAtual().getColuna() + 0.5;
        double alvoLin = alvo.getPosicaoAtual().getLinha() + 0.5;
        double torreCol = this.posicao.getColuna() + 0.5;
        double torreLin = this.posicao.getLinha() + 0.5;
        anguloAlvoRad = Math.atan2(alvoLin - torreLin, alvoCol - torreCol);
    }

    public double getAnguloAtualRad() {
        return anguloAtualRad;
    }

    public static void definirSpawnReferencia(Posicao spawn) {
        spawnReferencia = spawn;
    }

    private void apontarParaSpawn() {
        if (spawnReferencia == null) return;
        double spawnCol = spawnReferencia.getColuna() + 0.5;
        double spawnLin = spawnReferencia.getLinha() + 0.5;
        double torreCol = this.posicao.getColuna() + 0.5;
        double torreLin = this.posicao.getLinha() + 0.5;
        anguloAtualRad = anguloAlvoRad = Math.atan2(spawnLin - torreLin, spawnCol - torreCol);
    }

    private double normalizarAngulo(double ang) {
        while (ang <= -Math.PI) ang += Math.PI * 2;
        while (ang > Math.PI) ang -= Math.PI * 2;
        return ang;
    }

    public Image getFrameDisparador() {
        return animDisparador != null ? animDisparador.getFrameAtual() : null;
    }
}
