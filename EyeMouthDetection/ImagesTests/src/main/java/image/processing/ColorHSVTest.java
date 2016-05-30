package image.processing;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 2016-05-30.
 */
public class ColorHSVTest extends ImageProcessing{

    public ColorHSVTest(){
        super();
        indentifier = "ImagesTests/TESTS-HSV";
    }

    public void checkHSVValues(){
        BufferedImage b = workingImages.get(0);
        int[] d = new int[3];
        float[] h = new float[3];
        System.out.println();
        //Mike
        d = b.getRaster().getPixel(60,46, d);
        h = Color.RGBtoHSB(d[0], d[1], d[2], h);
        System.out.printf("Mike left eye hue: %f saturation: %f brightness %f\n", h[0], h[1], h[2]);
        d = b.getRaster().getPixel(104,46, d);
        h = Color.RGBtoHSB(d[0], d[1], d[2], h);
        System.out.printf("Mike right eye hue: %f saturation: %f brightness %f\n", h[0], h[1], h[2]);
        d = b.getRaster().getPixel(64,95, d);
        h = Color.RGBtoHSB(d[0], d[1], d[2], h);
        System.out.printf("Mike left mouth hue: %f saturation: %f brightness %f\n", h[0], h[1], h[2]);
        d = b.getRaster().getPixel(96,94, d);
        h = Color.RGBtoHSB(d[0], d[1], d[2], h);
        System.out.printf("Mike right mouth hue: %f saturation: %f brightness %f\n", h[0], h[1], h[2]);
        System.out.println();
        //Gary
        b = workingImages.get(1);
        d = b.getRaster().getPixel(102,117, d);
        h = Color.RGBtoHSB(d[0], d[1], d[2], h);
        System.out.printf("Gary left eye hue: %f saturation: %f brightness %f\n", h[0], h[1], h[2]);
        d = b.getRaster().getPixel(146,115, d);
        h = Color.RGBtoHSB(d[0], d[1], d[2], h);
        System.out.printf("Gary right eye hue: %f saturation: %f brightness %f\n", h[0], h[1], h[2]);
        d = b.getRaster().getPixel(107,159, d);
        h = Color.RGBtoHSB(d[0], d[1], d[2], h);
        System.out.printf("Gary left mouth hue: %f saturation: %f brightness %f\n", h[0], h[1], h[2]);
        d = b.getRaster().getPixel(143,159, d);
        h = Color.RGBtoHSB(d[0], d[1], d[2], h);
        System.out.printf("Gary right mouth hue: %f saturation: %f brightness %f\n", h[0], h[1], h[2]);

        //Johny
        System.out.println();
        b = workingImages.get(2);
        d = b.getRaster().getPixel(143,159, d);
        h = Color.RGBtoHSB(d[0], d[1], d[2], h);
        System.out.printf("Johny left eye hue: %f saturation: %f brightness %f\n", h[0], h[1], h[2]);
        d = b.getRaster().getPixel(235,154, d);
        h = Color.RGBtoHSB(d[0], d[1], d[2], h);
        System.out.printf("Johny right eye hue: %f saturation: %f brightness %f\n", h[0], h[1], h[2]);
        d = b.getRaster().getPixel(151,248, d);
        h = Color.RGBtoHSB(d[0], d[1], d[2], h);
        System.out.printf("Johny left mouth hue: %f saturation: %f brightness %f\n", h[0], h[1], h[2]);
        d = b.getRaster().getPixel(232,245, d);
        h = Color.RGBtoHSB(d[0], d[1], d[2], h);
        System.out.printf("Johny right mouth hue: %f saturation: %f brightness %f\n", h[0], h[1], h[2]);
    }

    public void eyeMather(){
        int[] d = new int[3];
        float[] h = new float[3];
        for (int i = 0; i < workingImages.size(); i++){
            BufferedImage b = workingImages.get(i);
            for (int x = 0; x < b.getWidth(); x++){
                for(int y = 0; y < b.getHeight(); y++){
                    d = b.getRaster().getPixel(x,y, d);
                    h = Color.RGBtoHSB(d[0], d[1], d[2], h);
                    if (h[2] < 0.4) {
                        int[] tab = {255,255,255};
                        b.getRaster().setPixel(x,y, tab);
                    }
                }
            }
        }
    }

}
