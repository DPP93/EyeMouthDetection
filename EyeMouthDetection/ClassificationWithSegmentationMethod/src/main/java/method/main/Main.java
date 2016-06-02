package method.main;

import method.utils.ColorModelModifier;
import method.utils.Point;
import segmentation.SegmentationRegionGrowth;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Daniel on 2016-05-28.
 */
public class Main {
    public static void main(String args[]) throws IOException {
        System.out.println("Hello");
        System.out.println(Main.class.getPackage());

        BufferedImage b = ImageIO.read(new File("gary.jpg"));
//        b = ColorModelModifier.convertImageToGrayscale(b);
        ImageIO.write(b, "jpg", new File("garyWhite.jpg"));
//        b = SegmentationRegionGrowth.segmentateImageByGrayscale(b, 17, new Point(102, 117));
        b = SegmentationRegionGrowth.segmentateImageByGrayscale(b, 15, new Point(102, 117));

        ImageIO.write(b, "jpg", new File("garyTest.jpg"));
    }
}
