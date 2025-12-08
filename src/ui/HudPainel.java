package ui;

import java.awt.*;
import javax.swing.*;
import jogo.GameLoop;

public class HudPainel extends JPanel {

    private final GameLoop jogo;
    private final JLabel lblVidaTitulo;
    private final JLabel lblOnda;
    private final JLabel lblMoedas;
    private final BaseHealthBar barraVida;

    public HudPainel(GameLoop jogo) {
        this.jogo = jogo;

        setOpaque(true);
        setBackground(new Color(16, 23, 33, 200));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 160), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        setPreferredSize(new Dimension(240, 120));

        lblVidaTitulo = criarLabel("Vida da base");
        lblOnda = criarLabel("Onda: 0");
        lblMoedas = criarLabel("Moedas: 0");

        barraVida = new BaseHealthBar();
        barraVida.setAlignmentX(Component.LEFT_ALIGNMENT);
        barraVida.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        add(lblVidaTitulo);
        add(Box.createVerticalStrut(6));
        add(barraVida);
        add(Box.createVerticalStrut(12));
        add(lblOnda);
        add(Box.createVerticalStrut(6));
        add(lblMoedas);

        atualizar();
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    public void atualizar() {
        barraVida.atualizar(jogo.getVidaBase(), jogo.getVidaBaseMax());
        lblOnda.setText("Onda: " + jogo.getIndiceOndaAtual());
        lblMoedas.setText("Moedas: " + (int) jogo.getBanco().getSaldo());
    }

    private static class BaseHealthBar extends JComponent {
        private int vidaAtual = 0;
        private int vidaMax = 1;

        public BaseHealthBar() {
            setPreferredSize(new Dimension(200, 18));
        }

        public void atualizar(int vidaAtual, int vidaMax) {
            this.vidaAtual = Math.max(0, vidaAtual);
            this.vidaMax = Math.max(1, vidaMax);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth();
            int h = getHeight();
            g2.setColor(new Color(170, 40, 40));
            g2.fillRect(0, 0, w, h);
            double proporcao = Math.min(1.0, Math.max(0, (double) vidaAtual / vidaMax));
            int larguraVerde = (int) Math.round(w * proporcao);
            g2.setColor(new Color(0, 170, 70));
            g2.fillRect(0, 0, larguraVerde, h);
            g2.setColor(new Color(0, 0, 0, 200));
            g2.drawRect(0, 0, w - 1, h - 1);

            String texto = vidaAtual + "/" + vidaMax;
            Font fonte = getFont().deriveFont(Font.BOLD, 12f);
            g2.setFont(fonte);
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(texto)) / 2;
            int ty = (h + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(Color.WHITE);
            g2.drawString(texto, tx, ty);

            g2.dispose();
        }
    }
}
