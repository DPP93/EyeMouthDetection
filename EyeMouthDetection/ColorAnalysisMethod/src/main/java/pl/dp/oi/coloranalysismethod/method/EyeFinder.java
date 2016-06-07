/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dp.oi.coloranalysismethod.method;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pl.dp.poid.annotations.Annotations;
import pl.dp.poid.annotations.Face;
import pl.dp.poid.imagedatabase.ImageDatabase;
import pl.dp.poid.imagedatabase.ImageFile;
import pl.dp.poid.method.utils.ColorModelModifier;
import pl.dp.poid.method.utils.ImageProcessing;
import pl.dp.poid.method.utils.MedianFilter;
import pl.dp.poid.method.utils.Point;

/**
 *
 * @author Daniel
 */
public class EyeFinder {
    
    private double averageEyeBrightness = 10;
    private ImageDatabase imageDatabase;
    private Annotations trainingAnnotations;

    private double averageLeftX = 0;
    private double averageLeftY = 0;
    private double averageRightX = 0;
    private double averageRightY = 0;
    
    private double distanceToClassifierInPixels = 15;

    public EyeFinder(String databaseDirectory) throws IOException {
        trainingAnnotations = new Annotations();
        trainingAnnotations.setupElements(new File(databaseDirectory + "/training.txt"));
        imageDatabase = new ImageDatabase(databaseDirectory);
    }



    public List<Point> findEyes(BufferedImage img) {
        List<Point> eyes = new ArrayList<>();
        BufferedImage image = ImageProcessing.copyImage(img);

        Point closestLeftAverageSegmentation = new Point(-1, -1);
        double closestLeftToAverage = Double.MAX_VALUE;
        Point closestRightToAverageSegmenatation = new Point(-1, -1);
        double closestRightToAverage = Double.MAX_VALUE;
        int[] pixelValue = new int[image.getColorModel().getPixelSize() / 8];
        List<Point> elementsSegmanetated;
        //Find left eye
        for (int x = 15; x < image.getWidth()-15; x++) {
            for(int y = 15; y < image.getHeight()-15; y++){
                if(checkPoint(image, new Point(x, y))){
                    closestLeftAverageSegmentation = new Point(x, y);
                }
            }
        }
        //Find right eye
        for (int x = 15; x < image.getWidth()-15; x++) {
            for(int y = 15; y < image.getHeight()-15; y++){
                if(checkPoint(image, new Point(x, y))){
                    if(Point.computeDistanceBetweenPoints(closestLeftAverageSegmentation, new Point(x, y)) > 10){
                        closestRightToAverageSegmenatation = new Point(x, y);
                    }  
                }
            }
        }
        
        if (closestLeftAverageSegmentation.getX() < closestRightToAverageSegmenatation.getX()) {
            eyes.add(closestLeftAverageSegmentation);
            eyes.add(closestRightToAverageSegmenatation);
        } else {
            eyes.add(closestRightToAverageSegmenatation);
            eyes.add(closestLeftAverageSegmentation);
        }
        System.out.println("--------");
        System.out.println("Left " + eyes.get(0).getX() + " " + eyes.get(0).getY() + " " + closestLeftToAverage);
        System.out.println("Right " + eyes.get(1).getX() + " " + eyes.get(1).getY() + " " + closestRightToAverage);

        return eyes;
    }
    
    /**
     * Checks if point is possible to be eye
     * @param image
     * @param point
     * @return 
     */
    private boolean checkPoint(BufferedImage image, Point point){
        
        float[] hsv = new float[3];
        int[] pixelValue = new int[3];
        
        float pupilEdgeBrightness = 0.4f;
        
        float skinSaturation = 0.5f;
        
        float skinLeftHue = 5f / 360f;
        float skinRightHue = 30f / 360f;
        
        boolean horizontal = false;
        boolean vertical = false;
        
        pixelValue = image.getRaster().getPixel((int)point.getX(), (int)point.getY(), pixelValue);
        
        hsv = Color.RGBtoHSB(pixelValue[0], pixelValue[1], pixelValue[2],hsv);
        boolean leftSkin = false;
        boolean pupil = false;
        boolean rightSkin = false;
        //Check horizont
        for(int i = (int)point.getX() - 10; i <= point.getX() + 10; i++){
            
            if( i == point.getX()+1 && leftSkin == false){
                return false;
            }
            
            pixelValue = image.getRaster().getPixel(i, (int)point.getY(), pixelValue);
            hsv = Color.RGBtoHSB(pixelValue[0], pixelValue[1], pixelValue[2],hsv);
            
            if(hsv[0] >= skinLeftHue && hsv[0]<=skinRightHue && hsv[2] < skinSaturation){
                if(leftSkin && pupil){
                    rightSkin = true;
                    break;
                }
                leftSkin = true;
                continue;
            }
            if(hsv[2] < pupilEdgeBrightness){
                pupil = true;
            }
        }
        
        if(rightSkin){
            return true;
        }
        
        return false;
    }
    
    public void runTest() throws IOException {
        File f = new File("results.txt");
        f.createNewFile();
        PrintWriter pw = new PrintWriter(f);
        Random random = new Random();
        List<ImageFile> testList = imageDatabase.getTestFiles();
        int counter = 1;
        for (ImageFile file : testList) {
            System.out.println(counter + " of " + testList.size() + " " + file.getImageName());
            BufferedImage image = ImageIO.read(file.getFile());
            List<Point> eyes = findEyes(image);
            StringBuilder sb = new StringBuilder();
            sb.append(file.getImageName());
            sb.append(" " + eyes.get(0).toString());
            sb.append(" " + eyes.get(1).toString());
            sb.append(" " + new Point(0, 0).toString());
            sb.append(" " + new Point(0, 0).toString());
            pw.println(sb.toString());
            counter++;
        }
        pw.close();
    }

    private double computeModificationOfAverage(double average, double value) {
        if (average == value) {
            return 0;
        } else if (value > average) {
            return 0.05 * (value - average);
        } else {
            return -0.05 * (average - value);
        }
    }

}
