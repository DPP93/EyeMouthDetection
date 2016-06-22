/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.oi.testing.recent;

import java.awt.Dimension;
import java.util.List;
import pl.oi.annotations.Annotations;
import pl.oi.annotations.Face;
import pl.oi.utils.Euclides;

/**
 *
 * @author Daniel
 */
public class ConfusionMatrixManager {
    
    private ConfusionMatrix leftEyeConfusionMatrix;
    private ConfusionMatrix rightEyeConfusionMatrix;
    private ConfusionMatrix mouthConfusionMatrix;
    
    List<Face> resultFaceList;
    List<Face> testFaceList;
    List<Dimension> imageDimensions;
    
    private double surfaceRightRecognitionEdge = 0.3;
    
    public ConfusionMatrixManager(Annotations annotations) {
        resultFaceList = annotations.getResultElements();
        testFaceList = annotations.getTestElements();
        imageDimensions = annotations.getImageDimensions();

        leftEyeConfusionMatrix = new ConfusionMatrix();
        rightEyeConfusionMatrix = new ConfusionMatrix();
        mouthConfusionMatrix = new ConfusionMatrix();
    }

    public void runTests(){
        computeLeftEye();
        computeRightEye();
        computeMouth();

        System.out.println("Results of test for left eye");
        System.out.println(leftEyeConfusionMatrix.computeAllElements());
        System.out.println("Results of test for right eye");
        System.out.println(rightEyeConfusionMatrix.computeAllElements());
        System.out.println("Results of test for mouth");
        System.out.println(mouthConfusionMatrix.computeAllElements());
    }

    private void computeLeftEye(){
        //Wyznacz punkty prostokąta dla szerokości i wysokości - szerokość - 10% obrazu, wysokość - połowa szerokości
        //Zrób to samo zarówno dla testowanego punktu, jak i wyznaczonego z metody, jeśli pokrywają się w przynajmniej 30%
        //Oznacza to, że oko jest poprawnie rozpoznane, jeśli mamy -1 -1, to znaczy, że oko nie zostało rozpoznane przez metodę

        double rectangleASide = 0, rectangleBSide = 0;
        double minDistanceToDecide = 0;
        for (int faceIndex = 0; faceIndex < resultFaceList.size(); faceIndex++) {
            if (testFaceList.get(faceIndex).getLeftEye().getX() == -1){
                leftEyeConfusionMatrix.incrementFN();
                continue;
            }

            rectangleASide = imageDimensions.get(faceIndex).getWidth() * 0.1 / 4.0;
            rectangleBSide = imageDimensions.get(faceIndex).getWidth() * 0.1 / 2.0;
            minDistanceToDecide = Math.sqrt(Math.pow(rectangleASide, 2.0) + Math.pow(rectangleBSide, 2.0));
            if (Euclides.computeDistanceBetweenPoints(testFaceList.get(faceIndex).getLeftEye(), resultFaceList.get(faceIndex).getLeftEye())
                    <= minDistanceToDecide){
                leftEyeConfusionMatrix.incrementTP();
            }else{
                leftEyeConfusionMatrix.incrementFP();
            }

        }
    }

    private void computeRightEye(){
        //Wyznacz punkty prostokąta dla szerokości i wysokości - szerokość - 10% obrazu, wysokość - połowa szerokości
        //Zrób to samo zarówno dla testowanego punktu, jak i wyznaczonego z metody, jeśli pokrywają się w przynajmniej 30%
        //Oznacza to, że oko jest poprawnie rozpoznane, jeśli mamy -1 -1, to znaczy, że oko nie zostało rozpoznane przez metodę

        double rectangleASide = 0, rectangleBSide = 0;
        double minDistanceToDecide = 0;
        for (int faceIndex = 0; faceIndex < resultFaceList.size(); faceIndex++) {
            if (testFaceList.get(faceIndex).getRightEye().getX() == -1){
                rightEyeConfusionMatrix.incrementFN();
                continue;
            }
            rectangleASide = imageDimensions.get(faceIndex).getWidth() * 0.1 / 4.0;
            rectangleBSide = imageDimensions.get(faceIndex).getWidth() * 0.1 / 2.0;
            minDistanceToDecide = Math.sqrt(Math.pow(rectangleASide, 2.0) + Math.pow(rectangleBSide, 2.0));
            if (Euclides.computeDistanceBetweenPoints(testFaceList.get(faceIndex).getRightEye(), resultFaceList.get(faceIndex).getRightEye())
                    <= minDistanceToDecide){
                rightEyeConfusionMatrix.incrementTP();
            }else{
                rightEyeConfusionMatrix.incrementFP();
            }

        }
    }

    private void computeMouth(){

    }
    
}
