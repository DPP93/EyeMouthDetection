/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dp.oi.coloranalysismethod.main;

import java.io.IOException;
import pl.dp.oi.coloranalysismethod.method.EyeFinder;

/**
 *
 * @author Daniel
 */
public class Main {
    public static void main(String args[]) throws IOException {

        EyeFinder eyeFinder = new EyeFinder("MTFL");
        System.out.println("Uczenie");
        System.out.println("Test");
        eyeFinder.runTest();
        System.out.println("Koniec można bezpiecznie wyłączyć");
    }
}
