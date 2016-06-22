package pl.oi.annotations;

import java.awt.Dimension;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * This class has two collections of elements with encapsulated data
 * Created by dpp on 5/4/16.
 */
public class Annotations {

    private List<Face> testElements;
    private List<Face> resultElements;
    private List<Dimension> imageDimensions;

    public void setupTestElements(File testElementsFile) throws IOException {
        testElements = new LinkedList<>();
        BufferedReader br = new BufferedReader(new FileReader(testElementsFile));
        List<String> list = br.lines().collect(Collectors.toList());
//        list.remove(list.size() - 1);
        for (String s : list) {
            parseTestFileLine(s);
        }
        br.close();
    }

    public void setupTestElements(BufferedReader testElementsFile) throws IOException {
        testElements = new ArrayList<>();
        BufferedReader br = testElementsFile;
        List<String> list = br.lines().collect(Collectors.toList());
//        list.remove(list.size() - 1);
        for (String s : list) {
            parseTestFileLine(s);
        }
        br.close();
    }

    public void setResultElements(File resultElementsFile) throws IOException {
        resultElements = new ArrayList<>();
        imageDimensions = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(resultElementsFile));
        List<String> list = br.lines().collect(Collectors.toList());
//        list.remove(list.size() - 1);
        for (String s : list) {
            parseResultFileLine(s);
        }
        br.close();
    }

    public List<Face> getTestElements() {
        return testElements;
    }

    public List<Face> getResultElements() {
        return resultElements;
    }

    public List<Dimension> getImageDimensions() {
        return imageDimensions;
    }
    
    private void parseTestFileLine(String s){
        StringTokenizer st = new StringTokenizer(s, " ");
        String[] tab = new String[15];
        for(int i=0; i<tab.length; i++){
            if(st.hasMoreTokens()){
                tab[i] = st.nextToken();
            }else{
                break;
            }

        }
        if(tab[0] != null){
            String imageName = tab[0];
            FacePoint annotateForLeftEye = new FacePoint(Double.parseDouble(tab[1]), Double.parseDouble(tab[6]));
            FacePoint annotateForRightEye = new FacePoint(Double.parseDouble(tab[2]), Double.parseDouble(tab[7]));

            FacePoint annotateForLeftMouthCorner = new FacePoint(Double.parseDouble(tab[4]), Double.parseDouble(tab[9]));
            FacePoint annotateForRightMouthCorner = new FacePoint(Double.parseDouble(tab[5]), Double.parseDouble(tab[10]));

            testElements.add(new Face(imageName, annotateForLeftEye, annotateForRightEye, annotateForLeftMouthCorner, annotateForRightMouthCorner));

        }
    }

    private void parseResultFileLine(String s){
        StringTokenizer st = new StringTokenizer(s, " ");
        String[] tab = new String[11];
        for(int i=0; i<tab.length; i++){
            if(st.hasMoreTokens()){
                tab[i] = st.nextToken();
//                System.out.println(i+" "+tab[i]);
            }else{
                break;
            }

        }
        if(tab[0] != null){
            String imageName = tab[0];
            FacePoint annotateForLeftEye = new FacePoint(Double.parseDouble(tab[1]), Double.parseDouble(tab[2]));
            FacePoint annotateForRightEye = new FacePoint(Double.parseDouble(tab[3]), Double.parseDouble(tab[4]));

            FacePoint annotateForLeftMouthCorner = new FacePoint(Double.parseDouble(tab[5]), Double.parseDouble(tab[6]));
            FacePoint annotateForRightMouthCorner = new FacePoint(Double.parseDouble(tab[7]), Double.parseDouble(tab[8]));

            resultElements.add(new Face(imageName, annotateForLeftEye, annotateForRightEye, annotateForLeftMouthCorner, annotateForRightMouthCorner));
            
            imageDimensions.add(new Dimension(Integer.parseInt(tab[9]), Integer.parseInt(tab[10])));
        }
    }

    public static Face findFace(List<Face> faces, String faceName){
        for(Face f : faces){
            if(f.getPictureName().equals(faceName)){
//                System.out.println(faceName +" "+f.getPictureName());
                return f;
            }
        }
        return faces.get(0);
    }
}
