package method.utils;

import java.util.Comparator;

/**
 * Created by dpp on 6/2/16.
 */
public class Point implements Comparator<Point> {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
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

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
