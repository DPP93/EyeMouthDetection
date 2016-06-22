package pl.oi.utils;

import pl.oi.annotations.FacePoint;


/**
 * Created by Daniel on 2016-06-21.
 */
public class Euclides {
    public static double computeDistanceBetweenPoints(FacePoint a, FacePoint b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2.0) + Math.pow(a.getY() - b.getY(), 2.0));
    }
}
