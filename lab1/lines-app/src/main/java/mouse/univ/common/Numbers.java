package mouse.univ.common;

import java.util.List;

/**
 * Number utilities
 */
public class Numbers {
    public static final double ACCURACY = 1e-8;
    public static final int BOX_SIZE = 122;
    public static boolean isZero(double number) {
        return Math.abs(number) <= ACCURACY;
    }
    public static boolean equals(double n1, double n2) {
        return isZero(n1 - n2);
    }
    public static boolean isOutOfRange(double number) {
        return Math.abs(number) > BOX_SIZE;
    }

    public static boolean allEquals(List<Double> numbers) {
        if (numbers.isEmpty()) {
            return true;
        }
        Double first = numbers.getFirst();
        return numbers.stream().allMatch(f -> equals(f, first));
    }
}
