package mouse.univ.geometry;

import mouse.univ.common.Numbers;

public record Point(double x, double y) {
    public boolean isCloseTo(Point other) {
        return Numbers.equals(x, other.x) && Numbers.equals(y, other.y);
    }
}
