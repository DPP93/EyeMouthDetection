package method.main;

import method.utils.ColorModelModifier;
import method.utils.IntegralImage;
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
        IntegralImage ii = new IntegralImage(b);

        int diff1 = 10;
        int diff2 = 5;

        int x = 146;
        int y = 110;

        Point p1a = new Point(x-diff1,y-diff1);
        Point p1b = new Point(x+diff1,y-diff1);
        Point p1c = new Point(x-diff1,y+diff1);
        Point p1d = new Point(x+diff1,y+diff1);

        Point p2a = new Point(x-diff2,y-diff2);
        Point p2b = new Point(x+diff2,y-diff2);
        Point p2c = new Point(x-diff2,y+diff2);
        Point p2d = new Point(x+diff2,y+diff2);


        int sum1 = ii.getSum(p1a, p1b, p1c, p1d);
        int sum2 = ii.getSum(p2a, p2b, p2c, p2d);
        System.out.println("sum1 = " + sum1);
        System.out.println("sum2 = " + sum2);
        System.out.println(sum1 - sum2);

        ImageIO.write(b, "jpg", new File("garyTest.jpg"));
    }
}
