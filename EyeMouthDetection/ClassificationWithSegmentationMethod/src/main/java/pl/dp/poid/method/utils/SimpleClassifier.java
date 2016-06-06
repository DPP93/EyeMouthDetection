package pl.dp.poid.method.utils;

import java.awt.image.BufferedImage;

/**
 * Created by dpp on 6/2/16.
 */
public class SimpleClassifier {

    public static Point simpleClassificator(BufferedImage b, Point featureTrainingPoint){
        double x = featureTrainingPoint.getX() / (double)b.getWidth();
        double y = featureTrainingPoint.getY() / (double)b.getHeight();
        return new Point(x,y);
    }

}
