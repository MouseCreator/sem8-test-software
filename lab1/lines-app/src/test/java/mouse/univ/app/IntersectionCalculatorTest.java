package mouse.univ.app;

import mouse.univ.common.Messages;
import mouse.univ.geometry.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class IntersectionCalculatorTest {

    private IntersectionCalculator intersectionCalculator;
    private AutomaticUserIO userInput;
    @BeforeEach
    void setUp() {
        userInput = new PreparedAutomaticUserIO();
        intersectionCalculator = new IntersectionCalculator(userInput);
    }

    @ParameterizedTest
    @DisplayName("OK: Matching lines")
    @CsvFileSource
    void calculate_sameLine(List<String> inputs) {
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(Messages.sameLine(), result);
    }

    @ParameterizedTest
    @DisplayName("OK: Parallel lines")
    @CsvFileSource
    void calculate_parallelLines(List<String> inputs) {
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(Messages.parallel(), result);
    }

    @ParameterizedTest
    @DisplayName("OK: One intersection")
    @CsvFileSource
    void calculate_oneIntersection(List<String> inputs) {
        List<Point> expected = getExpectedPoints(inputs, 1);
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertSamePoints(expected, result);
    }
    private static void assertSamePoints(List<Point> expected, String result) {
    }

    @ParameterizedTest
    @DisplayName("OK: Two intersections")
    @CsvFileSource
    void calculate_twoIntersections(List<String> inputs) {
        List<Point> expected = getExpectedPoints(inputs, 2);
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertSamePoints(expected, result);
    }

    @ParameterizedTest
    @DisplayName("OK: Three intersections")
    @CsvFileSource
    void calculate_threeIntersections(List<String> inputs) {
        List<Point> expected = getExpectedPoints(inputs, 3);
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertSamePoints(expected, result);
    }

    @ParameterizedTest
    @DisplayName("OK: Terminated")
    @CsvFileSource
    void calculate_terminated(List<String> inputs) {
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(Messages.terminated(), result);
    }

    @ParameterizedTest
    @DisplayName("ERROR: Missing input")
    @CsvFileSource
    void calculate_missingInput(List<String> inputs) {

    }

    @ParameterizedTest
    @DisplayName("ERROR: Input is not a number")
    @CsvFileSource
    void calculate_invalidInput(List<String> inputs) {
    }

    @ParameterizedTest
    @DisplayName("ERROR: Input is out of bounds")
    @CsvFileSource
    void calculate_outOfBounds(List<String> inputs) {
    }

    @ParameterizedTest
    @DisplayName("ERROR: Line 1 does not exist")
    @CsvFileSource
    void calculate_line1Invalid(List<String> inputs) {
    }

    @ParameterizedTest
    @DisplayName("ERROR: Line 2 does not exist")
    @CsvFileSource
    void calculate_line2Invalid(List<String> inputs) {
    }

    @ParameterizedTest
    @DisplayName("ERROR: Line 3 matches Line 2")
    @CsvFileSource
    void calculate_line3MatchesLine2(List<String> inputs) {
    }

    private List<Point> getExpectedPoints(List<String> inputs, int numberOfPoints) {
        List<Point> result = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            String x = inputs.removeFirst();
            String y = inputs.removeFirst();
            Point p = new Point(Integer.parseInt(x), Integer.parseInt(y));
            result.add(p);
        }
        return result;
    }
}