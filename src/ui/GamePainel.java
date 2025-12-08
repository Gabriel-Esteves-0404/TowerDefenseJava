package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import jogo.GameLoop;
import model.Mapa;
import model.Posicao;
import model.economia.Banco;
import model.economia.Loja;
import model.excecoes.NivelMaximoException;
import model.excecoes.SaldoInsuficienteException;
import model.inimigos.Inimigos;
import model.torre.ImpactoVisual;
import model.torre.Projetil;
import model.torre.Torre;
import model.torre.TorreArqueira;
import model.torre.TorreFrozen;
import model.torre.TorrePoison;

public class GamePainel extends JPanel {

    private final GameLoop jogo;
    private final int cellSize = 32;
    private final Loja loja;
    private Image imgZumbi;
    private Image imgGolem;
    private Image[] framesTorreArqueira; 
    private Image[] framesTorrePoison;   
    private Image[] framesTorreFrozen;   
    private Image disparadorArqueira;
    private Image disparadorPoison;
    private Image disparadorFrozen;
    private Image imgProjetilArqueira;
    private Image imgProjetilPoison;
    private Image imgProjetilFrozen;
    private Image imgImpactoArqueira;
    private Image imgImpactoPoison;
    private Image imgImpactoFrozen;
    private Image tileTerreno;
    private Image tileCaminho;
    private List<DecoracaoMapa> decoracoes;
    private Image imgBaseCastelo;
    private Posicao previewPosicao;
    private int previewAlcance;
    private boolean previewAtiva;
    private PlacementListener placementListener;

