package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import jogo.GameLoop;
import model.economia.Loja;

public class StartWindow extends JFrame {

    public StartWindow(GameLoop jogo, Loja loja) {
        setTitle("BitGuardians - Inicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon capaIcon = carregarCapa();
        int w = 1300, h = 700;
        JLabel background;
        if (capaIcon != null && capaIcon.getIconWidth() > 0) {
            int targetW = Math.min(w, Math.max(1100, capaIcon.getIconWidth()));
            int targetH = (int) (capaIcon.getIconHeight() * (targetW / (double) capaIcon.getIconWidth()));
            Image scaled = capaIcon.getImage().getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
            background = new JLabel(new ImageIcon(scaled));
            w = targetW;
            h = targetH;
            background.setLayout(new BorderLayout());
        } else {
            background = new JLabel();
            background.setOpaque(true);
            background.setBackground(new Color(26, 32, 44));
            background.setLayout(new BorderLayout());
        }
        background.setPreferredSize(new Dimension(w, h));

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 16));
        buttons.setBorder(BorderFactory.createEmptyBorder(12, 12, 24, 12));

        Font font = new Font("Segoe UI", Font.BOLD, 15);

        JButton btnStart = new JButton("Start");
        estilizar(btnStart, font);
        btnStart.addActionListener(e -> {
            new GameWindow(jogo, loja);
            dispose();
        });

        JButton btnTutorial = new JButton("Tutorial");
        estilizar(btnTutorial, font);
        btnTutorial.addActionListener(e -> mostrarTutorial());

        JButton btnExit = new JButton("Sair");
        estilizar(btnExit, font);
        btnExit.addActionListener(e -> System.exit(0));

        Dimension btnSize = new Dimension(180, 38);
        btnStart.setPreferredSize(btnSize);
        btnTutorial.setPreferredSize(btnSize);
        btnExit.setPreferredSize(btnSize);

        buttons.add(btnStart);
        buttons.add(btnTutorial);
        buttons.add(btnExit);

        background.add(buttons, BorderLayout.SOUTH);
        setContentPane(background);

        setPreferredSize(new Dimension(w, h));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private ImageIcon carregarCapa() {
        File f = Paths.get(System.getProperty("user.dir"), "assets", "capa", "capa.png").toFile();
        System.out.println("Carregando capa em " + f.getAbsolutePath() + " exists=" + f.exists());
        if (!f.exists()) return null;
        try {
            Image img = ImageIO.read(f);
            return new ImageIcon(img);
        } catch (Exception e) {
            System.out.println("Falha ao ler capa: " + e.getMessage());
            return null;
        }
    }

