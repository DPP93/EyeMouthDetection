package pl.dp.poid.imagedatabase;

import java.io.File;
import java.util.ArrayList;
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

        trainingFiles = new ArrayList<>();
        testFiles = new ArrayList<>();

//        System.out.println(net.getName());
//        System.out.println(lfw.getName());
//        System.out.println(aflw.getName());

        trainingFiles = setupList(net.listFiles(), trainingFiles);
        trainingFiles = setupList(lfw.listFiles(), trainingFiles);
        testFiles = setupList(aflw.listFiles(), testFiles);
    }

    public List<ImageFile> getTrainingFiles() {
        return trainingFiles;
    }

    public List<ImageFile> getTestFiles() {
        return testFiles;
    }

    private static List<ImageFile> setupList(File[] fileTab, List<ImageFile> imageFileList){

        for(int i = 0; i < fileTab.length; i++){
            imageFileList.add(new ImageFile(fileTab[i]));
        }
        return imageFileList;
    }

}
