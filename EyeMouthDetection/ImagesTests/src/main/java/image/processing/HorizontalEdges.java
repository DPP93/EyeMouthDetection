package image.processing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2016-05-29.
 */
public class HorizontalEdges extends ImageProcessing{
    private double divisor = 1.0 / 4.0;
    private double[][] horizontalMask = {
            {-divisor, 0, divisor},
            {0, 0, 0},
            {divisor, 0, -divisor}
    };

    public HorizontalEdges() {
        super();
        indentifier = "ImagesTests/TESTS-HORIZONTAL EDGE DETECTION";
    }

    public void computeHorizontalEdgeDetectionMasks(){
        List<BufferedImage> copyOfWorkingImages = new ArrayList<>();
        copyOfWorkingImages.addAll(workingImages);
        for (int i = 0; i < copyOfWorkingImages.size(); i++) {
            BufferedImage b = copyOfWorkingImages.get(i);
            for (int x = 1; x < b.getWidth() - 1; x++) {
                for (int y = 1; y < b.getHeight() - 1; y++) {
                    double[] colorConvoluted = new double[b.getColorModel().getPixelSize()/8];
                    for(int maskX = 0; maskX < horizontalMask.length; maskX++) {
                        for(int maskY = 0; maskY < horizontalMask[maskX].length; maskY++) {
                            BufferedImage orig = copyOfWorkingImages.get(i);
                            double[] color = new double[orig.getColorModel().getPixelSize()/8];
                            color = orig.getRaster().getPixel(x-1+maskX, y-1+maskY, color);
                            for (int k = 0; k < colorConvoluted.length; k++){
                                colorConvoluted[k] += (color[k]*horizontalMask[maskX][maskY]);
                            }
                        }
                    }
                    for (int k = 0; k < colorConvoluted.length; k++) {
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
