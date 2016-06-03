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

    private double averageEyeBrightness = 10;
    private ImageDatabase imageDatabase;
    private Annotations trainingAnnotations;

    private ClassificationRegion leftEyeRegion;
    private ClassificationRegion rightEyeRegion;


    public EyeFinder(String databaseDirectory) throws IOException {
        trainingAnnotations = new Annotations();
        trainingAnnotations.setupElements(new File(databaseDirectory+"/training.txt"));
        imageDatabase = new ImageDatabase(databaseDirectory);
        leftEyeRegion = new ClassificationRegion(4, 0,0.5);
        rightEyeRegion = new ClassificationRegion(4, 0.5,1);
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
            ClassificationRegion.modifyClassificationRegion(leftEyeRegion,
                    SimpleClassifier.simpleClassificator(image, new Point((int)face.getLeftEye().getX(),(int)face.getLeftEye().getY())),
                    0.01);

            ClassificationRegion.modifyClassificationRegion(
                    rightEyeRegion,
                    SimpleClassifier.simpleClassificator(image, new Point((int)face.getRightEye().getX(),(int)face.getRightEye().getY())),
                    0.01);

            value = image.getRaster().getPixel((int)face.getRightEye().getX(), (int)face.getRightEye().getY(), pixelValue)[0];
            averageEyeBrightness += computeModificationOfAverage(averageEyeBrightness, value);
        }

        System.out.println("AVERAGE GRAYSCALE EYE VALUE "+averageEyeBrightness);
        System.out.println("Left region");
        System.out.println(leftEyeRegion.toString());
        System.out.println("Right region");
        System.out.println(rightEyeRegion.toString());
    }

    public List<Point> findEyes(BufferedImage img, int plusTolerationInSegmentation){
        List<Point> eyes = new ArrayList<>();
        BufferedImage image = ImageProcessing.copyImage(img);
        ColorModelModifier.convertImageToGrayscale(image);
        MedianFilter.computeMedianFilter(image);

        Point closestLeftAverageSegmentation = new Point(-1,-1);
        double closestLeftToAverage = Double.MAX_VALUE;
        Point closestRightToAverageSegmenatation = new Point(-1,-1);
        double closestRightToAverage = Double.MAX_VALUE;
        int[] pixelValue = new int[image.getColorModel().getPixelSize()/8];
        for(int x = 25; x < image.getWidth()-25; x++) {
            for (int y = 25; y < image.getHeight()-25; y++){
//                Zrób segmentację i sprawdź czy z tych wartości wychodzi obszar
                List<Point> elementsSegmanetated = SegmentationRegionGrowth.
                        segmentateImageByGrayscale(image, 5, new Point(x,y));

                //Compute average from this elements
                double avg = 0;
                for (Point p : elementsSegmanetated) {
                    avg += image.getRaster().getPixel((int)p.getX(), (int)p.getY(), pixelValue)[0];
                }
                avg /= elementsSegmanetated.size();

                if (closestLeftToAverage > Math.pow(avg - averageEyeBrightness,2)){
                    closestLeftToAverage = avg;
                    closestLeftAverageSegmentation = new Point(x,y);
                }else if( closestRightToAverage > Math.pow(avg - averageEyeBrightness,2)) {
                    closestRightToAverage = avg;
                    closestRightToAverageSegmenatation = new Point(x, y);
                }
            }
        }

        if(closestLeftAverageSegmentation.getX() < closestRightToAverageSegmenatation.getX()){
            eyes.add(closestLeftAverageSegmentation);
            eyes.add(closestRightToAverageSegmenatation);
        }else{
            eyes.add(closestRightToAverageSegmenatation);
            eyes.add(closestLeftAverageSegmentation);
        }
        System.out.println("Average "+averageEyeBrightness);
        System.out.println("Left "+eyes.get(0).getX() + " " + eyes.get(0).getY() + " " + closestLeftToAverage);
        System.out.println("Right "+eyes.get(1).getX() + " " + eyes.get(1).getY() + " " + closestRightToAverage);

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
