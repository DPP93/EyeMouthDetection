package method.utils;

/**
 * Created by Daniel on 2016-06-03.
 */
public class ClassificationRegion {

    private Point a;
    private Point b;
    private Point c;
    private Point d;

    public ClassificationRegion(Point a, Point b, Point c, Point d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public void setA(Point a) {
        this.a = a;
    }

    public void setB(Point b) {
        this.b = b;
    }

    public void setC(Point c) {
        this.c = c;
    }

    public void setD(Point d) {
        this.d = d;
    }

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }

    public Point getC() {
        return c;
    }

    public Point getD() {
        return d;
    }

    public static void modifyClassificationRegion(ClassificationRegion region, Point p, double howMuch){

        //W zaleźności od tego gdzie jest dany

    }
}
