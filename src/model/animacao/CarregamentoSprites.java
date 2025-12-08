package model.animacao;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class CarregamentoSprites {


    public static Image[] carregarStripQuadrado(String caminhoRelativo) {
        try {
            BufferedImage sheet = ImageIO.read(new File(caminhoRelativo));

            int frameSize = sheet.getHeight();          // assume frames quadrados
            int qtdFrames = sheet.getWidth() / frameSize;

            Image[] frames = new Image[qtdFrames];

            for (int i = 0; i < qtdFrames; i++) {
                frames[i] = sheet.getSubimage(
                    i * frameSize, // x
                    0,             // y
                    frameSize,     // largura
                    frameSize      // altura
                );
            }

            System.out.println("Carregado strip " + caminhoRelativo +
                               " com " + qtdFrames + " frames.");
            return frames;

        } catch (Exception e) {
            System.out.println("Erro ao carregar strip: " + caminhoRelativo);
            e.printStackTrace();
            return new Image[0];
        }
    }

public static Image[] carregarStripHorizontal(String caminhoRelativo, int qtdFrames) {
    try {
        BufferedImage sheet = ImageIO.read(new File(caminhoRelativo));

        int frameWidth  = sheet.getWidth() / qtdFrames;
        int frameHeight = sheet.getHeight();

        Image[] frames = new Image[qtdFrames];

        for (int i = 0; i < qtdFrames; i++) {
            frames[i] = sheet.getSubimage(
                i * frameWidth, // x
                0,              // y
                frameWidth,
                frameHeight
            );
        }

        System.out.println("Carregado strip horizontal " + caminhoRelativo +
                           " com " + qtdFrames + " frames.");
        return frames;

    } catch (Exception e) {
        System.out.println("Erro ao carregar strip horizontal: " + caminhoRelativo);
        e.printStackTrace();
        return new Image[0];
    }
}


}
