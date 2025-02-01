package mouse.univ.common;

import mouse.univ.geometry.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Messages {
    public static String sameLine() {
        return "Given lines are the same line";
    }
    public static String parallel() {
        return "Given lines do not intersect";
    }
    public static String intersections(List<Point> pointList) {
        List<Point> sortedList = softPoints(pointList);
        if (sortedList.isEmpty()) {
            throw new IllegalArgumentException("Assumed that given lines intersect, but received empty list of intersections.");
        }
        if (sortedList.size() == 1) {
            return "Lines intersect at one point: " + (sortedList.getFirst());
        }
        if (sortedList.size() == 2) {
            return "Lines intersect at two points: " + formatListOfPoints(sortedList);
        }
        if (sortedList.size() == 3) {
            return "Lines intersect at three points: " + formatListOfPoints(sortedList);
        }
        return "Lines intersect at " + sortedList.size() + " points " + formatListOfPoints(sortedList);
    }

    private static List<Point> softPoints(List<Point> pointList) {
        List<Point> copyList = new ArrayList<>(pointList);
        Comparator<Point> comparator = (p1, p2) -> {
            if (Numbers.equals(p1.x(), p2.x())) {
                if (Numbers.equals(p1.y(), p2.y())) {
                    return 0;
                }
                return p1.y() > p2.y() ? 1 : -1;
            }
            return p1.x() > p2.x() ? 1 : -1;
        };
        copyList.sort(comparator);
        return copyList;
    }

    private static String formatListOfPoints(List<Point> list) {
        if (list.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(list.getFirst().toString());
        for (int i = 1; i < list.size(); i++) {
            builder.append(", ");
            builder.append(list.get(i));
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
