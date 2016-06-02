package segmentation;

import method.utils.Point;

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
    public static BufferedImage segmentateImage(BufferedImage image, double edge, Point startingPoint){



        List<Point> segmentatedPoints = new ArrayList<>();
        List<Point> visitedPoints = new ArrayList<>();

        Queue<Point> pointsToCheck = new LinkedList<>();

        segmentatedPoints.add(startingPoint);
        visitedPoints.add(startingPoint);
        double segmentatedPixelCounter = 1.0 ;
        double [] pixelTab = new double[image.getColorModel().getPixelSize()/8];
        pixelTab = image.getRaster().getPixel(startingPoint.getX(), startingPoint.getY(), pixelTab);
        double sum = pixelTab[0];
        double average = 0;
        pointsToCheck.addAll(getNeighbourPoints(image.getWidth(), image.getHeight(), startingPoint));
        while (segmentatedPixelCounter < 100) {

            if (visitedPoints.contains(pointsToCheck.element())){
                pointsToCheck.poll();
            }
            Point p = pointsToCheck.element();
            visitedPoints.add(pointsToCheck.element());
            pixelTab = new double[image.getColorModel().getPixelSize()/8];
            pixelTab = image.getRaster().getPixel(p.getX(), p.getY(), pixelTab);
            average = sum / segmentatedPixelCounter;

            if (pixelTab[0] <= average + edge && pixelTab[0] >= average - edge){
                System.out.println(pixelTab[0] + " " + average);
                segmentatedPoints.add(pointsToCheck.element());
                segmentatedPixelCounter++;
                sum += pixelTab[0];
                pointsToCheck.addAll(getNeighbourPoints(image.getWidth(), image.getHeight(), pointsToCheck.remove()));

            }
        }

        pixelTab = new double[image.getColorModel().getPixelSize()/8];
        for (int i = 0; i < pixelTab.length; i++){
            pixelTab[i] = 255;
        }
        for (Point p : segmentatedPoints){
            image.getRaster().setPixel(p.getX(), p.getY(), pixelTab);
        }

        visitedPoints.clear();
        segmentatedPoints.clear();

        return image;
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