    public GamePainel(GameLoop jogo, Loja loja) {
        this.jogo = jogo;
        this.loja = loja;

        carregarSpritesTorres();
        carregarTilesMapa();
        carregarDecoracoesMapa();
        imgBaseCastelo = carregarImagem("assets/base/base.png");
        imgZumbi = carregarImagem("assets/inimigos/WALK_ZUMBI_01.png");
        imgGolem = carregarImagem("assets/inimigos/GOLEM_WALK_ (1).png"); 
        Mapa mapa = jogo.getMapa();
        int linhas = mapa.getLinhas();
        int colunas = mapa.getColunas();

        setPreferredSize(new Dimension(colunas * cellSize, linhas * cellSize));
        setBackground(Color.BLACK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    cancelarPreview();
                    return;
                }
                int coluna = e.getX() / cellSize;
                int linha  = e.getY() / cellSize;

                Posicao pos = new Posicao(linha, coluna);

                if (GameWindow.modoUpgrade) {
                    tentarUpgrade(pos);
                    return;
                }

                if (!GameWindow.modoConstrucao) return;
                if (GameWindow.tipoTorreSelecionada < 0) return;

                if (previewAtiva && previewPosicao != null && previewPosicao.equals(pos)) {
                    confirmarConstrucao();
                    return;
                }

                if (!jogo.getMapa().podeConstruir(pos)) {
                    JOptionPane.showMessageDialog(
                        GamePainel.this,
                        "Nao e possivel construir aqui.",
                        "Construcao invalida",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                previewPosicao = pos;
                previewAlcance = obterAlcanceParaTipo(GameWindow.tipoTorreSelecionada);
                previewAtiva = true;
                notificarPreview();
                repaint();
            }
        });
    }

    public void setPlacementListener(PlacementListener listener) {
        this.placementListener = listener;
    }

    private void notificarPreview() {
        if (placementListener != null) {
            placementListener.onPreviewChanged(previewAtiva);
        }
    }

    public void cancelarPreview() {
        previewAtiva = false;
        previewPosicao = null;
        notificarPreview();
        repaint();
    }

    public boolean confirmarConstrucao() {
        if (!previewAtiva || previewPosicao == null) return false;

        Banco banco = jogo.getBanco();
        Torre nova = switch (GameWindow.tipoTorreSelecionada) {
            case 0 -> loja.comprarTorreArqueira(banco, previewPosicao);
            case 1 -> loja.comprarTorrePoison(banco, previewPosicao);
            case 2 -> loja.comprarTorreFrozen(banco, previewPosicao);
            default -> null;
        };

        if (nova == null) {
            JOptionPane.showMessageDialog(
                this,
                "Moedas insuficientes para comprar essa torre.",
                "Saldo insuficiente",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        jogo.adicionarTorre(nova);
        previewAtiva = false;
        previewPosicao = null;
        GameWindow.modoConstrucao = false;
        GameWindow.tipoTorreSelecionada = -1;
        notificarPreview();
        repaint();
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Mapa mapa = jogo.getMapa();

        desenharGrid(g, mapa);
        desenharDecoracoes(g);
        desenharCasteloBase(g, mapa.getBase());
        desenharTorres(g, jogo.listaAtivaTorres());
        desenharInimigos(g, jogo.getInimigosAtivos());
        desenharProjeteis(g, jogo.getProjeteisAtivos());
        desenharImpactos(g, jogo.getImpactosVisuais());
        desenharPreviewAlcance(g);
    }

    private Image carregarImagem(String caminhoRelativo) {
        ImageIcon icon = new ImageIcon(caminhoRelativo);
        System.out.println(
            "Carregando " + caminhoRelativo +
            " -> largura=" + icon.getIconWidth() +
            ", altura=" + icon.getIconHeight()
        );
        return icon.getImage();
    }

    private void desenharGrid(Graphics g, Mapa mapa) {
        int linhas = mapa.getLinhas();
        int colunas = mapa.getColunas();

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                int x = j * cellSize;
                int y = i * cellSize;

                Posicao pos = new Posicao(i, j);
                boolean terrenoVerde = mapa.ehConstruivel(pos);

                Image tile = terrenoVerde ? tileTerreno : tileCaminho;
                if (tile != null) {
                    g.drawImage(tile, x, y, cellSize, cellSize, this);
                } else {
                    g.setColor(terrenoVerde ? new Color(0, 100, 0) : Color.GRAY);
                    g.fillRect(x, y, cellSize, cellSize);
                }
                g.setColor(new Color(0, 0, 0, 60));
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
    }

    private Image[] carregarSheet3Niveis(String caminho) throws Exception {
        BufferedImage sheet = ImageIO.read(new File(caminho));
        int cols = 3;
        int tileW = sheet.getWidth() / cols;
        int tileH = sheet.getHeight();

        Image[] frames = new Image[3];
        for (int i = 0; i < cols; i++) {
            frames[i] = sheet.getSubimage(i * tileW, 0, tileW, tileH);
        }
        return frames;
    }

    private int obterAlcanceParaTipo(int tipo) {
        return switch (tipo) {
            case 0 -> new TorreArqueira(new Posicao(0, 0)).getAlcance();
            case 1 -> new TorrePoison(new Posicao(0, 0)).getAlcance();
            case 2 -> new TorreFrozen(new Posicao(0, 0)).getAlcance();
            default -> 0;
        };
    }

    private void carregarSpritesTorres() {
        try {
            framesTorreArqueira = carregarSheet3Niveis("assets/torres/arqueira/torreArqueira/Tower 03.png");
        } catch (Exception e) {
            System.out.println("Erro ao carregar arqueira: " + e.getMessage());
            framesTorreArqueira = null;
        }
        try {
            framesTorrePoison   = carregarSheet3Niveis("assets/torres/poison/torrePoison/Tower 02.png");
        } catch (Exception e) {
            System.out.println("Erro ao carregar poison: " + e.getMessage());
            framesTorrePoison = null;
        }
        try {
            BufferedImage sheetFrozen = ImageIO.read(new File("assets/torres/frozen/torreFrozen/Tower 06.png"));
            int cols = 3;
            if (sheetFrozen.getWidth() % cols == 0) {
                framesTorreFrozen = carregarSheet3Niveis("assets/torres/frozen/torreFrozen/Tower 06.png");
            } else {
                framesTorreFrozen = new Image[]{sheetFrozen};
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar frozen: " + e.getMessage());
            framesTorreFrozen = null;
        }

        try { disparadorArqueira = ImageIO.read(new File("assets/torres/arqueira/disparadorArqueira/Tower 01 - Level 01 - Weapon.png")); } catch (Exception ignored) {}
        try { disparadorPoison   = ImageIO.read(new File("assets/torres/poison/disparadorPoison/disparador(1).png")); } catch (Exception ignored) {}
        try { disparadorFrozen   = ImageIO.read(new File("assets/torres/frozen/disparadorFrozen/disparadorFrozen.png")); } catch (Exception ignored) {}

        try {
            BufferedImage sheetProj = ImageIO.read(
                new File("assets/torres/arqueira/projetilArqueira/Tower 01 - Level 01 - Projectile.png")
            );
            int colsProj = 3;
            int tileWProj = sheetProj.getWidth() / colsProj;
            int tileHProj = sheetProj.getHeight();
            imgProjetilArqueira = sheetProj.getSubimage(0, 0, tileWProj, tileHProj);
        } catch (Exception e) {
            imgProjetilArqueira = null;
        }

        try { imgProjetilPoison = ImageIO.read(new File("assets/torres/poison/projetilPoison/Tower 02 - Level 03 - Projectile.png")); } catch (Exception e) { imgProjetilPoison = null; }
        try { imgProjetilFrozen = ImageIO.read(new File("assets/torres/frozen/projetilFrozen/Tower 05 - Level 01 - Projectile.png")); } catch (Exception e) { imgProjetilFrozen = null; }
        try { imgImpactoArqueira = ImageIO.read(new File("assets/torres/arqueira/impactoArqueira/Tower 01 - Weapon - Impact.png")); } catch (Exception e) { imgImpactoArqueira = null; }
        try { imgImpactoPoison = ImageIO.read(new File("assets/torres/poison/impactoPoison/Tower 02 - Level 03 - Projectile - Impact.png")); } catch (Exception e) { imgImpactoPoison = null; }
        try { imgImpactoFrozen = ImageIO.read(new File("assets/torres/frozen/impactoFrozen/Tower 05 - Level 01 - Projectile - Impact.png")); } catch (Exception e) { imgImpactoFrozen = null; }

        System.out.println("Sprites de torres carregados.");
    }

    private void carregarTilesMapa() {
        try {
            BufferedImage imgConstruivel = ImageIO.read(new File("assets/background/construivel.png"));
            BufferedImage imgCaminho = ImageIO.read(new File("assets/background/caminho.png"));
            tileTerreno = imgConstruivel.getSubimage(0, 0, imgConstruivel.getWidth(), imgConstruivel.getHeight());
            tileCaminho = imgCaminho.getSubimage(0, 0, imgCaminho.getWidth(), imgCaminho.getHeight());
        } catch (Exception e) {
            System.out.println("Erro ao carregar tiles do mapa: " + e.getMessage());
            tileTerreno = null;
            tileCaminho = null;
        }
    }

    private void carregarDecoracoesMapa() {
        decoracoes = new ArrayList<>();
        decoracoes.clear();
    }

    private boolean posicaoLivreParaDecoracao(Mapa mapa, int linha, int coluna) {
        Posicao pos = new Posicao(linha, coluna);
        if (mapa.isAreaBase(pos)) return false;
        if (!mapa.ehConstruivel(pos) && mapa.getCaminho().contains(pos)) return false;
        if (mapa.ehConstruivel(pos)) return false;
        return true;
    }

    private void tentarDecoracao(Mapa mapa, int linha, int coluna, Image sprite) {
        if (posicaoLivreParaDecoracao(mapa, linha, coluna)) {
            decoracoes.add(new DecoracaoMapa(linha, coluna, sprite, 0, 0));
        }
    }

    private void desenharDecoracoes(Graphics g) {
        // sem big trees
    }

    private void desenharCasteloBase(Graphics g, Posicao base) {
        if (imgBaseCastelo == null || base == null) return;
        int topLeftCol = Math.max(0, base.getColuna() - 3);
        int topLeftRow = Math.max(0, base.getLinha() - 5);
        int w = cellSize * 5;
        int h = cellSize * 5;
        int x = topLeftCol * cellSize;
        int y = topLeftRow * cellSize;
        g.drawImage(imgBaseCastelo, x, y, w, h, this);
    }

    private void desenharProjeteis(Graphics g, List<Projetil> projeteis) {
        if (projeteis == null) return;

        Graphics2D g2 = (Graphics2D) g;

        for (Projetil p : projeteis) {
            double cx = p.getX() * cellSize;
            double cy = p.getY() * cellSize;

            Image spriteProj = switch (p.getOrigem()) {
                case "poison" -> imgProjetilPoison != null ? imgProjetilPoison : imgProjetilArqueira;
                case "frozen" -> imgProjetilFrozen != null ? imgProjetilFrozen : imgProjetilArqueira;
                default -> imgProjetilArqueira;
            };

            if (spriteProj != null) {
                int w = (int) (cellSize * 0.24);
                int h = (int) (cellSize * 0.60);

                int drawX = (int) Math.round(cx - w / 2.0);
                int drawY = (int) Math.round(cy - h / 2.0);

                double angulo = 0;
                Inimigos alvo = p.getAlvo();
                if (alvo != null && alvo.getPosicaoAtual() != null) {
                    double alvoCx = (alvo.getPosicaoAtual().getColuna() + 0.5) * cellSize;
                    double alvoCy = (alvo.getPosicaoAtual().getLinha() + 0.5) * cellSize;
                    angulo = Math.atan2(alvoCy - cy, alvoCx - cx);
                }

                AffineTransform old = g2.getTransform();
                g2.rotate(angulo + Math.PI / 2, cx, cy);
                g2.drawImage(spriteProj, drawX, drawY, w, h, this);
                g2.setTransform(old);
            } else {
                g.setColor(Color.WHITE);
                int r = (int) (cellSize * 0.12);
                int x = (int) Math.round(cx - r / 2.0);
                int y = (int) Math.round(cy - r / 2.0);
                g.fillOval(x, y, r, r);
            }
        }
    }

    private void desenharImpactos(Graphics g, List<ImpactoVisual> impactos) {
        if (impactos == null) return;
        for (ImpactoVisual imp : impactos) {
            Posicao pos = imp.getPosicao();
            if (pos == null) continue;
            int x = pos.getColuna() * cellSize;
            int y = pos.getLinha() * cellSize;

            Image img = switch (imp.getOrigem()) {
                case "poison" -> imgImpactoPoison;
                case "frozen" -> imgImpactoFrozen;
                default -> imgImpactoArqueira;
            };

            if (img != null) {
                int w = (int) (cellSize * 0.8);
                int h = (int) (cellSize * 0.8);
                int drawX = x + (cellSize - w) / 2;
                int drawY = y + (cellSize - h) / 2;
                g.drawImage(img, drawX, drawY, w, h, this);
            }
        }
    }

    private void desenharPreviewAlcance(Graphics g) {
        if (!previewAtiva || previewPosicao == null) return;

        Graphics2D g2 = (Graphics2D) g;
        int linhas = jogo.getMapa().getLinhas();
        int colunas = jogo.getMapa().getColunas();

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                double distancia = Math.hypot(j - previewPosicao.getColuna(), i - previewPosicao.getLinha());
                if (distancia <= previewAlcance) {
                    int x = j * cellSize;
                    int y = i * cellSize;
                    g2.setColor(new Color(255, 255, 255, 70));
                    g2.fillRect(x, y, cellSize, cellSize);
                }
            }
        }

        int selX = previewPosicao.getColuna() * cellSize;
        int selY = previewPosicao.getLinha() * cellSize;
        g2.setColor(new Color(255, 255, 255, 140));
        g2.fillRect(selX, selY, cellSize, cellSize);
        g2.setColor(new Color(0, 0, 0, 200));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(selX, selY, cellSize, cellSize);
    }

    private void tentarUpgrade(Posicao pos) {
        Torre torre = torreNaPosicao(pos);
        if (torre == null) {
            JOptionPane.showMessageDialog(
                this,
                "Clique em uma torre existente para upar.",
                "Nenhuma torre aqui",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        try {
            boolean ok = torre.melhorar(jogo.getBanco());
            if (ok) {
                JOptionPane.showMessageDialog(
                    this,
                    "Torre upada para o nivel " + torre.getNivel() + ".",
                    "Upgrade concluido",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (NivelMaximoException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Nivel maximo",
                JOptionPane.WARNING_MESSAGE
            );
        } catch (SaldoInsuficienteException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Saldo insuficiente",
                JOptionPane.ERROR_MESSAGE
            );
        } finally {
            GameWindow.modoUpgrade = false;
            repaint();
        }
    }

    private void desenharTorres(Graphics g, List<Torre> torres) {
        if (torres == null) return;

        Graphics2D g2 = (Graphics2D) g;

        for (Torre t : torres) {
            Posicao p = t.getPosicao();
            int x = p.getColuna() * cellSize;
            int y = p.getLinha() * cellSize;

            Image spriteCorpo = null;
            int idxNivel = Math.max(0, Math.min(t.getNivel() - 1, 2));
            if (t instanceof TorreArqueira && framesTorreArqueira != null) {
                spriteCorpo = framesTorreArqueira[idxNivel];
            } else if (t instanceof TorrePoison && framesTorrePoison != null) {
                spriteCorpo = framesTorrePoison[idxNivel];
            } else if (t instanceof TorreFrozen && framesTorreFrozen != null) {
                spriteCorpo = framesTorreFrozen[idxNivel];
            }

            int corpoW = (int) (cellSize * 1.10);
            int corpoH = (int) (cellSize * 1.85);
            if (t instanceof TorrePoison) {
                corpoH = (int) (cellSize * 2.05); // corpo um pouco mais alto
            }
            int corpoX = x + (cellSize - corpoW) / 2;
            int corpoY = y + (cellSize - corpoH);

            if (spriteCorpo != null) {
                g.drawImage(spriteCorpo, corpoX, corpoY, corpoW, corpoH, this);
            } else {
                g.setColor(Color.ORANGE);
                g.fillOval(corpoX + 5, corpoY + 5, corpoW - 10, corpoH - 10);
            }

            if (t instanceof TorreArqueira arq) {
                Image frame = arq.getFrameDisparador();
                if (frame == null) frame = disparadorArqueira;
                int wTop = (int) (cellSize * 1.30);
                int hTop = (int) (cellSize * 1.70);
                int topX = x + (cellSize - wTop) / 2;
                int topY = corpoY + (cellSize / 8);
                double cx = topX + wTop / 2.0;
                double cy = topY + hTop / 2.0;
                AffineTransform old = g2.getTransform();
                g2.rotate(arq.getAnguloAtualRad() + Math.PI / 2, cx, cy);
                g2.drawImage(frame, topX, topY, wTop, hTop, this);
                g2.setTransform(old);
            } else if (t instanceof TorrePoison poison) {
                Image frame = poison.getFrameDisparador();
                if (frame == null) frame = disparadorPoison;
                int wTop = (int) (cellSize * 0.95);
                int hTop = (int) (cellSize * 1.30);
                int topX = x + (cellSize - wTop) / 2;
                int topY = corpoY + (cellSize / 8);
                g.drawImage(frame, topX, topY, wTop, hTop, this);
            } else if (t instanceof TorreFrozen && disparadorFrozen != null) {
                int wTop = (int) (cellSize * 1.45);
                int hTop = (int) (cellSize * 1.95);
                int topX = x + (cellSize - wTop) / 2;
                int topY = corpoY + (cellSize / 8);
                g.drawImage(disparadorFrozen, topX, topY, wTop, hTop, this);
            }
        }
    }

    private void desenharInimigos(Graphics g, List<Inimigos> inimigos) {
        if (inimigos == null) return;

        for (Inimigos i : inimigos) {
            Posicao p = i.getPosicaoAtual();
            if (p == null) continue;

            int x = p.getColuna() * cellSize;
            int y = p.getLinha() * cellSize;

            Image sprite = i.getSpriteAtual();

            if (sprite != null) {
                g.drawImage(sprite, x, y, cellSize, cellSize, this);
            } else {
                g.setColor(Color.RED);
                g.fillRect(x + 8, y + 8, cellSize - 16, cellSize - 16);
            }

            desenharBarraVida(g, i, x, y);
        }
    }

    private void desenharBarraVida(Graphics g, Inimigos inimigo, int x, int yBaseSprite) {
        int vidaAtual = inimigo.getVidaAtual();
        int vidaMax   = inimigo.getVidaMaxima();

        if (vidaMax <= 0) return;

        int barWidth  = cellSize;
        int barHeight = 4;
        int barX = x;
        int barY = yBaseSprite - barHeight - 2;

        g.setColor(Color.RED);
        g.fillRect(barX, barY, barWidth, barHeight);

        double proporcao = (double) vidaAtual / vidaMax;
        int larguraVerde = (int) (barWidth * proporcao);

        g.setColor(Color.GREEN);
        g.fillRect(barX, barY, larguraVerde, barHeight);

        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);
    }

    private Torre torreNaPosicao(Posicao pos) {
        for (Torre t : jogo.listaAtivaTorres()) {
            if (t.getPosicao().equals(pos)) {
                return t;
            }
        }
        return null;
    }

    private static class DecoracaoMapa {
        final int linha;
        final int coluna;
        final Image sprite;
        final int offsetX;
        final int offsetY;

        DecoracaoMapa(int linha, int coluna, Image sprite, int offsetX, int offsetY) {
           this.linha = linha;
           this.coluna = coluna;
           this.sprite = sprite;
           this.offsetX = offsetX;
           this.offsetY = offsetY;
        }
    }

    public interface PlacementListener {
        void onPreviewChanged(boolean ativo);
    }
}
