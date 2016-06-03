package method.eye;

import annotations.Annotations;
import annotations.Face;
import database.ImageDatabase;
import database.ImageFile;
import method.utils.*;
import segmentation.SegmentationRegionGrowth;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Daniel on 2016-06-03.
 */
public class EyeFinder {

    private double averageEyeBrightness = 50;
    private ImageDatabase imageDatabase;
    private Annotations trainingAnnotations;

    private ClassificationRegion leftEyeRegion;
    private ClassificationRegion rightEyeRegion;


    public EyeFinder(String databaseDirectory) throws IOException {
        trainingAnnotations = new Annotations();
        trainingAnnotations.setupElements(new File(databaseDirectory+"/training.txt"));
        imageDatabase = new ImageDatabase(databaseDirectory);
        leftEyeRegion = new ClassificationRegion(new Point(0,0),new Point(0.1,0), new Point(0,0.1), new Point(0.1,0.1));
        rightEyeRegion = new ClassificationRegion(new Point(0.8,0.0),new Point(0.9,0.0), new Point(0.8,0.1), new Point(0.9,0.1));
    }

    public void learn() throws IOException {
        List<ImageFile> testList = imageDatabase.getTrainingFiles();
        int[] pixelValue;
        int value;
        for(int i = 0; i< testList.size(); i++){
//            if ( i % testList.size()/10 == 1){
//                System.out.println((i+1) + " from "+ testList.size());
//            }
            ImageFile file = testList.get(i);
            Face face = trainingAnnotations.getFace(file.getImageName());
            BufferedImage image = ImageProcessing.copyImage(ImageIO.read(file.getFile()));
            ColorModelModifier.convertImageToGrayscale(image);
            MedianFilter.computeMedianFilter(image);
            pixelValue = new int[image.getColorModel().getPixelSize()/8];

            value = image.getRaster().getPixel((int)face.getLeftEye().getX(), (int)face.getLeftEye().getY(), pixelValue)[0];
            averageEyeBrightness += computeModificationOfAverage(averageEyeBrightness, value);

            value = image.getRaster().getPixel((int)face.getRightEye().getX(), (int)face.getRightEye().getY(), pixelValue)[0];
            averageEyeBrightness += computeModificationOfAverage(averageEyeBrightness, value);
        }

        System.out.println("AVERAGE GRAYSCALE EYE VALUE "+averageEyeBrightness);
    }

    public List<Point> findEyes(BufferedImage img, int plusTolerationInSegmentation){
        List<Point> eyes = new ArrayList<>();
        BufferedImage image = ImageProcessing.copyImage(img);
        ColorModelModifier.convertImageToGrayscale(image);
        MedianFilter.computeMedianFilter(image);

        Point closestAverageSegmentation = new Point(-1,-1);
        double closestToAverage = Double.MAX_VALUE;
        Point secondClosestAverageSegmentation = new Point(-1,-1);
        double secondClosestToAverage = Double.MAX_VALUE;
        int[] pixelValue = new int[image.getColorModel().getPixelSize()/8];
        for(int x = 10; x < image.getWidth()-10; x+=5) {
            for (int y = 10; y < image.getHeight()-10; y+=5){
//                Zrób segmentację i sprawdź czy z tych wartości wychodzi obszar
                List<Point> elementsSegmanetated = SegmentationRegionGrowth.
                        segmentateImageByGrayscale(image, 5, new Point(x,y));

                //Compute average from this elements
                double avg = 0;
                for (Point p : elementsSegmanetated) {
                    avg += image.getRaster().getPixel((int)p.getX(), (int)p.getY(), pixelValue)[0];
                }
                avg /= elementsSegmanetated.size();

                if (closestToAverage > Math.pow(avg - averageEyeBrightness,2)){
                    closestToAverage = avg;
                    closestAverageSegmentation = new Point(x,y);
                }else if( secondClosestToAverage > Math.pow(avg - averageEyeBrightness,2)) {
                    secondClosestToAverage = avg;
                    secondClosestAverageSegmentation = new Point(x, y);
                }
            }
        }

        if(closestAverageSegmentation.getX() < secondClosestAverageSegmentation.getX()){
            eyes.add(closestAverageSegmentation);
            eyes.add(secondClosestAverageSegmentation);
        }else{
            eyes.add(secondClosestAverageSegmentation);
            eyes.add(closestAverageSegmentation);
        }
        System.out.println("Average "+averageEyeBrightness);
        System.out.println("Left "+eyes.get(0).getX() + " " + eyes.get(0).getY() + " " + closestToAverage);
        System.out.println("Right "+eyes.get(1).getX() + " " + eyes.get(1).getY() + " " + secondClosestToAverage);

        return eyes;
    }

    public void runTest() throws IOException {
        File f = new File("results.txt");
        f.createNewFile();
        PrintWriter pw = new PrintWriter(f);
        Random random = new Random();
        List<ImageFile> testList = imageDatabase.getTestFiles();
        int counter = 1;
        for(ImageFile file : testList){
            System.out.println(counter + " of "+testList.size() + " "+file.getImageName());
            BufferedImage image = ImageIO.read(file.getFile());
            List<Point> eyes = findEyes(image, 10);
            StringBuilder sb = new StringBuilder();
            sb.append(file.getImageName());
            sb.append(" "+eyes.get(0).toString());
            sb.append(" "+eyes.get(1).toString());
            sb.append(" "+new Point(0,0).toString());
            sb.append(" "+new Point(0,0).toString());
            pw.println(sb.toString());
            counter++;
        }
        pw.close();
    }


    private double computeModificationOfAverage(double average, double value){
        if (average == value){
            return 0;
        }
        else if (value > average){
            return 0.05 * (value - average);
        }else{
            return -0.05 * (average - value);
        }
    }


}
