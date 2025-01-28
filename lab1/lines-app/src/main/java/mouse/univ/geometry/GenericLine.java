package mouse.univ.geometry;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import mouse.univ.common.Numbers;

@Data
@Getter(AccessLevel.NONE)
public final class GenericLine {
    private final double a;
    private final double b;
    private final double c;

    private GenericLine(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double a() {
        return a;
    }

    public double b() {
        return b;
    }

    public double c() {
        return c;
    }
    public static GenericLine fromGenericEquation(double a, double b, double c) {
        if (Numbers.isZero(a) && Numbers.isZero(b)) {
            throw new InvalidLineException("Invalid line: both A and B arguments in line equation 'Ax + By + C = 0' are zero; Consider entering non-zero values for A and B.");
        }
        return new GenericLine(a, b, c);
    }

    public static GenericLine fromTwoPoints(Point point1, Point point2) {
        if (point1.isCloseTo(point2)) {
            throw new InvalidLineException("Invalid line: a line cannot be defined by two points, located at the same position; Consider entering different points to construct a line.");
        }
        double a = point2.y() - point1.y();
        double b = point1.x() - point2.x();
        double c = point1.y() * point2.x() - point1.x() * point2.y();
        return new GenericLine(a, b, c);
    }


    public static GenericLine fromTwoSegments(double xSegmentLength, double ySegmentLength) {
        if (Numbers.isZero(xSegmentLength) || Numbers.isZero(ySegmentLength)) {
            throw new InvalidLineException("Invalid line: a line cannot be defined by two segments, when at least one of the segments is zero; Consider entering non-zero values.");
        }
        double a = 1.0/xSegmentLength;
        double b = 1.0/ySegmentLength;
        double c = -1.0;
        return new GenericLine(a, b, c);
    }

}
