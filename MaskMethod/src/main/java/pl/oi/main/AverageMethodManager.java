package pl.oi.main;

import pl.oi.annotations.Annotations;
import pl.oi.annotations.Face;
import pl.oi.database.ImageDatabase;
import pl.oi.database.ImageFile;
import pl.oi.model.AverageMethod;
import pl.oi.model.ImageMask;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

/**
 * Created by dpp on 5/10/16.
 */
public class AverageMethodManager {

    private ImageDatabase imageDatabase;
    private Annotations trainingAnnotations;
    private final int maskSize = 3;

    private AverageMethod leftEye;
    private AverageMethod rightEye;
    private AverageMethod leftMouthcorner;
    private AverageMethod rightMouthCorner;
    private String resultDirectory;
    public AverageMethodManager(String mtflDirectory, String resultsDirectory) throws IOException {
        this.resultDirectory = resultsDirectory;
        trainingAnnotations = new Annotations();
        trainingAnnotations.setupElements(new File(mtflDirectory+File.pathSeparator+"training.txt"));

        imageDatabase = new ImageDatabase(mtflDirectory);

        leftEye = new AverageMethod();
        rightEye = new AverageMethod();
        leftMouthcorner = new AverageMethod();
        rightMouthCorner = new AverageMethod();
    }

    public void startLearning() throws IOException {

        List<ImageFile> testList = imageDatabase.getTrainingFiles();
        for(int i = 0; i< testList.size(); i++){
            ImageFile file = testList.get(i);
            Face face = trainingAnnotations.getFace(file.getImageName());
            BufferedImage image = ImageIO.read(file.getFile());

//            System.out.println("Left Eye "+face.getLeftEye().toString());
            leftEye.learn(image,face.getLeftEye());
            rightEye.learn(image,face.getRightEye());
//            System.out.println("Left Mouth "+face.getLeftMouthCorner().toString());
            leftMouthcorner.learn(image,face.getLeftMouthCorner());
            rightMouthCorner.learn(image,face.getRightMouthCorner());


        }
        leftEye.stopLearning();
        rightEye.stopLearning();
        leftMouthcorner.stopLearning();
        rightMouthCorner.stopLearning();
    }

    public void runTest() throws IOException {
        File f = new File(resultDirectory+File.pathSeparator+"results.txt");
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
            sb.append(" "+image.getWidth());
            sb.append(" "+image.getHeight());
            pw.println(sb.toString());
        }
        pw.close();
    }

}
