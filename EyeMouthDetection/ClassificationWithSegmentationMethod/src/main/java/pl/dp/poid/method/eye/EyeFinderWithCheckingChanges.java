/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dp.poid.method.eye;

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
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pl.dp.poid.annotations.Annotations;
import pl.dp.poid.annotations.Face;
import pl.dp.poid.imagedatabase.ImageDatabase;
import pl.dp.poid.imagedatabase.ImageFile;
import pl.dp.poid.method.utils.ChartDrawer;
import pl.dp.poid.method.utils.ClassificationRegion;
import pl.dp.poid.method.utils.ColorModelModifier;
import pl.dp.poid.method.utils.ImageProcessing;
import pl.dp.poid.method.utils.MedianFilter;
import pl.dp.poid.method.utils.Point;
import pl.dp.poid.method.utils.SimpleClassifier;
import pl.dp.poid.segmentation.SegmentationRegionGrowth;

/**
 *
 * @author Daniel
 */
public class EyeFinderWithCheckingChanges {

    private double averageEyeBrightness = 10;
    private double averageMouthBrightness = 10;

    private ImageDatabase imageDatabase;
    private Annotations trainingAnnotations;

    private ClassificationRegion leftEyeRegion;
    private ClassificationRegion rightEyeRegion;
    private int classificationElements = 250;

    private double averageLeftX = 0;
    private double averageLeftY = 0;
    private double averageRightX = 0;
    private double averageRightY = 0;

    private double distanceToClassifierInPixels = 15;
    private String resultDirectory;
    public EyeFinderWithCheckingChanges(String databaseDirectory, String resultDirectory) throws IOException {
        this.resultDirectory = resultDirectory;
        trainingAnnotations = new Annotations();
        trainingAnnotations.setupElements(new File(databaseDirectory + File.separator +"training.txt"));
        imageDatabase = new ImageDatabase(databaseDirectory);
        leftEyeRegion = new ClassificationRegion(classificationElements, 0, 0.5, 0, 0.5);
        rightEyeRegion = new ClassificationRegion(classificationElements, 0.5, 1, 0, 0.5);
    }

    public void learn() throws IOException {
        List<ImageFile> testList = imageDatabase.getTrainingFiles();
        int[] pixelValue;
        int value;
        int lastLeftIndex = -1;
        int lastRightIndex = -1;
        for (int i = 0; i < testList.size(); i++) {
            if ((i + 1) % 100 == 0) {
                System.out.println((i + 1) + " from " + testList.size());
            }
            ImageFile file = testList.get(i);
            Face face = trainingAnnotations.getFace(file.getImageName());
            BufferedImage image = ImageProcessing.copyImage(ImageIO.read(file.getFile()));
            ColorModelModifier.convertImageToGrayscale(image);
            MedianFilter.computeMedianFilter(image);
            pixelValue = new int[image.getColorModel().getPixelSize() / 8];

            value = image.getRaster().getPixel((int) face.getLeftEye().getX(), (int) face.getLeftEye().getY(), pixelValue)[0];
            averageEyeBrightness += computeModificationOfAverage(averageEyeBrightness, value);
            ClassificationRegion.modifyClassificationRegion(leftEyeRegion,
                    SimpleClassifier.simpleClassificator(image, new Point((int) face.getLeftEye().getX(), (int) face.getLeftEye().getY())),
                    0.01, lastLeftIndex);

            ClassificationRegion.modifyClassificationRegion(
                    rightEyeRegion,
                    SimpleClassifier.simpleClassificator(image, new Point((int) face.getRightEye().getX(), (int) face.getRightEye().getY())),
                    0.01, lastRightIndex);

            value = image.getRaster().getPixel((int) face.getRightEye().getX(), (int) face.getRightEye().getY(), pixelValue)[0];
            averageEyeBrightness += computeModificationOfAverage(averageEyeBrightness, value);

            value = image.getRaster().getPixel((int) face.getLeftMouthCorner().getX(), (int) face.getLeftMouthCorner().getY(), pixelValue)[0];
            averageMouthBrightness += computeModificationOfAverage(averageMouthBrightness, value);

            value = image.getRaster().getPixel((int) face.getRightMouthCorner().getX(), (int) face.getRightMouthCorner().getY(), pixelValue)[0];
            averageMouthBrightness += computeModificationOfAverage(averageMouthBrightness, value);
        }

        XYSeries leftEye = new XYSeries("Left Eye");
        XYSeries rightEye = new XYSeries("Right Eye");

        for (int i = 0; i < leftEyeRegion.getRegion().length; i++) {
            leftEye.add(leftEyeRegion.getRegion()[i].getX(), 1.0 - leftEyeRegion.getRegion()[i].getY());
            rightEye.add(rightEyeRegion.getRegion()[i].getX(), 1.0 - rightEyeRegion.getRegion()[i].getY());
            averageLeftX += leftEyeRegion.getRegion()[i].getX();
            averageLeftY += leftEyeRegion.getRegion()[i].getY();
            averageRightX += rightEyeRegion.getRegion()[i].getX();
            averageRightY += rightEyeRegion.getRegion()[i].getY();
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(leftEye);
        dataset.addSeries(rightEye);
        JFreeChart chart = ChartFactory.createScatterPlot("Eyes", "0-1", "0-1", dataset);
        
        ChartDrawer.drawChart(chart);
        
        averageLeftX /= leftEyeRegion.getRegion().length;
        averageLeftY /= leftEyeRegion.getRegion().length;
        averageRightX /= leftEyeRegion.getRegion().length;
        averageRightY /= leftEyeRegion.getRegion().length;

        System.out.println("AVERAGE GRAYSCALE EYE VALUE " + averageEyeBrightness);
        System.out.println("Left region");
        System.out.println(leftEyeRegion.toString());
        System.out.println("Right region");
        System.out.println(rightEyeRegion.toString());
    }

    public List<Point> findEyes(BufferedImage img, int plusTolerationInSegmentation) {
        List<Point> eyes = new ArrayList<>();
        BufferedImage image = ImageProcessing.copyImage(img);
        ColorModelModifier.convertImageToGrayscale(image);
        MedianFilter.computeMedianFilter(image);

        Point closestLeftAverageSegmentation = new Point(-1, -1);
        double closestLeftToAverage = Double.MAX_VALUE;
        Point closestRightToAverageSegmenatation = new Point(-1, -1);
        double closestRightToAverage = Double.MAX_VALUE;
        
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                if (checkIfPointIsCloseToClassificationPoint(new Point(x, y), image, averageLeftX, averageLeftY)) {

                    if (checkPoint(image, new Point(x, y))) {
                        closestLeftAverageSegmentation = new Point(x, y);
                    }
                }
            }
        }

