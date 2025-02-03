package mouse.univ.geometry;

import mouse.univ.common.Numbers;

import java.util.Locale;

public record Point(double x, double y) {
    public boolean isCloseTo(Point other) {
        return Numbers.equals(x, other.x) && Numbers.equals(y, other.y);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "(%.8f, %.8f)", x, y);
    }
}
