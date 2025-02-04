package mouse.univ.geometry;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import mouse.univ.common.Numbers;

/**
 * Пряма у загальному вигляді
 */
@Data
@Getter(AccessLevel.NONE)
public final class GenericLine {
    private final double a;
    private final double b;
    private final double c;

    /**
     * Побудова прямої у загальному вигляді A*x + B*y + C = 0
     * @param a - x argument
     * @param b - y argument
     * @param c - constant
     * @throws InvalidLineException - impossible to construct a line
     */
    private GenericLine(double a, double b, double c) throws InvalidLineException {
        if (Numbers.isZero(a) && Numbers.isZero(b)) {
            throw new InvalidLineException("Некоректно задана пряма: аргументи A and B у загальному рівнянні прямої 'Ax + By + C = 0' дорівнюють нулю; Спробуйте ввести ненулеві значення для A та B.");
        }
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

    /**
     * Constructs a line by two points
     */
    public static GenericLine fromTwoPoints(Point point1, Point point2) throws InvalidLineException {
        if (point1.isCloseTo(point2)) {
            throw new InvalidLineException("Некоректно задана пряма: пряма не може бути задана двома співпадаючими точками; Спробуйте ввести дві різні точки, щоб побудувати пряму.");
        }
        double a = point2.y() - point1.y();
        double b = point1.x() - point2.x();
        double c = point1.y() * point2.x() - point1.x() * point2.y();
        return new GenericLine(a, b, c);
    }


    /**
     * Constructs a line by two segments
     */
    public static GenericLine fromTwoSegments(double xSegmentLength, double ySegmentLength) throws InvalidLineException {
        if (Numbers.isZero(xSegmentLength) && Numbers.isZero(ySegmentLength)) {
            throw new InvalidLineException("Некоректно задана пряма: пряма не може бути задана двома нулевими відрізками; Надайте ненулеві значення параметрам A та B.");
        }
        if (Numbers.isZero(xSegmentLength)) {
            throw new InvalidLineException("Некоректно задана пряма: пряма не може відтинати нулевий відрізок на осі OX; Спробуйте ввести ненулеве значення параметра A.");
        }
        if (Numbers.isZero(ySegmentLength)) {
            throw new InvalidLineException("Некоректно задана пряма: пряма не може відтинати нулевий відрізок на осі OY; Спробуйте ввести ненулеве значення параметра B.");
        }
        double a = ySegmentLength;
        double b = xSegmentLength;
        double c = -ySegmentLength * xSegmentLength;
        return new GenericLine(a, b, c);
    }
}
