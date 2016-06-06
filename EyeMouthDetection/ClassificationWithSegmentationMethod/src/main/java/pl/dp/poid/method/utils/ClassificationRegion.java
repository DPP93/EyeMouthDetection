package pl.dp.poid.method.utils;

/**
 * Created by Daniel on 2016-06-03.
 */
public class ClassificationRegion {

    private Point[] region;
    private static double learningParameter = 0.3;
    private static double neighbourhoodRadius = 0.1;
    public ClassificationRegion(int sizeOfRegion, double leftBoundaryStarting, double rightBoundaryStarting,
                                double topBoundaryStarting, double bottomBoundaryStarting) {

        region = new Point[sizeOfRegion];

        for (int i = 0; i < region.length; i++){
            region[i] = new Point(RandomUtils.getRandomDoubleFromBoundaries(leftBoundaryStarting,rightBoundaryStarting),
                    RandomUtils.getRandomDoubleFromBoundaries(topBoundaryStarting,bottomBoundaryStarting));
        }
        System.out.println(this.toString());
    }

    public Point[] getRegion() {
        return region;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Point p : region){
            sb.append("R "+p.getX() + " " + p.getY() + "\n");
        }
        return sb.toString();
    }

    /**
     *
     * @param region
     * @param p NORMALIZED to [0,1] point
     * @param howMuch percentage of distance between winner and learning point
     */
    public static void modifyClassificationRegion(ClassificationRegion region, Point p, double howMuch){

        //In case where is this point we change region, winner is learning most, escond is learning in half od winner,
        // third is learning by fourth, and so forth

        int closestPointIndex = 0;
        double minDistance = Point.computeDistanceBetweenPoints(region.getRegion()[closestPointIndex], p);
        double dist;
        for (int i = 1; i < region.getRegion().length; i++) {
            dist = Point.computeDistanceBetweenPoints(region.getRegion()[i], p);
            if (dist < minDistance){
                minDistance = dist;
                closestPointIndex = i;
            }
        }

        for (int i = 0; i < region.getRegion().length; i++) {

            if (i == closestPointIndex){
                region.getRegion()[i] = modifyVictorousPoint(region.getRegion()[i], p, learningParameter);
            }

        }
    }

    private static Point modifyVictorousPoint(Point pointToModify, Point learningPoint, double alpha){
        pointToModify.setX(pointToModify.getX() + alpha * learningPoint.getX() * (learningPoint.getX() - pointToModify.getX()));
        pointToModify.setY(pointToModify.getY() + alpha * learningPoint.getY() * (learningPoint.getY() - pointToModify.getY()));
        return pointToModify;
    }
}
