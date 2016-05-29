import image.processing.HorizontalEdges;
import image.processing.ImageProcessing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Daniel on 2016-05-29.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedImage bi = ImageIO.read(new File("mike.jpg"));
        System.out.println("Finished "+bi.getColorModel().getPixelSize());

        ImageProcessing ip = new ImageProcessing();
        ip.saveImages();

        HorizontalEdges he = new HorizontalEdges();
        he.computeHorizontalEdgeDetectionMasks();
//        he.checking();
        he.saveImages();
    }

}
