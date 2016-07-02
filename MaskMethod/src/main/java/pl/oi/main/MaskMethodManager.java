package pl.oi.main;

import pl.oi.annotations.Annotations;
import pl.oi.annotations.Face;
import pl.oi.database.ImageDatabase;
import pl.oi.database.ImageFile;
import pl.oi.model.ImageMask;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

/**
 * Created by dpp on 5/5/16.
 */
public class MaskMethodManager {

    private ImageDatabase imageDatabase;
    private Annotations trainingAnnotations;
    private final int maskSize = 10;

    private ImageMask leftEye;
    private ImageMask rightEye;
    private ImageMask leftMouthcorner;
    private ImageMask rightMouthCorner;

    public MaskMethodManager(String mtflDirectory) throws IOException {

        trainingAnnotations = new Annotations();
        trainingAnnotations.setupElements(new File(mtflDirectory+"/training.txt"));

        imageDatabase = new ImageDatabase(mtflDirectory);

        leftEye = new ImageMask(maskSize);
        rightEye = new ImageMask(maskSize);
        leftMouthcorner = new ImageMask(maskSize);
        rightMouthCorner = new ImageMask(maskSize);
    }

    public void startLearning() throws IOException {

        List<ImageFile> testList = imageDatabase.getTrainingFiles();
        for(ImageFile file : testList){
            Face face = trainingAnnotations.getFace(file.getImageName());
            BufferedImage image = ImageIO.read(file.getFile());

            leftEye.learn(image,face.getLeftEye());
            rightEye.learn(image,face.getRightEye());
            leftMouthcorner.learn(image,face.getLeftMouthCorner());
            rightMouthCorner.learn(image,face.getRightMouthCorner());

        }
        leftEye.stopLearning();
        rightEye.stopLearning();
        leftMouthcorner.stopLearning();
        rightMouthCorner.stopLearning();
    }

    public void runTest() throws IOException {
        File f = new File("results.txt");
        f.createNewFile();
        PrintWriter pw = new PrintWriter(f);
        Random random = new Random();
        List<ImageFile> testList = imageDatabase.getTestFiles();
        for(ImageFile file : testList){

            BufferedImage image = ImageIO.read(file.getFile());

            StringBuilder sb = new StringBuilder();
            sb.append(file.getImageName());
            sb.append(" "+leftEye.testMask(image).toString());
            sb.append(" "+rightEye.testMask(image).toString());
            sb.append(" "+leftMouthcorner.testMask(image).toString());
            sb.append(" "+rightMouthCorner.testMask(image).toString());
            pw.println(sb.toString());
        }
        pw.close();
    }
}
