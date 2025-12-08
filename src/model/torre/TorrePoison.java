package model.torre;

import java.awt.Image;
import model.Posicao;
import model.animacao.Animacao;
import model.animacao.CarregamentoSprites;
import model.inimigos.Inimigos;

public class TorrePoison extends Torre {

    public static final double CUSTO = 15.0;
    private Animacao animDisparador;

    public TorrePoison(Posicao posicao) {
        super(posicao, 2, 2, CUSTO, 0, 3);
        this.maxNivel = 3;
        carregarDisparadorAnimado();
    }

    @Override
    protected void aplicarMelhoriasDoNivel() {
        switch (nivel) {
            case 2 -> {
                this.dano += 1;
                this.alcance += 1;
            }
            case 3 -> {
                this.dano += 1;
                this.intervaloTiro = Math.max(1, this.intervaloTiro - 1);
            }
        }
    }

    @Override
    public Projetil atirar(Inimigos alvo) {
        if (!podeAtirar() || alvo == null) return null;

        int duracaoVeneno;
        int danoPorTick;

        switch (nivel) {
            case 1 -> {
                duracaoVeneno = 2;
                danoPorTick = Math.max(1, this.dano / 2);
            }
            case 2 -> {
                duracaoVeneno = 3;
                danoPorTick = this.dano;
            }
            case 3 -> {
                duracaoVeneno = 4;
                danoPorTick = this.dano + 1;
            }
            default -> {
                duracaoVeneno = 2;
                danoPorTick = this.dano / 2;
            }
        }

        Projetil p = new Projetil(this.dano, 1.1, this.posicao, alvo, "poison");
        p.configurarVeneno(danoPorTick, duracaoVeneno);

        System.out.println("\n Torre Poison disparou projetil venenoso em " + alvo.getPosicaoAtual());

        this.cooldown = intervaloTiro;
        if (animDisparador != null) animDisparador.reset();
        return p;
    }

    @Override
    public void tickAnimacao() {
        if (animDisparador != null && !animDisparador.terminou()) animDisparador.atualizar();
    }

    private void carregarDisparadorAnimado() {
        String base = "assets/torres/poison/disparadorPoison/";
        Image frame1 = carregarPrimeiroFrame(base + "disparador(1).png");
        Image frame2 = carregarPrimeiroFrame(base + "disparador(2).png");
        Image frame3 = carregarPrimeiroFrame(base + "disparador(3).png");
        Image frame4 = carregarPrimeiroFrame(base + "disparador(4).png");
        Image[] frames = new Image[]{frame1, frame2, frame3, frame4};
        animDisparador = new Animacao(frames, 1, false);
    }

    private Image carregarPrimeiroFrame(String caminho) {
        Image[] frames = CarregamentoSprites.carregarStripHorizontal(caminho, 1);
        return (frames != null && frames.length > 0) ? frames[0] : null;
    }

    public Image getFrameDisparador() {
        return animDisparador != null ? animDisparador.getFrameAtual() : null;
    }
}