        for (int x = image.getWidth() - 1; x >= 0; x--) {
            for (int y = image.getHeight() - 1; y >= 0; y--) {
                if (checkIfPointIsCloseToClassificationPoint(new Point(x, y), image, averageRightX, averageRightY)) {
                    if (checkPoint(image, new Point(x, y))) {
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
        System.out.println("Average " + averageEyeBrightness);
        System.out.println("Left " + eyes.get(0).getX() + " " + eyes.get(0).getY() + " " + closestLeftToAverage);
        System.out.println("Right " + eyes.get(1).getX() + " " + eyes.get(1).getY() + " " + closestRightToAverage);

        return eyes;
    }

    public List<Point> findMouth(BufferedImage img, int plusTolerationInSegmentation, Point leftEye, Point rightEye) {
        List<Point> mouth = new ArrayList<>();
        BufferedImage image = ImageProcessing.copyImage(img);
        ColorModelModifier.convertImageToGrayscale(image);
        MedianFilter.computeMedianFilter(image);

        Point leftMouth = new Point(-1, -1);
        Point rightMouth = new Point(-1, -1);

        double estimatedMouthWidth = Point.computeDistanceBetweenPoints(leftEye, rightEye);
        List<Point> segmentated;
        for (int x = (int) leftEye.getX(); x < leftEye.getX() + 1; x++) {
            for (int y = (int) leftEye.getY(); y < image.getHeight() - 10; y++) {
                Point[] mouthTab = new Point[2];
                if (checkMouth(new Point(x, y), image, estimatedMouthWidth, mouthTab)) {
                    leftMouth = mouthTab[0];
                    rightMouth = mouthTab[1];
                }
            }
        }
        mouth.add(leftMouth);
        mouth.add(rightMouth);
        return mouth;
    }

    private boolean checkMouth(Point p, BufferedImage image, double estimateMouthSize, Point[] mouthTab) {

        int[] basePointPixelValue = new int[3];
        basePointPixelValue = image.getRaster().getPixel((int) p.getX() - 5, (int) p.getY(), basePointPixelValue);
        int[] pixelValue = new int[3];

        boolean leftChange = false;
        boolean mouth = false;
        boolean rightChange = false;
        
        Point leftChangePoint = new Point(0, 0);
        Point rightChangePoint = new Point(0, 0);
        //Check horizont
        for (int i = (int) p.getX() - 4; i <= p.getX() + estimateMouthSize + 5; i++) {
            pixelValue = image.getRaster().getPixel(i, (int) p.getY(), pixelValue);

            double difference = Math.abs(basePointPixelValue[0] - pixelValue[0]);
            if (difference >= 20) {
                if (leftChange && mouth) {
                    rightChange = true;
                    rightChangePoint = new Point(i, p.getY());
                    break;
                } else {
                    leftChange = true;
                    leftChangePoint = new Point(i, p.getY());
                }
            } else if (difference <= 10) {
                mouth = true;
            }
        }
        
        if (rightChange){
            mouthTab[0] = leftChangePoint;
            mouthTab[1] = rightChangePoint;
            return true;
        }
        
        return false;

    }

    private boolean checkSegmentatedIfItsMouth(List<Point> segmentatedPoints, double estimatedMouthSize, double imageHeight) {
        Point maxLeft = new Point(imageHeight, imageHeight);
        Point maxRight = new Point(0, 0);
        Point maxTop = new Point(imageHeight, imageHeight);
        Point maxBottom = new Point(0, 0);

        for (Point p : segmentatedPoints) {
            if (p.getX() <= maxLeft.getX()) {
                maxLeft = p;
            }

            if (p.getX() >= maxRight.getX()) {
                maxRight = p;
            }

            if (p.getY() <= maxTop.getY()) {
                maxTop = p;
            }

            if (p.getY() >= maxBottom.getY()) {
                maxBottom = p;
            }
        }
        double width = Point.computeDistanceBetweenPoints(maxLeft, maxRight);
        double height = Point.computeDistanceBetweenPoints(maxTop, maxBottom);
        if (width >= estimatedMouthSize * 0.9
                && width <= estimatedMouthSize * 1.1
                && height <= imageHeight * 0.1) {
            return true;
        }

        return false;
    }

    private boolean checkIfPointIsCloseToClassificationPoint(Point point, BufferedImage image, double x, double y) {

        double width = image.getWidth();
        double height = image.getHeight();
        Point p;
        p = new Point(x * width, y * height);
        if (Point.computeDistanceBetweenPoints(point, p) < distanceToClassifierInPixels) {
            return true;
        }

        return false;
    }

    public void runTest() throws IOException {
        File f = new File(resultDirectory+File.separator+"results.txt");
        f.createNewFile();
        PrintWriter pw = new PrintWriter(f);
        Random random = new Random();
        List<ImageFile> testList = imageDatabase.getTestFiles();
        int counter = 1;
        File directory = new File("checkingChangesComputedImages");
        directory.mkdir();

        for (ImageFile file : testList) {
            System.out.println(counter + " of " + testList.size() + " " + file.getImageName());
            BufferedImage image = ImageIO.read(file.getFile());
            List<Point> eyes = findEyes(image, 10);
            List<Point> mouth;
            boolean impossibleToFindMouth = false;
            for (Point p : eyes) {
                if (p.getX() < 0) {
                    impossibleToFindMouth = true;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(file.getImageName());
            sb.append(" " + eyes.get(0).toString());
            sb.append(" " + eyes.get(1).toString());
            if (!impossibleToFindMouth) {
                mouth = findMouth(image, 10, eyes.get(0), eyes.get(1));
                sb.append(" " + mouth.get(0).toString());
                sb.append(" " + mouth.get(1).toString());
                sb.append(" " + image.getWidth());
                sb.append(" " + image.getHeight());
                saveNewImage(file.getImageName(), image, eyes.get(0), eyes.get(1), mouth.get(0), mouth.get(1));
            } else {
                sb.append(" " + new Point(-1, -1).toString());
                sb.append(" " + new Point(-1, -1).toString());
                sb.append(" " + image.getWidth());
                sb.append(" " + image.getHeight());
                saveNewImage(file.getImageName(), image, eyes.get(0), eyes.get(1));
            }
            pw.println(sb.toString());
            counter++;
        }
        pw.close();
    }

    public static void saveNewImage(String identifier, BufferedImage imageToCopyAndSave, Point leftEye, Point rightEye) throws IOException {
        BufferedImage imageToSave = ImageProcessing.copyImage(imageToCopyAndSave);
        double edge = (double) imageToSave.getWidth() * 0.1 / 2.0;
        double[] colors = {255, 255, 255};
        for (int x = 0; x < imageToSave.getWidth(); x++) {
            for (int y = 0; y < imageToSave.getHeight(); y++) {
                if ((int) Point.computeDistanceBetweenPoints(leftEye, new Point(x, y)) == (int) edge) {
                    imageToSave.getRaster().setPixel(x, y, colors);
                }

                if ((int) Point.computeDistanceBetweenPoints(rightEye, new Point(x, y)) == (int) edge) {
                    imageToSave.getRaster().setPixel(x, y, colors);
                }
            }
        }

        ImageIO.write(imageToSave, "jpg", new File("checkingChangesComputedImages"+File.separator + identifier + ".jpg"));
    }

    public static void saveNewImage(String identifier, BufferedImage imageToCopyAndSave, Point leftEye, Point rightEye, Point leftMouth, Point rightMouth) throws IOException {
        BufferedImage imageToSave = ImageProcessing.copyImage(imageToCopyAndSave);
        double edge = (double) imageToSave.getWidth() * 0.1 / 2.0;
        double[] colorsEye = {255, 0, 0};
        double[] colorsMouth = {0, 0, 255};
        for (int x = 0; x < imageToSave.getWidth(); x++) {
            for (int y = 0; y < imageToSave.getHeight(); y++) {
                if ((int) Point.computeDistanceBetweenPoints(leftEye, new Point(x, y)) == (int) edge) {
                    imageToSave.getRaster().setPixel(x, y, colorsEye);
                }

                if ((int) Point.computeDistanceBetweenPoints(rightEye, new Point(x, y)) == (int) edge) {
                    imageToSave.getRaster().setPixel(x, y, colorsEye);
                }

                if ((int) Point.computeDistanceBetweenPoints(leftMouth, new Point(x, y)) == (int) edge) {
                    imageToSave.getRaster().setPixel(x, y, colorsMouth);
                }

                if ((int) Point.computeDistanceBetweenPoints(rightMouth, new Point(x, y)) == (int) edge) {
                    imageToSave.getRaster().setPixel(x, y, colorsMouth);
                }
            }
        }

        ImageIO.write(imageToSave, "jpg", new File("checkingChangesComputedImages"+File.separator + identifier + ".jpg"));
    }

    /**
     * Checks if point is possible to be eye
     *
     * @param image
     * @param point
     * @return
     */
    private boolean checkPoint(BufferedImage image, Point point) {

        int[] pixelValue = new int[3];
        int[] pointPixelValue = new int[3];

        boolean horizontal = false;
        boolean vertical = false;

        pointPixelValue = image.getRaster().getPixel((int) point.getX(), (int) point.getY(), pixelValue);

        if (pointPixelValue[0] > averageEyeBrightness + (averageEyeBrightness * 0.1)) {
            return false;
        }
        boolean leftChange = false;
        boolean pupil = false;
        boolean rightChange = false;

        boolean leftChangeV = false;
        boolean pupilV = false;
        boolean rightChangeV = false;
        //Check horizont
        for (int i = (int) point.getX() - 10; i <= point.getX() + 10; i++) {
            pixelValue = image.getRaster().getPixel(i, (int) point.getY(), pixelValue);

            double difference = Math.abs(pointPixelValue[0] - pixelValue[0]);
            if (difference >= 15) {
                if (leftChange && pupil) {
//                    System.out.println("Difference gut right");
                    rightChange = true;
                    break;
                } else {
//                    System.out.println("Difference left");
                    leftChange = true;
                    continue;
                }
            } else if (difference < 15) {
//                System.out.println("Pupil Gut");
                pupil = true;
                continue;
            }
        }

        //Check CheckVertice
        for (int i = (int) point.getY() - 10; i <= point.getY() + 10; i++) {
            pixelValue = image.getRaster().getPixel((int) point.getX(), i, pixelValue);

            double difference = Math.abs(pointPixelValue[0] - pixelValue[0]);
            if (difference > 20) {
                if (leftChangeV && pupilV) {
//                    System.out.println("Difference gut right");
                    rightChangeV = true;
                    break;
                } else {
//                    System.out.println("Difference left");
                    leftChangeV = true;
                    continue;
                }
            } else if (difference < 10) {
//                System.out.println("Pupil Gut");
                pupilV = true;
                continue;
            }
        }

        if (pupil = true) {
            horizontal = true;
        }

        if (pupilV = true) {
            vertical = true;
        }

        if (horizontal && vertical) {
            return true;
        }

        return false;
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