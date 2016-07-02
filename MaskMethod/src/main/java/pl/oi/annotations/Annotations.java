package pl.oi.annotations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * This class has two collections of elements with encapsulated data
 * Created by dpp on 5/4/16.
 */
public class Annotations {

    private List<Face> elements;


    public void setupElements(File trainingFile) throws IOException {

        elements = new LinkedList<>();
        BufferedReader br = new BufferedReader(new FileReader(trainingFile));
        List<String> list = br.lines().collect(Collectors.toList());
//        list.remove(list.size() - 1);
        for (String s : list) {
            parseFileLine(s);
        }
        br.close();
    }

    public void setupElements(BufferedReader b) throws IOException {

        elements = new LinkedList<>();
        BufferedReader br = b;
        List<String> list = br.lines().collect(Collectors.toList());
//        list.remove(list.size() - 1);
        for (String s : list) {
            parseFileLine(s);
        }
        br.close();
    }


    private void parseFileLine(String line) {

        StringTokenizer st = new StringTokenizer(line, " ");
        String[] tab = new String[15];
        for (int i = 0; i < tab.length; i++) {
            if (st.hasMoreTokens()) {
                tab[i] = st.nextToken();
//                System.out.println(i+" "+tab[i]);
            } else {
                break;
            }

        }
        if (tab[0] != null) {


            String imageName = tab[0];
            FacePoint annotateForLeftEye = new FacePoint(Double.parseDouble(tab[1]), Double.parseDouble(tab[6]));
            FacePoint annotateForRightEye = new FacePoint(Double.parseDouble(tab[2]), Double.parseDouble(tab[7]));

            FacePoint annotateForLeftMouthCorner = new FacePoint(Double.parseDouble(tab[4]), Double.parseDouble(tab[9]));
            FacePoint annotateForRightMouthCorner = new FacePoint(Double.parseDouble(tab[5]), Double.parseDouble(tab[10]));

            elements.add(new Face(imageName, annotateForLeftEye, annotateForRightEye, annotateForLeftMouthCorner, annotateForRightMouthCorner));
        }

    }

    public List<Face> getElements() {
        return elements;
    }

    public Face getFace(String imageDatabaseName) {
        for (Face f : elements) {

            if (f.getPictureName().toLowerCase().contains(imageDatabaseName.toLowerCase())) {
//                System.out.println(f.getPictureName());
//                System.out.println(imageDatabaseName);
//                System.out.println("------------------------");
                return f;
            }
        }
//        System.out.println("Nie znalazłem puszczam pierwszy");
        return elements.get(0);
    }

}
