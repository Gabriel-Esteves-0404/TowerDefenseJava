package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import jogo.GameLoop;
import model.economia.Loja;
import model.torre.TorreArqueira;
import model.torre.TorreFrozen;
import model.torre.TorrePoison;

public class GameWindow extends JFrame implements GamePainel.PlacementListener {

    public static boolean modoConstrucao = false;
    public static int tipoTorreSelecionada = -1; // 0=Arqueira, 1=Poison, 2=Frozen
    public static boolean modoUpgrade = false;

    private final GameLoop jogo;
    private final Loja loja;
    private final GamePainel gamePanel;
    private final HudPainel hudPanel;
    private Timer timer;
    private boolean pausado;
    private boolean travadoPorBanner;
    private boolean fimMostrado = false;

    private JLabel bannerOnda;
    private JPanel barraInferior;
    private JButton btnConfirmar;
    private JButton btnCancelar;
    private JButton btnNextWave;
    private JButton btnPause;
    private JButton btnLoja;

    public GameWindow(GameLoop jogo, Loja loja) {
        this.jogo = jogo;
        this.loja = loja;

        setTitle("BitGuardians - Tower Defense");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gamePanel = new GamePainel(jogo, loja);
        gamePanel.setPlacementListener(this);
        hudPanel = new HudPainel(jogo);
        TorreArqueira.definirSpawnReferencia(jogo.getMapa().getSpawn());

        JLayeredPane areaJogo = criarAreaJogo();
        setContentPane(areaJogo);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        iniciarLoopGrafico();
    }

    private JLayeredPane criarAreaJogo() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        Dimension gameSize = gamePanel.getPreferredSize();
        layeredPane.setPreferredSize(new Dimension(gameSize.width, gameSize.height));

        gamePanel.setBounds(0, 0, gameSize.width, gameSize.height);

        Dimension hudSize = hudPanel.getPreferredSize();
        int margin = 16;
        hudPanel.setBounds(margin, margin, hudSize.width, hudSize.height);

        bannerOnda = new JLabel("", SwingConstants.CENTER);
        bannerOnda.setFont(new Font("Segoe UI", Font.BOLD, 22));
        bannerOnda.setForeground(Color.WHITE);
        bannerOnda.setOpaque(true);
        bannerOnda.setBackground(new Color(0, 0, 0, 190));
        bannerOnda.setVisible(false);
        int bannerW = 200;
        int bannerH = 48;
        bannerOnda.setBounds((gameSize.width - bannerW) / 2, 20, bannerW, bannerH);

        barraInferior = criarBarraInferior(gameSize.width, gameSize.height);

        layeredPane.add(gamePanel, Integer.valueOf(0));
        layeredPane.add(hudPanel, Integer.valueOf(1));
        layeredPane.add(barraInferior, Integer.valueOf(2));
        layeredPane.add(bannerOnda, Integer.valueOf(3));

