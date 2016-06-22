package pl.oi.testing;

import pl.oi.annotations.Annotations;
import pl.oi.annotations.Face;
import pl.oi.annotations.FacePoint;

import java.util.List;

import static pl.oi.annotations.Annotations.findFace;

/**
 * Created by dpp on 5/4/16.
 */
public class FalseDiscoveryRate {
    private List<Face> test;
    private List<Face> results;

    private double allowedEdge = 5;

    public FalseDiscoveryRate(Annotations annotations) {
        this.test = annotations.getTestElements();
        this.results = annotations.getResultElements();;
    }

    public double getAllowedEdge() {
        return allowedEdge;
    }

    public void setAllowedEdge(double allowedEdge) {
        this.allowedEdge = allowedEdge;
    }

    public void runTest(){
        System.out.println("-----False Discovery Rate-----");
        System.out.println("False Discovery Rate left eye");
        System.out.println(computeFalseDiscoveryRateForLeftEye());
        System.out.println("False Discovery Rate right eye");
        System.out.println(computeFalseDiscoveryRateForRightEye());
        System.out.println("False Discovery Rate left mouth corner");
        System.out.println(computeFalseDiscoveryRateForLeftMouthCorner());
        System.out.println("False Discovery Rate right mouth corner");
        System.out.println(computeFalseDiscoveryRateForRightMouthCorner());
    }


    private double computeFalseDiscoveryRateForLeftEye(){
        double falseDiscoveryRate = 0;
        for(Face testFace : test){
            Face resultFace = findFace(results, testFace.getPictureName());

            if(isNotRecognized(resultFace.getLeftEye(), testFace.getLeftEye(), allowedEdge)){
                falseDiscoveryRate ++;
            }
        }

        falseDiscoveryRate /= (double)test.size();
        return falseDiscoveryRate;
    }

    private double computeFalseDiscoveryRateForRightEye(){
        double falseDiscoveryRate = 0;
        for(Face testFace : test){
            Face resultFace = findFace(results, testFace.getPictureName());

            if(isNotRecognized(resultFace.getRightEye(), testFace.getRightEye(), allowedEdge)){
                falseDiscoveryRate ++;
            }
        }

        falseDiscoveryRate /= (double)test.size();
        return falseDiscoveryRate;
    }

    private double computeFalseDiscoveryRateForLeftMouthCorner(){
        double falseDiscoveryRate = 0;
        for(Face testFace : test){
            Face resultFace = findFace(results, testFace.getPictureName());

            if(isNotRecognized(resultFace.getLeftMouthCorner(), testFace.getLeftMouthCorner(), allowedEdge)){
                falseDiscoveryRate ++;
            }
        }

        falseDiscoveryRate /= (double)test.size();
        return falseDiscoveryRate;
    }

    private double computeFalseDiscoveryRateForRightMouthCorner(){
        double falseDiscoveryRate = 0;
        for(Face testFace : test){
            Face resultFace = findFace(results, testFace.getPictureName());

            if(isNotRecognized(resultFace.getRightMouthCorner(), testFace.getRightMouthCorner(), allowedEdge)){
                falseDiscoveryRate ++;
            }
        }

        falseDiscoveryRate /= (double)test.size();
        return falseDiscoveryRate;
    }

    private static boolean isNotRecognized(FacePoint testPoint, FacePoint resultPoint, double allowedEdge){
        double distance = Math.sqrt(Math.pow(testPoint.getX() - resultPoint.getX(), 2.0) + Math.pow(testPoint.getY() - resultPoint.getY(), 2.0));
        if(distance > allowedEdge){
            return true;
        }
        return false;
    }
}
