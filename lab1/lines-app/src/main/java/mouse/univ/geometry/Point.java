package mouse.univ.geometry;

import mouse.univ.common.Numbers;

public record Point(double x, double y) {
    public boolean isCloseTo(Point other) {
        return Numbers.equals(x, other.x) && Numbers.equals(y, other.y);
    }

    @Override
    public String toString() {
        return String.format("(%.8f, %.8f)", x, y);
    }
}
