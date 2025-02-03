package mouse.univ.geometry;

import mouse.univ.common.Numbers;

/**
 * A point in a 2D space
 * @param x - x coordinate of the point
 * @param y - y coordinate of the point
 */
public record Point(double x, double y) {
    public boolean isCloseTo(Point other) {
        return Numbers.equals(x, other.x) && Numbers.equals(y, other.y);
    }
}
