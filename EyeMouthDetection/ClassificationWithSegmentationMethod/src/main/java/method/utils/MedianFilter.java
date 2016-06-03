package method.utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2016-06-03.
 */
public class MedianFilter {

    public static BufferedImage computeMedianFilter(BufferedImage image){
        List<Integer> medianPoints = new ArrayList<>();
        int[][] newPixelValues = new int[image.getWidth()][image.getHeight()];
        int[] pixelValue = new int[image.getColorModel().getPixelSize()/8];
        for (int x = 1; x < image.getWidth() - 1; x++) {
            for (int y = 1; y < image.getHeight()-1; y++) {
                medianPoints.clear();
                for(int i = x - 1; i <= x+1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        medianPoints.add(image.getRaster().getPixel(i,j,pixelValue)[0]);
                    }
                }
                medianPoints.sort(Integer::compareTo);
                newPixelValues[x][y] = medianPoints.get(5);
            }
        }

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int i = 0; i < pixelValue.length; i++) {
                    pixelValue[i] = newPixelValues[x][y];
                }
                image.getRaster().setPixel(x,y,pixelValue);
            }
        }
        return image;
    }

}
