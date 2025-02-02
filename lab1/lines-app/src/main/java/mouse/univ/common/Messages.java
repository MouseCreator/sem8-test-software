package mouse.univ.common;

import mouse.univ.geometry.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Messages {
    public static String sameLine() {
        return "Прямі співпадають";
    }
    public static String parallel() {
        return "Прямі не перетинаються";
    }
    public static String intersections(List<Point> pointList) {
        List<Point> sortedList = softPoints(pointList);
        if (sortedList.isEmpty()) {
            throw new IllegalArgumentException("Припускається, що прямі перетинаються принаймні в одній точці, але отримано порожній список точок перетину");
        }
        if (sortedList.size() == 1) {
            return "Прямі перетинаються в одній точці " + sortedList.getFirst();
        }
        if (sortedList.size() == 2) {
            return "Прямі перетинаються в двох точках " + formatListOfPoints(sortedList);
        }
        if (sortedList.size() == 3) {
            return "Прямі перетинаються в трьох точках  " + formatListOfPoints(sortedList);
        }
        return "Прямі перетинацються в " + sortedList.size() + " точках " + formatListOfPoints(sortedList);
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
        return "Зупинено";
    }

    public static String restarted() {
        return "Перезапущено";
    }
}
