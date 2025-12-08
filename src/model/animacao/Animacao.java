package model.animacao;

import java.awt.Image;

public class Animacao {
    private Image[] frames;
    private int frameIndex = 0;
    private int tick = 0;
    private int ticksPorFrame;  // controla a “velocidade”
    private boolean loop;
    private boolean terminou = false;

    public Animacao(Image[] frames, int ticksPorFrame, boolean loop) {
        this.frames = frames;
        this.ticksPorFrame = ticksPorFrame;
        this.loop = loop;
    }

    public void atualizar() {
        if (terminou || frames == null || frames.length == 0) return;

        tick++;
        if (tick >= ticksPorFrame) {
            tick = 0;
            frameIndex++;

            if (frameIndex >= frames.length) {
                if (loop) {
                    frameIndex = 0;
                } else {
                    frameIndex = frames.length - 1;
                    terminou = true;
                }
            }
        }
    }

    public Image getFrameAtual() {
        if (frames == null || frames.length == 0) return null;
        return frames[frameIndex];
    }

    public boolean terminou() {
        return terminou;
    }

    public void reset() {
        frameIndex = 0;
        tick = 0;
        terminou = false;
    }

    public void setTicksPorFrame(int ticksPorFrame) {
        this.ticksPorFrame = ticksPorFrame;
    }
}
