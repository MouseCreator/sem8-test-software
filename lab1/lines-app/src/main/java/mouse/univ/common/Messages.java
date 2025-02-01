package mouse.univ.common;

import mouse.univ.geometry.Point;

import java.util.List;

public class Messages {
    public static String sameLine() {
        return "Given lines are the same line";
    }
    public static String parallel() {
        return "Given lines do not intersect";
    }
    public static String intersections(List<Point> pointList) {
        if (pointList.isEmpty()) {
            throw new IllegalArgumentException("Assumed that given lines intersect, but received empty list of intersections.");
        }
        if (pointList.size() == 1) {
            return "Lines intersect at one point: " + formatPoint(pointList.getFirst());
        }
        if (pointList.size() == 2) {
            return "Lines intersect at two points: " + formatListOfPoints(pointList);
        }
        if (pointList.size() == 3) {
            return "Lines intersect at three points: " + formatListOfPoints(pointList);
        }
        return "Lines intersect at " + pointList.size() + " points " + formatListOfPoints(pointList);
    }

    private static String formatPoint(Point point) {
        return String.format("(%.8f, %.8f)", point.x(), point.y());
    }
    private static String formatListOfPoints(List<Point> list) {
        if (list.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(formatPoint(list.getFirst()));
        for (int i = 1; i < list.size(); i++) {
            builder.append(", ");
            builder.append(formatPoint(list.get(i)));
        }
        return builder.toString();
    }


    public static String terminated() {
        return "Terminated";
    }

    public static String restarted() {
        return "Restarted";
    }
}
