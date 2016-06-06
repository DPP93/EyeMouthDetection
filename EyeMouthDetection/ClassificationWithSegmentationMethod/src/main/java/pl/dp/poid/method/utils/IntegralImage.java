package pl.dp.poid.method.utils;

import java.awt.image.BufferedImage;

/**
 * Created by dpp on 6/3/16.
 */
public class IntegralImage {

    private int[][] summedAreaTable;

    public IntegralImage(BufferedImage grayscaleImage){
        setupSummedAreaTable(grayscaleImage);
    }

    private void setupSummedAreaTable(BufferedImage image){
        summedAreaTable = new int[image.getWidth()][image.getHeight()];
        int[] pixelValue = new int[image.getColorModel().getPixelSize()/8];
        int value;
        for (int x = 0; x < image.getWidth(); x++){
            for (int y = 0; y < image.getHeight(); y++){
                value = image.getRaster().getPixel(x,y,pixelValue)[0];
                if (x == 0 && y == 0){
                    summedAreaTable[x][y] = value;
                    continue;
                }

                if (x > 0 && y == 0){
                    value += summedAreaTable[x - 1][y];
                    summedAreaTable[x][y] = value;
                    continue;
                }

                if (x == 0 && y > 0){
                    value += summedAreaTable[x][y - 1];
                    summedAreaTable[x][y] = value;
                    continue;
                }

                value += summedAreaTable[x - 1][y];
                value += summedAreaTable[x][y - 1];
                value -= summedAreaTable[x - 1][y - 1];
                summedAreaTable[x][y] = value;
            }
        }

    }

    public int[][] getSummedAreaTable() {
        return summedAreaTable;
    }

    /**
     *
     * @param a left top corner
     * @param b right top corner
     * @param c left bottom corner
     * @param d right bottom corner
     * @return
     */
    public int getSum(Point a, Point b, Point c, Point d){
        int sum = 0;
        sum += summedAreaTable[(int) a.getX()][(int) a.getY()];
        sum += summedAreaTable[(int) d.getX()][(int) d.getY()];
        sum -= summedAreaTable[(int) b.getX()][(int) b.getY()];
        sum -= summedAreaTable[(int) c.getX()][(int) c.getY()];
        return sum;
    }
}
