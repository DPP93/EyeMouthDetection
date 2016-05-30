package image.processing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2016-05-29.
 */
public class SobelOperator extends ImageProcessing{

    private final double[][] gxMask = {
            {-1,0,1},
            {-2,0,2},
            {-1,0,1}
    };

    private final double[][] gyMask = {
            {1, 2, 1},
            {0, 0, 0},
            {-1, -2, -1}
    };

    public SobelOperator() {
        super();
        indentifier = "ImagesTests/TESTS-SOBEL";
    }

    public void computeHorizontalEdgeDetectionMasks(){
        List<BufferedImage> copyOfWorkingImages = new ArrayList<>();
        copyOfWorkingImages.addAll(workingImages);
        for (int i = 0; i < copyOfWorkingImages.size(); i++) {
            BufferedImage b = copyOfWorkingImages.get(i);
            for (int x = 1; x < b.getWidth() - 1; x++) {
                for (int y = 1; y < b.getHeight() - 1; y++) {
                    double[] gx = new double[b.getColorModel().getPixelSize()/8];
                    double[] gy = new double[b.getColorModel().getPixelSize()/8];
                    double[] colorConvoluted = new double[b.getColorModel().getPixelSize()/8];

                    for(int maskX = 0; maskX < gxMask.length; maskX++) {
                        for(int maskY = 0; maskY < gxMask[maskX].length; maskY++) {
                            BufferedImage orig = workingImages.get(i);
                            double[] color = new double[orig.getColorModel().getPixelSize()/8];
                            color = orig.getRaster().getPixel(x-1+maskX, y-1+maskY, color);
                            for (int k = 0; k < colorConvoluted.length; k++){
                                gx[k] += (color[k] * gxMask[maskX][maskY]);
                            }
                        }
                    }

                    for(int maskX = 0; maskX < gyMask.length; maskX++) {
                        for(int maskY = 0; maskY < gyMask[maskX].length; maskY++) {
                            BufferedImage orig = workingImages.get(i);
                            double[] color = new double[orig.getColorModel().getPixelSize()/8];
                            color = orig.getRaster().getPixel(x-1+maskX, y-1+maskY, color);
                            for (int k = 0; k < colorConvoluted.length; k++){
                                gy[k] += (color[k] * gyMask[maskX][maskY]);
                            }
                        }
                    }



                    for (int k = 0; k < colorConvoluted.length; k++) {

                        colorConvoluted[k] = Math.sqrt(Math.pow(gx[k], 2.0) + Math.pow(gy[k], 2.0));

                        if (colorConvoluted[k] > 255){
                            colorConvoluted[k] = 255;
                        }else if(colorConvoluted[k] < 0){
                            colorConvoluted[k] = 0;
                        }
                    }
                    b.getRaster().setPixel(x,y,colorConvoluted);
                }
            }
            workingImages.set(i, b);
        }
    }
}
