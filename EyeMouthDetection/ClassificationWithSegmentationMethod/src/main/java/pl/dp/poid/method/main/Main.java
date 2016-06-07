package pl.dp.poid.method.main;

import pl.dp.poid.method.eye.EyeFinder;
import pl.dp.poid.method.utils.ColorModelModifier;
import pl.dp.poid.method.utils.IntegralImage;
import pl.dp.poid.method.utils.MedianFilter;
import pl.dp.poid.method.utils.Point;

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

        BufferedImage b = ImageIO.read(new File("johny.jpg"));
        System.out.println("Konwersja na szaro");
        b = ColorModelModifier.convertImageToGrayscale(b);
//        double ax1 = 0.069, ay1 = 0.41;
//        double bx1 = 0.13,  by1 = 0.027;
//        double cx1 = 0.42, cy1 = 0.45;
//        double dx1 = 0.11, dy1 = 0.011;
//        
//        double ax2 = 0.059, ay2 = 0.45;
//        double bx2 = 0.63, by2 = 0.3;
//        double cx2 = 0.59, cy2 = 0.28;
//        double dx2 = 0.89, dy2 = 0.095;

        System.out.println("Mediana");
        MedianFilter.computeMedianFilter(b);
        ImageIO.write(b, "jpg", new File("garyWhite.jpg"));
//        b = SegmentationRegionGrowth.segmentateImageByGrayscale(b, 5, new Point(102, 117));
        IntegralImage ii = new IntegralImage(b);

        int ax1 = (int) (0.069*b.getWidth()), ay1 = (int) (0.41*b.getHeight());
        int bx1 = (int) (0.13*b.getWidth()),  by1 = (int) (0.027*b.getHeight());
        int cx1 = (int) (0.42*b.getWidth()), cy1 = (int) (0.45*b.getHeight());
        int dx1 = (int) (0.11*b.getWidth()), dy1 = (int) (0.011*b.getHeight());
        
        int ax2 = (int) (0.059*b.getWidth()), ay2 = (int) (0.45*b.getHeight());
        int bx2 = (int) (0.63*b.getWidth()), by2 = (int) (0.3*b.getHeight());
        int cx2 = (int) (0.59*b.getWidth()), cy2 = (int) (0.28*b.getHeight());
        int dx2 = (int) (0.89*b.getWidth()), dy2 = (int) (0.095*b.getHeight());
        int[] pixel = {255,0,0};
        b.getRaster().setPixel(ax1, ay1, pixel);
        b.getRaster().setPixel(bx1, by1, pixel);
        b.getRaster().setPixel(cx1, cy1, pixel);
        b.getRaster().setPixel(dx1, dy1, pixel);
        pixel = new int[]{0,255,0};
        b.getRaster().setPixel(ax2, ay2, pixel);
        b.getRaster().setPixel(bx2, by2, pixel);
        b.getRaster().setPixel(cx2, cy2, pixel);
        b.getRaster().setPixel(dx2, dy2, pixel);
        ImageIO.write(b, "jpg", new File("garyTest.jpg"));
        System.out.println("Zapisany");
        int diff1 = 24;
        int diff2 = 12;

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

        EyeFinder eyeFinder = new EyeFinder("MTFL");
        System.out.println("Uczenie");
        eyeFinder.learn();
        System.out.println("Test");
        eyeFinder.runTest();
        System.out.println("Koniec można bezpiecznie wyłączyć");
    }
}
