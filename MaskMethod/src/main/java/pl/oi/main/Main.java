package pl.oi.main;

import pl.oi.model.AverageMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by dpp on 5/5/16.
 */
public class Main {

    public static void main(String[] args){
        try {
//            MaskMethodManager maskMethodManager = new MaskMethodManager(args[0]);
//            maskMethodManager.startLearning();
//            maskMethodManager.runTest();

            File jarPath=new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
//            String propertiesPath=jarPath.getParentFile().getAbsolutePath();
            AverageMethodManager averageMethodManager = new AverageMethodManager(args[0], args[1]);
            averageMethodManager.startLearning();
            averageMethodManager.runTest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
