package pl.oi.method;



import pl.oi.method.annotations.Annotations;
import pl.oi.method.annotations.Face;
import pl.oi.method.database.ImageDatabase;
import pl.oi.method.database.ImageFile;

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
public class Method{

    private Annotations trainingAnnotations;
    private Annotations testAnnotations;
    private ImageDatabase imageDatabase;
    
    private String resultDirectory;
    public Method(String mtflDirectoryPath, String resultDirectory) throws IOException {
        this.resultDirectory = resultDirectory;
        trainingAnnotations = new Annotations();
        trainingAnnotations.setupElements(new File(mtflDirectoryPath + File.pathSeparator +"training.txt"));

        imageDatabase = new ImageDatabase(mtflDirectoryPath);
    }

    public void learn(){

    }

    public void testMethod() throws IOException {
        File f = new File(resultDirectory + File.pathSeparator + "results.txt");
        f.createNewFile();
        PrintWriter pw = new PrintWriter(f);
        Random random = new Random();
        List<ImageFile> testList = imageDatabase.getTestFiles();
        for(ImageFile file : testList){
            Face face = testAnnotations.getFace(file.getImageName());
            BufferedImage image = ImageIO.read(file.getFile());

            StringBuilder sb = new StringBuilder();
            int w = image.getWidth();
            int h = image.getHeight();
            sb.append(file.getImageName());
            sb.append(" "+random.nextInt(w)+" "+ random.nextInt(h));
            sb.append(" "+random.nextInt(w)+" "+ random.nextInt(h));
            sb.append(" "+random.nextInt(w)+" "+ random.nextInt(h));
            sb.append(" "+random.nextInt(w)+" "+ random.nextInt(h));
            sb.append(" "+w+" "+h);
            pw.println(sb.toString());
        }

        pw.close();
    }


}
