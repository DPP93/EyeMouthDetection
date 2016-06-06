package pl.dp.poid.method.utils;

import java.util.Comparator;

/**
 * Created by dpp on 6/2/16.
 */
public class Point implements Comparator<Point> {
    private double x;
    private double y;

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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
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
    public String toString() {
        return x+" "+y;
    }

    public static boolean isSecondPointAbove(Point p1, Point p2){
        if(p1.getY() > p2.getY()){
            return true;
        }
        return false;
    }

    public static boolean isSecondPointBelow(Point p1, Point p2){
        if(p1.getY() < p2.getY()){
            return true;
        }
        return false;
    }

    public static boolean isSecondPointLeft(Point p1, Point p2){
        if(p1.getX() > p2.getX()){
            return true;
        }
        return false;
    }

    public static boolean isSecondPointRight(Point p1, Point p2){
        if(p1.getX() < p2.getX()){
            return true;
        }
        return false;
    }

    public static double computeDistanceBetweenPoints(Point p1, Point p2){
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }
}
