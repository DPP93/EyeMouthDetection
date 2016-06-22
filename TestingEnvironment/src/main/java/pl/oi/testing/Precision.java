package pl.oi.testing;

import pl.oi.annotations.Annotations;
import pl.oi.annotations.Face;
import pl.oi.annotations.FacePoint;

import java.util.List;

import static pl.oi.annotations.Annotations.findFace;

/**
 * Created by dpp on 5/4/16.
 */
public class Precision {

    private List<Face> test;
    private List<Face> results;

    private double allowedEdge = 5;

    public Precision(Annotations annotations) {
        this.test = annotations.getTestElements();
        this.results = annotations.getResultElements();
    }

    public double getAllowedEdge() {
        return allowedEdge;
    }

    public void setAllowedEdge(double allowedEdge) {
        this.allowedEdge = allowedEdge;
    }

    public void runTest(){
        System.out.println("-----Precision-----");
        System.out.println("Precision left eye");
        System.out.println(computePrecisionForLeftEye());
        System.out.println("Precision right eye");
        System.out.println(computePrecisionForRightEye());
        System.out.println("Precision left mouth corner");
        System.out.println(computePrecisionForLeftMouthCorner());
        System.out.println("Precision right mouth corner");
        System.out.println(computePrecisionForRightMouthCorner());
    }


    private double computePrecisionForLeftEye(){
        double precision = 0;
        for(Face testFace : test){
            Face resultFace = findFace(results, testFace.getPictureName());

            if(isRecognized(resultFace.getLeftEye(), testFace.getLeftEye(), allowedEdge)){
                precision ++;
            }
        }

        precision /= (double)test.size();
        return precision;
    }

    private double computePrecisionForRightEye(){
        double precision = 0;
        for(Face testFace : test){
            Face resultFace = findFace(results, testFace.getPictureName());

            if(isRecognized(resultFace.getRightEye(), testFace.getRightEye(), allowedEdge)){
                precision ++;
            }
        }

        precision /= (double)test.size();
        return precision;
    }

    private double computePrecisionForLeftMouthCorner(){
        double precision = 0;
        for(Face testFace : test){
            Face resultFace = findFace(results, testFace.getPictureName());

            if(isRecognized(resultFace.getLeftMouthCorner(), testFace.getLeftMouthCorner(), allowedEdge)){
                precision ++;
            }
        }

        precision /= (double)test.size();
        return precision;
    }

    private double computePrecisionForRightMouthCorner(){
        double precision = 0;
        for(Face testFace : test){
            Face resultFace = findFace(results, testFace.getPictureName());

            if(isRecognized(resultFace.getRightMouthCorner(), testFace.getRightMouthCorner(), allowedEdge)){
                precision ++;
            }
        }

        precision /= (double)test.size();
        return precision;
    }

    private static boolean isRecognized(FacePoint testPoint, FacePoint resultPoint, double allowedEdge){
        double distance = Math.sqrt(Math.pow(testPoint.getX() - resultPoint.getX(), 2.0) + Math.pow(testPoint.getY() - resultPoint.getY(), 2.0));
        if(distance <= allowedEdge){
            return true;
        }
        return false;
    }

}
