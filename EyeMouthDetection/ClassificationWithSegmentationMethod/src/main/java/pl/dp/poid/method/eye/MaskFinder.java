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
import pl.dp.poid.annotations.Annotations;
import pl.dp.poid.annotations.Face;
import pl.dp.poid.annotations.FacePoint;
import pl.dp.poid.imagedatabase.ImageDatabase;
import pl.dp.poid.imagedatabase.ImageFile;
import static pl.dp.poid.method.eye.EyeFinderWithCheckingChanges.saveNewImage;
import pl.dp.poid.method.utils.ClassificationRegion;
import pl.dp.poid.method.utils.ColorModelModifier;
import pl.dp.poid.method.utils.ImageProcessing;
import pl.dp.poid.method.utils.MedianFilter;
import pl.dp.poid.method.utils.Point;

/**
 *
 * @author Daniel
 */
public class MaskFinder {

    private ImageDatabase imageDatabase;
    private Annotations trainingAnnotations;
    private int maskSize = 21;
    private Mask leftEye;
    private Mask rightEye;
    private Mask leftMouth;
    private Mask rightMouth;
    private String resultDirectory;
    public MaskFinder(String databaseDirectory, String resultDirectory) throws IOException {
        this.resultDirectory = resultDirectory;
        trainingAnnotations = new Annotations();
        trainingAnnotations.setupElements(new File(databaseDirectory + File.separator + "training.txt"));
        imageDatabase = new ImageDatabase(databaseDirectory);
    }

    public void learn() throws IOException {
        leftEye = new Mask(maskSize);
        rightEye = new Mask(maskSize);
        leftMouth = new Mask(maskSize);
        rightMouth = new Mask(maskSize);

        FacePoint le;
        FacePoint re;
        FacePoint lm;
        FacePoint rm;
        int halfOfMaskSize = (maskSize - 1) / 2;
        List<ImageFile> testList = imageDatabase.getTrainingFiles();
        for (int i = 0; i < testList.size(); i++) {

            if (i % 500 == 0) {
                System.out.println((i + 1) + " of " + testList.size());
            }

            ImageFile file = testList.get(i);
            Face face = trainingAnnotations.getFace(file.getImageName());
            BufferedImage image = ImageProcessing.copyImage(ImageIO.read(file.getFile()));
            ColorModelModifier.convertImageToGrayscale(image);
            MedianFilter.computeMedianFilter(image);

            le = face.getLeftEye();
            re = face.getRightEye();
            lm = face.getLeftMouthCorner();
            rm = face.getRightMouthCorner();
            computeMask(leftEye, le, image);
            computeMask(rightEye, re, image);
            computeMask(leftMouth, lm, image);
            computeMask(rightMouth, rm, image);

        }
    }

    public void runTest() throws IOException {
        File f = new File(resultDirectory+ File.separator +"results.txt");
        f.createNewFile();
        PrintWriter pw = new PrintWriter(f);
        Random random = new Random();
        List<ImageFile> testList = imageDatabase.getTestFiles();
        File dir = new File("MaskComputedImages");
        dir.mkdir();
        int counter = 1;
        for (ImageFile file : testList) {
            System.out.println(counter + " of " + testList.size() + " " + file.getImageName());
            BufferedImage image = ImageIO.read(file.getFile());
            image = ImageProcessing.copyImage(image);
            image = ColorModelModifier.convertImageToGrayscale(image);
            image = MedianFilter.computeMedianFilter(image);
            List<Point> eyes = findEyes(image);
            List<Point> mouth = findMouth(image);
            StringBuilder sb = new StringBuilder();
            sb.append(file.getImageName());
            sb.append(" " + eyes.get(0).toString());
            sb.append(" " + eyes.get(1).toString());
            sb.append(" " + mouth.get(0).toString());
            sb.append(" " + mouth.get(1).toString());
            sb.append(" " + image.getWidth());
            sb.append(" " + image.getHeight());
            pw.println(sb.toString());
            saveNewImage(file.getImageName(), image, eyes.get(0), eyes.get(1), mouth.get(0), mouth.get(1));
            counter++;
        }
        pw.close();
    }

