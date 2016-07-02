package pl.oi.model;

import pl.oi.annotations.FacePoint;

import java.awt.image.BufferedImage;

/**
 * Created by dpp on 5/10/16.
 */
public class AverageMethod {

    private double redChannel = 0;
    private double greenChannel = 0;
    private double blueChannel = 0;


    private double radius = 1;

    private double counter = 0;

    public void learn(BufferedImage image, FacePoint facePoint) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (distance(x, y, facePoint.getX(), facePoint.getY()) <= radius) {
                    counter++;
                    int[] tab = new int[image.getColorModel().getPixelSize() / 8];

                    image.getRaster().getPixel(x, y, tab);

                    if (tab.length == 3) {
                        redChannel += tab[0];
                        greenChannel += tab[1];
                        blueChannel += tab[2];
                    } else {
                        redChannel += tab[0];
                        greenChannel += tab[0];
                        blueChannel += tab[0];
                    }
                }
            }
        }
    }

    public void stopLearning() {
        redChannel /= counter;
        greenChannel /= counter;
        blueChannel /= counter;
//        System.out.println("RED "+redChannel);
//        System.out.println("GREEN "+greenChannel);
//        System.out.println("BLUE "+blueChannel);
//        System.out.println("------------------");
    }


    public FacePoint testMask(BufferedImage image){

        FacePoint facePoint;

        int minX = 0;
        int minY = 0;
        double minValue = Double.MAX_VALUE;

        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                int[] tab = new int[image.getColorModel().getPixelSize() / 8];

                image.getRaster().getPixel(x, y, tab);
                double sum = 0;
                if (tab.length == 3) {
                    sum += tab[0];
                    sum += tab[1];
                    sum += tab[2];
                } else {
                    sum += 3*tab[0];
                }
                double value = sum-(redChannel+greenChannel+blueChannel);
                if(Math.abs(value) < minValue){
                    minX = x;
                    minY = y;
                    minValue = value;
                }
            }
        }
        facePoint = new FacePoint(minX, minY);
        return facePoint;
    }


    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

}
