package image.processing;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2016-05-29.
 */
public class ImageProcessing {
    private Logger logger = LogManager.getLogger(ImageProcessing.class.getName());
    protected List<BufferedImage> originalImages;
    protected List<BufferedImage> workingImages;
    protected String indentifier = "ImagesTests/TESTS-IMAGE PROCESSING";
    private String[] imagesNames = {"mike.jpg", "gary.jpg", "johny.jpg"};

    public ImageProcessing() {
        originalImages = new ArrayList<>();
        workingImages = new ArrayList<>();
        readImages();
    }

    public void saveImages(){
        for (BufferedImage b : workingImages ) {
            try {
                new File(indentifier).mkdir();
                ImageIO.write(b, "jpg", new File(indentifier+"\\"+b.hashCode()+".jpg"));
            } catch (IOException e) {
                logger.debug(logger.getName() + " Cannot write "+b.hashCode()+" getting continue");
                continue;
            }
        }
    }

    private void readImages(){
        for (String image : imagesNames) {
            BufferedImage b;
            try {
                b = ImageIO.read(new File(image));
            } catch (IOException e) {
                logger.debug(logger.getName() + " Cannot read "+image+" getting continue");
                continue;
            }
            originalImages.add(b);
            workingImages.add(copyImage(b));
        }
    }

    private static BufferedImage copyImage(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