    private List<Point> findEyes(BufferedImage image) {
        List<Point> eyes = new ArrayList<>();

        int halfOfMaskSize = (leftEye.getMask().length - 1) / 2;
        double minMSE = Double.MAX_VALUE;
        Point minLeft = new Point(-1, -1);
        Point minRight = new Point(-1, -1);
        for (int x = halfOfMaskSize + 1; x < image.getWidth() - halfOfMaskSize - 1; x++) {
            for (int y = halfOfMaskSize + 1; y < image.getHeight() - halfOfMaskSize - 1; y++) {
                if (minMSE >= computeMSE(leftEye, image, new Point(x, y))) {
                    minMSE = computeMSE(leftEye, image, new Point(x, y));
                    minLeft = new Point(x, y);
                }
            }
        }
        minMSE = Double.MAX_VALUE;
        for (int x = image.getWidth() - halfOfMaskSize - 1; x > halfOfMaskSize + 1; x--) {
            for (int y = image.getHeight() - halfOfMaskSize - 1; y > halfOfMaskSize + 1; y--) {
                if (minMSE >= computeMSE(rightEye, image, new Point(x, y))) {
                    minMSE = computeMSE(rightEye, image, new Point(x, y));
                    minRight = new Point(x, y);
                }
            }
        }
        eyes.add(minLeft);
        eyes.add(minRight);
        return eyes;
    }

    private List<Point> findMouth(BufferedImage image) {
        List<Point> mouth = new ArrayList<>();

        int halfOfMaskSize = (leftMouth.getMask().length - 1) / 2;
        double minMSE = Double.MAX_VALUE;
        Point minLeft = new Point(-1, -1);
        Point minRight = new Point(-1, -1);
        for (int x = halfOfMaskSize + 1; x < image.getWidth() - halfOfMaskSize - 1; x++) {
            for (int y = halfOfMaskSize + 1; y < image.getHeight() - halfOfMaskSize - 1; y++) {
                if (minMSE >= computeMSE(leftMouth, image, new Point(x, y))) {
                    minMSE = computeMSE(leftMouth, image, new Point(x, y));
                    minLeft = new Point(x, y);
                }
            }
        }
        minMSE = Double.MAX_VALUE;
        for (int x = image.getWidth() - halfOfMaskSize - 1; x > halfOfMaskSize + 1; x--) {
            for (int y = image.getHeight() - halfOfMaskSize - 1; y > halfOfMaskSize + 1; y--) {
                if (minMSE >= computeMSE(rightMouth, image, new Point(x, y))) {
                    minMSE = computeMSE(rightMouth, image, new Point(x, y));
                    minRight = new Point(x, y);
                }
            }
        }
        mouth.add(minLeft);
        mouth.add(minRight);
        return mouth;
    }

    private double computeMSE(Mask mask, BufferedImage image, Point point) {
        double result = 0;
        int halfOfMaskSize = (mask.getMask().length - 1) / 2;
        int maskX = 0, maskY = 0;
        for (int x = (int) (point.getX() - halfOfMaskSize); x < point.getX() + halfOfMaskSize - 1; x++) {
            maskY = 0;
            for (int y = (int) (point.getY() - halfOfMaskSize); y < point.getY() + halfOfMaskSize - 1; y++) {
                double value = image.getRaster().getPixel(x, y, new double[3])[0];
                result += Math.pow(value - mask.getMask()[maskX][maskY], 2.0);
                maskY++;
            }
            maskX++;
        }
        return result;
    }

    private void computeMask(Mask mask, FacePoint point, BufferedImage image) {
        int halfOfMaskSize = (mask.getMask().length - 1) / 2;
        int maskX = 0, maskY = 0;
        for (int x = (int) (point.getX() - halfOfMaskSize); x < point.getX() + halfOfMaskSize - 1; x++) {
            maskY = 0;
            for (int y = (int) (point.getY() - halfOfMaskSize); y < point.getY() + halfOfMaskSize - 1; y++) {
                double value = image.getRaster().getPixel(x, y, new double[3])[0];
                mask.getMask()[maskX][maskY] = computeModificationOfAverage(mask.getMask()[maskX][maskY], value);
                maskY++;
            }
            maskX++;
        }
    }

    private double computeModificationOfAverage(double average, double value) {
        if (average == value) {
            return 0;
        } else if (value > average) {
            return 0.09 * (value - average);
        } else {
            return -0.09 * (average - value);
        }
    }
}
