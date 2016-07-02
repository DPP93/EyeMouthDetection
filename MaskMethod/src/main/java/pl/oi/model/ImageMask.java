package pl.oi.model;

import pl.oi.annotations.FacePoint;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by dpp on 5/5/16.
 */
public class ImageMask {

    private double[][] redChannel;
    private double[][] blueChannel;
    private double[][] greenChannel;

    private int counter=0;

    private final int maskSize;

    public ImageMask(int maskSize){
        redChannel = new double[maskSize][maskSize];
        blueChannel= new double[maskSize][maskSize];
        greenChannel = new double[maskSize][maskSize];

        this.maskSize = maskSize;
    }

    /**
     * Metoda wykonuje uczenie maski dla jednego zdjęcia
     * @param image maska
     * @param facePoint punkt uczący wskazujący na nasz element
     */
    public void learn(BufferedImage image, FacePoint facePoint){
        counter++;

        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){

                if(distance(x,y,facePoint.getX(),facePoint.getY()) <= maskSize/2){
                    int[] tab = new int[image.getColorModel().getPixelSize()/8];

                    image.getRaster().getPixel(x,y,tab);

                    if(tab.length == 3){
                        redChannel[x%maskSize][y%maskSize] += tab[0];
                        greenChannel[x%maskSize][y%maskSize] += tab[1];
                        blueChannel[x%maskSize][y%maskSize] += tab[2];
                    }else {
                        redChannel[x%maskSize][y%maskSize] += tab[0];
                        greenChannel[x%maskSize][y%maskSize] += tab[0];
                        blueChannel[x%maskSize][y%maskSize] += tab[0];
                    }
                }
            }
        }
    }

    /**
     * Wywołać na sam koniec uczenia
     */
    public void stopLearning(){
        for(int x = 0; x < redChannel.length; x++){
            for(int y = 0; y < redChannel[x].length; y++){
                redChannel[x][y] /= counter;
                greenChannel[x][y] /= counter;
                blueChannel[x][y] /= counter;
            }
        }
    }

    public FacePoint testMask(BufferedImage image){

        FacePoint facePoint;

        //Punkty minX, minY wyznaczają moment w którym maska miała najbardziej podobną wartość, oznaczają lewy górny bok maski
        int minX = 0;
        int minY = 0;
        double minValue = Double.MAX_VALUE;

        for(int x = 0; x < image.getWidth() - (maskSize/2); x++){
            for(int y = 0; y < image.getHeight() - (maskSize/2); y++){

                double val = computeSimilarityOfMask(image, x, y);
                if(val < minValue){
                    minValue = val;
                    minX = x;
                    minY = y;
                }
            }
        }
//        System.out.println();
        facePoint = new FacePoint(minX + (maskSize/2)-1, minY + (maskSize/2)-1);
        return facePoint;
    }

    private double computeSimilarityOfMask(BufferedImage image, int startX, int startY){
        double result = 0;
        double red = 0, green = 0, blue = 0;
        for(int x = 0; x < redChannel.length; x++){
            for(int y = 0; y < redChannel[x].length; y++){

                if( startX+x >= image.getWidth() || startY+y>=image.getHeight()){
                    break;
                }

                if(redChannel[x][y] == 0){
                    continue;
                }

                int[] tab = new int[image.getColorModel().getPixelSize()/8];
                image.getRaster().getPixel(startX+x,startY+y,tab);

                if(tab.length == 3){
                    red += Math.pow(redChannel[x][y] - tab[0], 2.0);
                    green += Math.pow(greenChannel[x][y] - tab[1], 2.0);
                    blue += Math.pow(blueChannel[x][y] - tab[2], 2.0);
                }else {
                    red += Math.pow(redChannel[x][y] - tab[0], 2.0);
                    green += Math.pow(greenChannel[x][y] - tab[0], 2.0);
                    blue += Math.pow(blueChannel[x][y] - tab[0], 2.0);
                }
            }
            red /= Math.pow(maskSize, 2.0);
            green /= Math.pow(maskSize, 2.0);
            blue /= Math.pow(maskSize, 2.0);
        }
        result = red + green + blue;
        return result;
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

}
