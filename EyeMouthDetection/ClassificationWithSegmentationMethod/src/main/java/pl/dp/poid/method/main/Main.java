package pl.dp.poid.method.main;

import java.io.IOException;
import pl.dp.poid.method.eye.EyeFinderWithCheckingChanges;
import pl.dp.poid.method.eye.MaskFinder;

/**
 * Created by Daniel on 2016-05-28.
 */

//args[0] - gdzie jest baza
//args[1] - gdzie ma dać wynik
public class Main {
    public static void main(String args[]) throws IOException {
        
        EyeFinderWithCheckingChanges eyeFinderWithCheckingChanges = new EyeFinderWithCheckingChanges(args[0], args[1]);
        System.out.println("Uczenie");
        eyeFinderWithCheckingChanges.learn();
        System.out.println("Test");
        eyeFinderWithCheckingChanges.runTest();
        System.out.println("Koniec metody");
        
//        MaskFinder mask = new MaskFinder(args[0], args[1]);
//        mask.learn();
//        mask.runTest();
//        System.out.println("Koniec maski");
    }
}
