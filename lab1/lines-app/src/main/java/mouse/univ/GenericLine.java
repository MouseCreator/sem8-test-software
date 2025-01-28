package mouse.univ;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

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

    public static GenericLine of(double a, double b, double c) {
        if (Numbers.isZero(a) && Numbers.isZero(b)) {
            throw new InvalidLineException("Invalid line: both A and B arguments in line equation 'Ax + By + C = 0' are zero; Consider entering non-zero values for A and B.");
        }
        return new GenericLine(a, b, c);
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

}
