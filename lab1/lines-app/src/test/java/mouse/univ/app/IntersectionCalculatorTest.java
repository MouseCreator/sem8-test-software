package mouse.univ.app;

import mouse.univ.common.Messages;
import mouse.univ.geometry.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
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
    @CsvFileSource(resources = "01_matching_lines.csv", delimiter = ';')
    void calculate_sameLine(@AggregateWith(StringListAggregator.class) List<String> inputs) {
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
        String expected = inputs.removeFirst();
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @DisplayName("OK: Two intersections")
    @CsvFileSource
    void calculate_twoIntersections(List<String> inputs) {
        String expected = inputs.removeFirst();
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @DisplayName("OK: Three intersections")
    @CsvFileSource
    void calculate_threeIntersections(List<String> inputs) {
        String expected = inputs.removeFirst();
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @DisplayName("OK: Terminated")
    @CsvFileSource
    void calculate_terminated(List<String> inputs) {
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(Messages.terminated(), result);
        assertEquals("Exiting...\n", userInput.getLastOutput());
    }

    @ParameterizedTest
    @DisplayName("OK: Restarted")
    @CsvFileSource
    void calculate_restarted(List<String> inputs) {
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(Messages.restarted(), result);
        assertEquals("Restarting...\nDefine LINE 1 by two points (X1; Y1), (X2; Y2)\n", userInput.getLastOutput());
    }

    @ParameterizedTest
    @DisplayName("ERROR: Missing input")
    @CsvFileSource
    void calculate_missingInput(List<String> inputs) {
        runPartialTest(inputs);
        assertEquals("No input; Please, enter a valid decimal integer number.\n", userInput.getLastOutput());
    }

    @ParameterizedTest
    @DisplayName("ERROR: Input is not a number")
    @CsvFileSource
    void calculate_invalidInput(List<String> inputs) {
        runPartialTest(inputs);
        assertEquals("No input; Please, enter a valid decimal integer number.\n", userInput.getLastOutput());
    }

    @ParameterizedTest
    @DisplayName("ERROR: Input is out of bounds")
    @CsvFileSource
    void calculate_outOfBounds(List<String> inputs) {
        runPartialTest(inputs);
        assertEquals("Provided integer is out of bounds of the allowed box: [-122, 122]\n", userInput.getLastOutput());
    }

    @ParameterizedTest
    @DisplayName("ERROR: Line 1 does not exist")
    @CsvFileSource
    void calculate_line1Invalid(List<String> inputs) {
        runPartialTest(inputs);
        String expectedMessage =
                "Invalid line: a line cannot be defined by two points, located at the same position; Consider entering different points to construct a line.\n" +
                "Enter the line arguments again, please!\n";
        assertEquals(expectedMessage, userInput.getLastOutput());
    }

    @ParameterizedTest
    @DisplayName("ERROR: Line 2 does not exist")
    @CsvFileSource
    void calculate_line2Invalid(List<String> inputs) {
        runPartialTest(inputs);
        String expectedMessage =
                "Invalid line: a line cannot be defined by two segments, when at least one of the segments is zero; Consider entering non-zero values.\n" +
                "Enter the line arguments again, please!\n";
        assertEquals(expectedMessage, userInput.getLastOutput());
    }

    @ParameterizedTest
    @DisplayName("ERROR: Line 3 matches Line 2")
    @CsvFileSource
    void calculate_line3MatchesLine2(List<String> inputs) {
        String expectedMessage =
                "LINE 3 cannot match LINE 2.\n" +
                        "Enter the line arguments again, please!\n";
        assertEquals(expectedMessage, userInput.getLastOutput());
        runPartialTest(inputs);
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

    private void runPartialTest(List<String> inputs) {
        userInput.supply(inputs);
        try {
            intersectionCalculator.calculate();
        } catch (OutOfInputsException ignored) {
            /*
             * Exception is thrown to avoid extra calculations by IntersectionCalculator
             * Used to quickly terminate the session and access userInput's state
             */
        }
    }
}