    private void mostrarTutorial() {
        JPanel container = new JPanel(new BorderLayout(0, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel intro = new JLabel("<html><b>Construa torres nas areas verdes.</b><br>" +
                "Caminho marrom = rota dos inimigos.<br>" +
                "Missao: defender a base e nao deixar os inimigos passarem.<br>" +
                "Use a Loja para comprar torres e Proxima Onda para iniciar.<br>" +
                "Upgrades fixos: N1->2 = 15 | N2->3 = 25 | N3->4 = 35.</html>");
        intro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        container.add(intro, BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout(0, 12));

        JPanel grid = new JPanel(new java.awt.GridLayout(2, 2, 8, 8));
        grid.setBorder(BorderFactory.createTitledBorder("Inimigos"));
        grid.add(cardInimigo("Zumbi", "Vida: 8", "Dano: 1", "Velocidade: 1",
                new String[]{"assets/inimigos/WALK_ZUMBI_01.png"}, null));
        grid.add(cardInimigo("Corredor", "Vida: 5", "Dano: 1", "Velocidade: 2",
                new String[]{"assets/inimigos/CORREDOR_RUN_(1).png"}, null));
        grid.add(cardInimigo("Blindado", "Vida: 6", "Dano: 2", "Velocidade: 1",
                new String[]{
                        "assets/inimigos/BLINDADO_WALK_ (1).png",},
                "Nao sofre efeitos especiais."));
        grid.add(cardInimigo("Golem", "Vida: 6", "Dano: 2", "Velocidade: 1",
                new String[]{
                        "assets/inimigos/GOLEM_WALK_ (1).png",
                        "assets/inimigos/GOLEM_WALK_(1).png"
                }, "Ao morrer, gera 2 golemitas."));

        JPanel torres = new JPanel(new java.awt.GridLayout(1, 3, 8, 8));
        torres.setBorder(BorderFactory.createTitledBorder("Torres"));
        torres.add(cardTorre("Torre Arqueira", "Dano: 2", "Cooldown: 2", "Alcance: 3", "Efeito: Flecha rapida", "assets/torres/arqueira/torreArqueira/Tower 03.png"));
        torres.add(cardTorre("Torre Poison", "Dano: 2", "Cooldown: 2", "Alcance: 3", "Efeito: Veneno continuo", "assets/torres/poison/torrePoison/Tower 02.png"));
        torres.add(cardTorre("Torre Frozen", "Dano: 2", "Cooldown: 2", "Alcance: 3", "Efeito: Congelamento", "assets/torres/frozen/torreFrozen/Tower 06.png"));

        main.add(grid, BorderLayout.CENTER);
        main.add(torres, BorderLayout.SOUTH);

        container.add(main, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                this,
                container,
                "Tutorial",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private JPanel cardInimigo(String nome, String vida, String dano, String vel, String[] caminhosIcone, String extra) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        p.setBackground(Color.WHITE);

        JLabel img = new JLabel();
        ImageIcon icon = carregarIcone(caminhosIcone);
        if (icon.getIconWidth() > 0) {
            Image scaled = icon.getImage().getScaledInstance(72, 72, Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(scaled));
        }
        img.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNome = new JLabel(nome);
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblVida = new JLabel(vida);
        JLabel lblDano = new JLabel(dano);
        JLabel lblVel = new JLabel(vel);

        lblVida.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDano.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblVel.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(img);
        p.add(Box.createVerticalStrut(6));
        p.add(lblNome);
        p.add(Box.createVerticalStrut(4));
        p.add(lblVida);
        p.add(lblDano);
        p.add(lblVel);
        if (extra != null) {
            JLabel lblExtra = new JLabel("<html><i>" + extra + "</i></html>");
            lblExtra.setAlignmentX(Component.LEFT_ALIGNMENT);
            p.add(lblExtra);
        }
        return p;
    }

    private JPanel cardTorre(String nome, String dano, String cooldown, String alcance, String efeito, String caminhoIcone) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        p.setBackground(Color.WHITE);

        JLabel img = new JLabel();
        ImageIcon icon = carregarIcone(new String[]{caminhoIcone});
        if (icon.getIconWidth() > 0) {
            Image scaled = icon.getImage().getScaledInstance(72, 72, Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(scaled));
        }
        img.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNome = new JLabel(nome);
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDano = new JLabel(dano);
        JLabel lblCd = new JLabel(cooldown);
        JLabel lblAlc = new JLabel(alcance);
        JLabel lblEfeito = new JLabel(efeito);

        lblDano.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblCd.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblAlc.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblEfeito.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(img);
        p.add(Box.createVerticalStrut(6));
        p.add(lblNome);
        p.add(Box.createVerticalStrut(4));
        p.add(lblDano);
        p.add(lblCd);
        p.add(lblAlc);
        p.add(lblEfeito);

        return p;
    }

    private void estilizar(JButton btn, Font font) {
        btn.setFont(font);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 73, 94));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 46, 60), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btn.setOpaque(true);
    }

    private ImageIcon carregarIcone(String[] caminhos) {
        for (String c : caminhos) {
            File f = new File(c);
            if (f.exists()) {
                return new ImageIcon(f.getAbsolutePath());
            }
        }
        if (caminhos.length > 0) {
            // fallback relativo
            ImageIcon rel = new ImageIcon(caminhos[0]);
            if (rel.getIconWidth() > 0) return rel;
        }
        return new ImageIcon();
    }
}
