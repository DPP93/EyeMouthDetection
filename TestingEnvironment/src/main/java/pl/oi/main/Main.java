package pl.oi.main;

import pl.oi.testing.Tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * First args mean path to txt file with test annotations, second is for result from method
 * Created by dpp on 5/4/16.
 */
public class Main {

    
    //UWAGA OD TEJ PORY PLIK RESULTS.TXT POWINIEN MIEĆ DWIE KOLEJNE WARTOŚCI: SZEROKOŚĆ I WYSOKOŚĆ BADANEGO OBRAZKA
    public static void main(String[] args){
        System.out.println("-----Testing Environment-----");

//        File test = new File("testing.txt");
//        File results = new File("results.txt");
//        File test = new File(args[0]);
//        File test = new File(Main.class.getResource("testing.txt").getPath());
        BufferedReader test = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/testing.txt")));
//        File test = new File("testing.txt");
        File results = new File(args[0]);
//        File results = new File("results.txt");

        try {
            Tester tester = new Tester(test, results);
            tester.runRectangleTest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
