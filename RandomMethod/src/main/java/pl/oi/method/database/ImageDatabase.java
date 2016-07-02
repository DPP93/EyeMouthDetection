package pl.oi.method.database;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dpp on 5/5/16.
 */
public class ImageDatabase {

    private List<ImageFile> trainingFiles;
    private List<ImageFile> testFiles;

    public ImageDatabase(String mtflDirectoryPath){

        File net = new File(mtflDirectoryPath + "/net_7876");
        File lfw = new File(mtflDirectoryPath + "/lfw_5590");
        File aflw = new File(mtflDirectoryPath + "/AFLW");

        trainingFiles = new LinkedList<>();
        testFiles = new LinkedList<>();

//        System.out.println(net.getName());
//        System.out.println(lfw.getName());
//        System.out.println(aflw.getName());

        setupList(net.listFiles(), trainingFiles);
        setupList(lfw.listFiles(), trainingFiles);
        setupList(aflw.listFiles(), testFiles);
    }

    public List<ImageFile> getTrainingFiles() {
        return trainingFiles;
    }

    public List<ImageFile> getTestFiles() {
        return testFiles;
    }

    private static void setupList(File[] fileTab, List<ImageFile> imageFileList){

        for(int i = 0; i < fileTab.length; i++){
            imageFileList.add(new ImageFile(fileTab[i]));
        }
    }

}
