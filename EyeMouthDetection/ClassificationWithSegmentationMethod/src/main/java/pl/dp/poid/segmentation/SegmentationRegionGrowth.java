package pl.dp.poid.segmentation;

import pl.dp.poid.method.utils.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by dpp on 6/2/16.
 */
public class SegmentationRegionGrowth {

    /**
     *
     * @param image image in grayscale
     * @param edge edge for define if pixel will be segmentated
     * @return
     */
    public static List<Point> segmentateImageByGrayscale(BufferedImage image, double edge, Point startingPoint){

        List<Point> segmentatedPoints = new ArrayList<>();
        List<Point> visitedPoints = new ArrayList<>();

        Queue<Point> pointsToCheck = new LinkedList<>();

        segmentatedPoints.add(startingPoint);
        visitedPoints.add(startingPoint);
        double segmentatedPixelCounter = 1.0 ;
        double [] pixelTab = new double[image.getColorModel().getPixelSize()/8];
        pixelTab = image.getRaster().getPixel((int)startingPoint.getX(), (int)startingPoint.getY(), pixelTab);
        double sum = pixelTab[0];
        double average = 0;
        pointsToCheck.addAll(getNeighbourPoints(image.getWidth(), image.getHeight(), startingPoint));
        int counter = 0;
        while (!pointsToCheck.isEmpty() && counter < 100) {
            counter++;
            if (visitedPoints.contains(pointsToCheck.element())){
                pointsToCheck.remove();
                continue;
            }

            Point p = pointsToCheck.element();
            visitedPoints.add(pointsToCheck.element());
            pixelTab = new double[image.getColorModel().getPixelSize()/8];
            pixelTab = image.getRaster().getPixel((int)p.getX(), (int)p.getY(), pixelTab);
            average = sum / segmentatedPixelCounter;

            if (pixelTab[0] <= average + edge){
//                System.out.println(pixelTab[0] + " " + average);
                segmentatedPoints.add(pointsToCheck.element());
                segmentatedPixelCounter++;
                sum += pixelTab[0];
                pointsToCheck.addAll(getNeighbourPoints(image.getWidth(), image.getHeight(), pointsToCheck.remove()));

            }
        }

//        pixelTab = new double[image.getColorModel().getPixelSize()/8];
//        for (int i = 0; i < pixelTab.length; i++){
//            pixelTab[i] = 255;
//        }
//        for (Point p : segmentatedPoints){
//            image.getRaster().setPixel((int)p.getX(), (int)p.getY(), pixelTab);
//        }
//
//        visitedPoints.clear();
//        segmentatedPoints.clear();

        return segmentatedPoints;
    }

    public static List<Point> segmentateImageByHSV(BufferedImage image, double edge, Point startingPoint){

        List<Point> segmentatedPoints = new ArrayList<>();
        List<Point> visitedPoints = new ArrayList<>();

        Queue<Point> pointsToCheck = new LinkedList<>();

        segmentatedPoints.add(startingPoint);
        visitedPoints.add(startingPoint);
        double segmentatedPixelCounter = 1.0 ;
        int [] pixelTab = new int[image.getColorModel().getPixelSize()/8];
        pixelTab = image.getRaster().getPixel((int)startingPoint.getX(), (int)startingPoint.getY(), pixelTab);
        float[] hsvTab= new float[pixelTab.length];
        hsvTab = Color.RGBtoHSB(pixelTab[0], pixelTab[1], pixelTab[2], hsvTab);
        double brightness = hsvTab[2];

        double average = 0;
        pointsToCheck.addAll(getNeighbourPoints(image.getWidth(), image.getHeight(), startingPoint));
        while (!pointsToCheck.isEmpty()) {

            if (visitedPoints.contains(pointsToCheck.element())){
                pointsToCheck.remove();
                continue;
            }

            Point p = pointsToCheck.element();
            visitedPoints.add(pointsToCheck.element());
            pixelTab = new int[image.getColorModel().getPixelSize()/8];
            pixelTab = image.getRaster().getPixel((int)p.getX(), (int)p.getY(), pixelTab);
            average = brightness / segmentatedPixelCounter;

            if (pixelTab[0] <= average + edge){
                segmentatedPoints.add(pointsToCheck.element());
                segmentatedPixelCounter++;
                hsvTab= new float[pixelTab.length];
                hsvTab = Color.RGBtoHSB(pixelTab[0], pixelTab[1], pixelTab[2], hsvTab);
                brightness += hsvTab[2];
                pointsToCheck.addAll(getNeighbourPoints(image.getWidth(), image.getHeight(), pointsToCheck.remove()));
            }
        }

        visitedPoints.clear();
        segmentatedPoints.clear();

        return segmentatedPoints;
    }

    private static List<Point> getNeighbourPoints(int imageWidth, int imageHeight, Point point){
        List<Point> list = new ArrayList<>();
        if(point.getX()+1 < imageWidth){
            list.add(new Point(point.getX()+1, point.getY()));
        }
        if(point.getX() - 1 >= 0){
            list.add(new Point(point.getX()-1, point.getY()));
        }
        if(point.getY() + 1 < imageHeight){
            list.add(new Point(point.getX(), point.getY() +1));
        }
        if (point.getY() - 1 >= 0){
            list.add(new Point(point.getX(), point.getY() - 1));
        }

        return list;
    }

}
