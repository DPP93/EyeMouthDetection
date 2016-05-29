package image.processing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2016-05-29.
 */
public class HorizontalEdges extends ImageProcessing{
    private double divisor = (1.0 / 1.25)*0.9;
    private double[][] horizontalMask = {
            {-1, 0, 1},
            {0, 1, 0},
            {1, 0, -1}
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
                            BufferedImage orig = workingImages.get(i);
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

//    public void checking(){
//        for (int x = 1; x < imageWidth - 1; x++) {
//            for (int y = 1; y < imageHeight - 1; y++) {
//
//                double valueR = 0, valueG = 0, valueB = 0;
//                for (int i = 0; i < mask.length; i++) {
//                    for (int j = 0; j < mask[i].length; j++) {
//                        //Tutaj ogarniam wartości dla poszczegolnych przesunięć w splocie
//                        int offsetX = 0, offsetY = 0;
//                        if (i == 0) {
//                            offsetX = -1;
//                        } else if (i == 1) {
//                            offsetX = 1;
//                        }
//                        if (j == 0) {
//                            offsetY = -1;
//                        } else if (j == 1) {
//                            offsetY = 1;
//                        }
//
//                        //Tutaj właściwe obliczenie
//                        Color c = new Color(copiedImage.getRGB(x + offsetX, y + offsetY));
//                        valueR += mask[i][j] * c.getRed();
//                        valueG += mask[i][j] * c.getGreen();
//                        valueB += mask[i][j] * c.getBlue();
//                    }
//                }
//
//                Color c = new Color(normalizeColor((int) valueR), normalizeColor((int) valueG), normalizeColor((int) valueB));
//                newValues[x][y] = c.getRGB();
//            }
//        }
//    }

}
