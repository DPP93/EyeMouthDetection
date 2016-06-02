package method.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by dpp on 6/2/16.
 */
public class ColorModelModifier {

    public static BufferedImage convertImageToGrayscale(BufferedImage bufferedImage){

        if (bufferedImage.getColorModel().getPixelSize() == 8){
            return bufferedImage;
        }

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                Color c = new Color(bufferedImage.getRGB(x, y));
                int red = (int)(c.getRed() * 0.299);
                int green = (int)(c.getGreen() * 0.587);
                int blue = (int)(c.getBlue() *0.114);
                Color newColor = new Color(red+green+blue, red+green+blue, red+green+blue);
                bufferedImage.setRGB(x,y,newColor.getRGB());
            }
        }
        return bufferedImage;
    }

}
