package pl.dp.poid.method.utils;

import java.util.Random;

/**
 * Created by Daniel on 2016-06-03.
 */
public class RandomUtils {

    private static Random random = new Random();

    public static double getRandomDoubleFromBoundaries(double b1, double b2){
        double result = 0;
        do{
            result = random.nextDouble();
        }while ( !(result >= b1 && result <= b2) );
        return result;
    }
}
