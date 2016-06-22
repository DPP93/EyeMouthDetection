package pl.oi.testing;

import pl.oi.annotations.Annotations;
import pl.oi.annotations.Face;
import pl.oi.annotations.FacePoint;

import java.util.List;

import static pl.oi.annotations.Annotations.findFace;

/**
 * Created by dpp on 5/4/16.
 */
public class MSE {

    private List<Face> test;
    private List<Face> results;

    public MSE(Annotations annotations) {
        this.test = annotations.getTestElements();
        this.results = annotations.getResultElements();;
    }

    public void runTest(){
        System.out.println("-----MSE-----");
        System.out.println("MSE Left Eye");
        System.out.println(computeMSELeftEye());
        System.out.println("MSE Right Eye");
        System.out.println(computeMSERightEye());
        System.out.println("MSE Left Mouth Corner");
        System.out.println(computeMSELeftMouthCorner());
        System.out.println("MSE Right Mouth Corner");
        System.out.println(computeMSERightMouthCorner());
    }

    private double computeMSELeftEye(){
        double mse = 0;
        for(Face testFace : test){
            Face resultFace = findFace(results, testFace.getPictureName());

            mse += computeMSEOfPoints(resultFace.getLeftEye(), testFace.getLeftEye());
        }

        mse /= (double)test.size();
        return mse;
    }

    private double computeMSERightEye(){
        double mse = 0;
        for(Face testFace : test){
            Face resultFace = findFace(results, testFace.getPictureName());

            mse += computeMSEOfPoints(resultFace.getRightEye(), testFace.getRightEye());
        }

        mse /= (double)test.size();
        return mse;
    }

    private double computeMSELeftMouthCorner(){
        double mse = 0;
        for(Face testFace : test){
            Face resultFace = findFace(results, testFace.getPictureName());

            mse += computeMSEOfPoints(resultFace.getLeftMouthCorner(), testFace.getLeftMouthCorner());
        }

        mse /= (double)test.size();
        return mse;
    }

    private double computeMSERightMouthCorner(){
        double mse = 0;
        for(Face testFace : test){
            Face resultFace = findFace(results, testFace.getPictureName());

            mse += computeMSEOfPoints(resultFace.getRightMouthCorner(), testFace.getRightMouthCorner());
        }

        mse /= (double)test.size();
        return mse;
    }

    private static double computeMSEOfPoints(FacePoint testAnnotated, FacePoint recognizedAnnotated) {
        return Math.pow(testAnnotated.getX()-recognizedAnnotated.getX(), 2.0) + Math.pow(testAnnotated.getY() - recognizedAnnotated.getY(), 2.0);
    }


}
