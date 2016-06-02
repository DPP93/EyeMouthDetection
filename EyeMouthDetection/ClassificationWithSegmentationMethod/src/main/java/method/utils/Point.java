package method.utils;

import java.util.Comparator;

/**
 * Created by dpp on 6/2/16.
 */
public class Point implements Comparator<Point> {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    @Override
    public int compare(Point o1, Point o2) {
        if (o1.getX() == o2.getX() && o1.getY() == o2.getY()) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        Point p = (Point) o;
        if (x == p.getX() && y == p.getY()){
            return true;
        }
        return false;
    }


}
