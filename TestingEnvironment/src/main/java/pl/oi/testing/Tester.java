package pl.oi.testing;

import pl.oi.annotations.Annotations;
import pl.oi.testing.recent.ConfusionMatrixManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * Run all test types
 * Created by dpp on 5/4/16.
 */
public class Tester {

    private MSE mse;
    private Precision precision;
    private FalseDiscoveryRate falseDiscoveryRate;
    private Annotations annotations;


    public Tester(File testFile, File resultFile) throws IOException {

        annotations = new Annotations();
        annotations.setupTestElements(testFile);
        annotations.setResultElements(resultFile);

        mse = new MSE(annotations);
        precision = new Precision(annotations);
        falseDiscoveryRate = new FalseDiscoveryRate(annotations);
    }

    public Tester(BufferedReader testFile, File resultFile) throws IOException {

        annotations = new Annotations();
        annotations.setupTestElements(testFile);
        annotations.setResultElements(resultFile);

        mse = new MSE(annotations);
        precision = new Precision(annotations);
        falseDiscoveryRate = new FalseDiscoveryRate(annotations);
    }

    public void runTests(){
        mse.runTest();
        precision.runTest();
        falseDiscoveryRate.runTest();
    }

    public void runRectangleTest(){
        ConfusionMatrixManager cmm = new ConfusionMatrixManager(annotations);
        cmm.runTests();
    }

}