        return layeredPane;
    }

    private JPanel criarBarraInferior(int gameWidth, int gameHeight) {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        barra.setBackground(new Color(245, 248, 252));
        barra.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 220, 230)));
        int altura = 72;
        barra.setBounds(0, gameHeight - altura, gameWidth, altura);

        btnPause = criarBotaoNeutro("Pausar");
        btnPause.addActionListener(e -> {
            pausado = !pausado;
            btnPause.setText(pausado ? "Continuar" : "Pausar");
        });

        JButton btnExit = criarBotaoNeutro("Sair");
        btnExit.addActionListener(e -> System.exit(0));

        btnLoja = criarBotaoPrimario("Loja");
        btnLoja.addActionListener(e -> abrirLojaCustom());

        btnNextWave = criarBotaoAlerta("Iniciar Onda");
        btnNextWave.addActionListener(e -> exibirBannerEOnda());

        btnConfirmar = criarBotaoPrimario("Confirmar");
        btnConfirmar.setVisible(false);
        btnConfirmar.addActionListener(e -> gamePanel.confirmarConstrucao());

        btnCancelar = criarBotaoNeutro("Cancelar");
        btnCancelar.setVisible(false);
        btnCancelar.addActionListener(e -> {
            modoConstrucao = false;
            modoUpgrade = false;
            gamePanel.cancelarPreview();
            onPreviewChanged(false);
        });

        Dimension btnSize = new Dimension(140, 36);
        for (JButton b : new JButton[]{btnPause, btnExit, btnLoja, btnNextWave, btnConfirmar, btnCancelar}) {
            b.setPreferredSize(btnSize);
            barra.add(b);
        }
        return barra;
    }

    private JButton criarBotaoPrimario(String texto) {
        return estilizarBotao(texto, new Color(36, 123, 217), new Color(18, 92, 162));
    }

    private JButton criarBotaoNeutro(String texto) {
        return estilizarBotao(texto, new Color(64, 76, 96), new Color(43, 52, 64));
    }

    private JButton criarBotaoAlerta(String texto) {
        return estilizarBotao(texto, new Color(204, 54, 54), new Color(148, 28, 28));
    }

    private JButton estilizarBotao(String texto, Color corFundo, Color corBorda) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(corFundo);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(corBorda, 2),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        btn.setOpaque(true);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 36));
        return btn;
    }

    private void abrirLojaCustom() {
        JDialog dialog = new JDialog(this, "Loja de Torres", true);
        dialog.setLayout(new BorderLayout(12, 12));
        dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel titulo = new JLabel("Escolha a torre para comprar:");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dialog.add(titulo, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 3, 12, 0));
        cards.add(criarCardTorre(new TorreInfo(
                "Torre Arqueira", TorreArqueira.CUSTO, "Dano: 2", "Cooldown: 2 ticks",
                "Efeito: Flecha rapida", carregarPrimeiroNivel("assets/torres/arqueira/torreArqueira/Tower 03.png"), 0
        )));
        cards.add(criarCardTorre(new TorreInfo(
                "Torre Poison", TorrePoison.CUSTO, "Dano: 2", "Cooldown: 2 ticks",
                "Efeito: Veneno continuo", carregarPrimeiroNivel("assets/torres/poison/torrePoison/Tower 02.png"), 1
        )));
        cards.add(criarCardTorre(new TorreInfo(
                "Torre Frozen", TorreFrozen.CUSTO, "Dano: 2", "Cooldown: 2 ticks",
                "Efeito: Congelamento", carregarPrimeiroNivel("assets/torres/frozen/torreFrozen/Tower 06.png"), 2
        )));

        dialog.add(cards, BorderLayout.CENTER);
        dialog.add(criarRodapeUpgrade(dialog), BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel criarRodapeUpgrade(JDialog dialog) {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JButton btnUpgrade = criarBotaoNeutro("Upar Torre existente");
        btnUpgrade.addActionListener(e -> {
            dialog.setVisible(false);
            modoUpgrade = true;
            modoConstrucao = false;
            JOptionPane.showMessageDialog(
                    this,
                    "Clique na torre que deseja upar.",
                    "Upgrade de Torre",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        JLabel custoInfo = new JLabel("Upgrade: N1->2 = 15 | N2->3 = 25 | N3->4 = 35");
        custoInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rodape.add(custoInfo, BorderLayout.WEST);
        rodape.add(btnUpgrade, BorderLayout.EAST);
        return rodape;
    }

    private JPanel criarCardTorre(TorreInfo info) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 40, 60), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(new Color(245, 248, 252));

        JLabel imgLabel = new JLabel();
        if (info.iconePrimeiroNivel() != null) {
            Image scaledImg = info.iconePrimeiroNivel().getScaledInstance(96, 120, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(scaledImg));
        }
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nome = new JLabel(info.nome() + " - " + info.custo());
        nome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dano = new JLabel(info.dano());
        JLabel cd = new JLabel(info.cooldown());
        JLabel efeito = new JLabel(info.efeito());
        dano.setAlignmentX(Component.LEFT_ALIGNMENT);
        cd.setAlignmentX(Component.LEFT_ALIGNMENT);
        efeito.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton selecionar = new JButton("Selecionar");
        selecionar.setAlignmentX(Component.CENTER_ALIGNMENT);
        selecionar.addActionListener(e -> {
            GameWindow.modoConstrucao = true;
            GameWindow.tipoTorreSelecionada = info.tipo();
            gamePanel.cancelarPreview();
            JOptionPane.showMessageDialog(
                    this,
                    "Clique em uma celula verde para visualizar o alcance.\nDepois confirme para comprar.",
                    "Posicionamento",
                    JOptionPane.INFORMATION_MESSAGE
            );
            selecionar.getTopLevelAncestor().setVisible(false);
        });

        card.add(imgLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(nome);
        card.add(Box.createVerticalStrut(6));
        card.add(dano);
        card.add(cd);
        card.add(efeito);
        card.add(Box.createVerticalStrut(12));
        card.add(selecionar);
        return card;
    }

    private void exibirBannerEOnda() {
        if (jogo.isOndaEmAndamento()) {
            return;
        }
        travadoPorBanner = true;
        btnNextWave.setEnabled(false);
        jogo.iniciarOuProximaOnda();
        int indice = jogo.getIndiceOndaAtual();
        bannerOnda.setText("ONDA " + indice);
        bannerOnda.setVisible(true);

        Timer t = new Timer(1000, e -> {
            travadoPorBanner = false;
            bannerOnda.setVisible(false);
            btnNextWave.setEnabled(!jogo.isOndaEmAndamento());
        });
        t.setRepeats(false);
        t.start();
    }

    private void iniciarLoopGrafico() {
        int delayMs = 300;

        timer = new Timer(delayMs, e -> {
            if (!jogo.SituacaoDoJogo()) {
                timer.stop();
                if (!fimMostrado) {
                    fimMostrado = true;
                    mostrarTelaFinal();
                }
                return;
            }

            if (!pausado && jogo.isOndaEmAndamento() && !travadoPorBanner) {
                jogo.tick();
            }

            btnNextWave.setEnabled(!jogo.isOndaEmAndamento() && !travadoPorBanner);
            btnLoja.setEnabled(!jogo.isOndaEmAndamento());
            hudPanel.atualizar();
            gamePanel.repaint();
        });

        timer.start();
    }

    @Override
    public void onPreviewChanged(boolean ativo) {
        btnConfirmar.setVisible(ativo);
        btnCancelar.setVisible(ativo);
        btnConfirmar.setEnabled(ativo);
        btnCancelar.setEnabled(ativo);
    }

    private void mostrarTelaFinal() {
        String msg = jogo.getVitoria()
                ? "VITORIA"
                : "GAME OVER";

        JLabel overlay = new JLabel(msg, SwingConstants.CENTER);
        overlay.setFont(new Font("Segoe UI", Font.BOLD, 42));
        overlay.setOpaque(true);
        overlay.setForeground(jogo.getVitoria() ? new Color(218, 165, 32) : Color.RED);
        overlay.setBackground(new Color(0, 0, 0, 190));

        int w = 280;
        int h = 90;
        int x = (getWidth() - w) / 2;
        int y = (getHeight() - h) / 2;
        overlay.setBounds(x, y, w, h);

        JLayeredPane layered = (JLayeredPane) getContentPane();
        layered.add(overlay, Integer.valueOf(4));
        layered.revalidate();
        layered.repaint();
    }

    private Image carregarPrimeiroNivel(String sheetPath) {
        try {
            BufferedImage img = ImageIO.read(new File(sheetPath));
            if (img == null) return null;
            int cols = 3;
            int tileW = img.getWidth() / cols;
            int tileH = img.getHeight();
            return img.getSubimage(0, 0, tileW, tileH);
        } catch (Exception e) {
            System.out.println("Falha ao recortar " + sheetPath + ": " + e.getMessage());
            return null;
        }
    }

    private record TorreInfo(String nome, double custo, String dano, String cooldown,
                             String efeito, Image iconePrimeiroNivel, int tipo) {
    }
}
