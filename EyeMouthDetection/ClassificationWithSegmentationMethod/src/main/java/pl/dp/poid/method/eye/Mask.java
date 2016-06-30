/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dp.poid.method.eye;

import java.util.Random;

/**
 *
 * @author Daniel
 */
public class Mask {
    private double[][] mask;
    
    public Mask (int size){
        mask = new double[size][size];
        Random random = new Random();
        for (int x = 0; x < mask.length; x++){
            for(int y = 0; y < mask[x].length; y++){
                mask[x][y] = random.nextInt(75)+100;
            }
        }
        
    }

    public double[][] getMask() {
        return mask;
    }
}
