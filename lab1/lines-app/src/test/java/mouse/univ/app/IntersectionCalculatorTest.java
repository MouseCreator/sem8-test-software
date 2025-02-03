package mouse.univ.app;

import mouse.univ.common.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class IntersectionCalculatorTest {
    private IntersectionCalculator intersectionCalculator;
    private AutomaticUserIO userInput;
    @BeforeEach
    void setUp() {
        userInput = new PreparedAutomaticUserIO();
        intersectionCalculator = new IntersectionCalculator(userInput);
    }
    /**
     * @param inputs - list of input parameters: [X1;Y1;X2;Y2;A1;B1;A2;B2]
     */
    @ParameterizedTest
    @DisplayName("OK: Parallel lines")
    @CsvFileSource(resources = "01_parallel_lines.csv", delimiter = ';')
    void calculate_parallelLines(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals("Прямі не перетинаються", result);
    }
    /**
     ** @param inputs - list of input parameters [S;X1;Y1;X2;Y2;A1;B1;A2;B2], where S - example of a correct result message
     *
     */
    @ParameterizedTest
    @DisplayName("OK: One intersection")
    @CsvFileSource(resources = "02_one_intersection.csv", delimiter = ';')
    void calculate_oneIntersection(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        String expected = inputs.removeFirst();
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertIntersectionMessagesEqual(expected, result);
    }
    /**
     * @param inputs - list of input parameters [S;X1;Y1;X2;Y2;A1;B1;A2;B2], where S - example of a correct result message
     */
    @ParameterizedTest
    @DisplayName("OK: Two intersections")
    @CsvFileSource(resources = "03_two_intersections.csv", delimiter = ';')
    void calculate_twoIntersections(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        String expected = inputs.removeFirst();
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertIntersectionMessagesEqual(expected, result);
    }
    /**
     * @param inputs - list of input parameters [S;X1;Y1;X2;Y2;A1;B1;A2;B2], where S - example of a correct result message
     */
    @ParameterizedTest
    @DisplayName("OK: Three intersections")
    @CsvFileSource(resources = "04_three_intersections.csv", delimiter = ';')
    void calculate_threeIntersections(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        String expected = inputs.removeFirst();
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertIntersectionMessagesEqual(expected, result);
    }
    /**
     * @param inputs - incomplete list of input parameters [X1;Y1;X2;Y2;A1;B1;A2;B2], which ends with 'e' entry
     * For example, [0;0;1;1;e] - terminates the application, when asked for A1.
     */
    @ParameterizedTest
    @DisplayName("OK: Terminated")
    @CsvFileSource(resources = "05_terminated.csv", delimiter = ';')
    void calculate_terminated(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(Messages.terminated(), result);
        assertEquals("Зупинення...\n", userInput.getLastOutput());
    }
    /**
     * @param inputs - incomplete list of input parameters [X1;Y1;X2;Y2;A1;B1;A2;B2], which ends with 'r' entry
     * For example, [0;0;1;1;r] - restarts the application, when asked for A1.
     */
    @ParameterizedTest
    @DisplayName("OK: Restarted")
    @CsvFileSource(resources = "06_restarted.csv", delimiter = ';')
    void calculate_restarted(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        runPartialTest(inputs);
        String result = userInput.getLastOutput();
        assertEquals("Відновлення...\nВизначіть ПРЯМУ 1 за двома точками (X1; Y1), (X2; Y2)\n", result);
    }
    /**
     * @param inputs - incomplete list of input parameters [X1;Y1;X2;Y2;A1;B1;A2;B2], which ends with '' (empty string)
     * For example, [0;1;2;] has empty value for parameter Y2
     */
    @ParameterizedTest
    @DisplayName("ERROR: Missing input")
    @CsvFileSource(resources = "07_missing_input.csv", delimiter = ';')
    void calculate_missingInput(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        runPartialTest(inputs);
        assertEquals("Відсутнє вхідне значення; Введіть ціле десяткове число з проміжку [-122; 122], будь ласка.\n", userInput.getLastOutput());
    }
    /**
     * @param inputs - incomplete list of input parameters [X1;Y1;X2;Y2;A1;B1;A2;B2], which ends with a non-integer entry
     * For example, [0;1;2;3;4;A] ends with B1='A', which is not a number
     */
    @ParameterizedTest
    @DisplayName("ERROR: Input is not a number")
    @CsvFileSource(resources = "08_input_not_number.csv", delimiter = ';')
    void calculate_invalidInput(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        runPartialTest(inputs);
        assertEquals("Вхідне значення не є цілим числом; Введіть ціле десяткове число з проміжку [-122; 122], будь ласка.\n", userInput.getLastOutput());
    }
    /**
     * @param inputs - incomplete list of input parameters [X1;Y1;X2;Y2;A1;B1;A2;B2], which ends with a number out of bounds of the given box
     * For example, [0;1;2;3;4;1000] ends with B1 = 1000 > 122 (the upper bound)
     */
    @ParameterizedTest
    @DisplayName("ERROR: Input is out of bounds")
    @CsvFileSource(resources = "09_input_out_of_bounds.csv", delimiter = ';')
    void calculate_outOfBounds(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        runPartialTest(inputs);
        assertEquals("Дане вхідне число не входить до проміжку [-122, 122]; Введіть ціле десяткове число з проміжку [-122; 122], будь ласка.\n", userInput.getLastOutput());
    }
    /**
     * @param inputs - list of input parameters [X1;Y1;X2;Y2], where X1=X2 and Y1=Y2;
     * For example, [0;1;0;1] defines two points at the same coordinates of (0, 1). The pair does not define a line.
     */
    @ParameterizedTest
    @DisplayName("ERROR: Line 1 does not exist")
    @CsvFileSource(resources = "10_line1_err.csv", delimiter = ';')
    void calculate_line1Invalid(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        runPartialTest(inputs);
        String expectedMessage =
                "Некоректно задана пряма: пряма не може бути задана двома співпадаючими точками; Спробуйте ввести дві різні точки, щоб побудувати пряму.\n" +
                "Введіть параметри прямої ще раз, будь ласка.\n";
        assertEquals(expectedMessage, userInput.getLastOutput());
    }
    /**
     * @param inputs - list of input parameters [X1;Y1;X2;Y2;A1;B1], where A1=0 and B1=0;
     * For example, [0;1;2;3;0;0] - defines zero segments, which is just a point at the origin. The pair does not define a line
     */
    @ParameterizedTest
    @DisplayName("ERROR: Line 2 does not exist: A = 0 and B = 0")
    @CsvFileSource(resources = "11_line2_err_ab.csv", delimiter = ';')
    void calculate_line2Invalid_bothZero(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        runPartialTest(inputs);
        String expectedMessage = "Некоректно задана пряма: пряма не може бути задана двома нулевими відрізками; Надайте ненулеві значення параметрам A та B.\n";
        expectedMessage += "Введіть параметри прямої ще раз, будь ласка.\n";
        assertEquals(expectedMessage, userInput.getLastOutput());
    }
    /**
     * @param inputs - list of input parameters [X1;Y1;X2;Y2;A1;B1], where A1 = 0 and B1 != 0;
     * For example, [0;1;2;3;0;2] - violates the requirement A1 = 0, so the corresponding message must be printed
     */
    @ParameterizedTest
    @DisplayName("ERROR: Line 2 does not exist: A = 0")
    @CsvFileSource(resources = "12_line2_err_a.csv", delimiter = ';')
    void calculate_line2Invalid_AZero(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        runPartialTest(inputs);
        String expectedMessage = "Некоректно задана пряма: пряма не може відтинати нулевий відрізок на осі OX; Спробуйте ввести ненулеве значення параметра A.\n";
        expectedMessage += "Введіть параметри прямої ще раз, будь ласка.\n";
        assertEquals(expectedMessage, userInput.getLastOutput());
    }
    /**
     * @param inputs - list of input parameters [X1;Y1;X2;Y2;A1;B1], where A1 != 0 and B1 = 0;
     * For example, [0;1;2;3;2;0] - violates the requirement B1 = 0, so the corresponding message must be printed
     */
    @ParameterizedTest
    @DisplayName("ERROR: Line 2 does not exist: B = 0")
    @CsvFileSource(resources = "13_line2_err_b.csv", delimiter = ';')
    void calculate_line2Invalid_BZero(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        runPartialTest(inputs);
        String expectedMessage = "Некоректно задана пряма: пряма не може відтинати нулевий відрізок на осі OY; Спробуйте ввести ненулеве значення параметра B.\n";
        expectedMessage += "Введіть параметри прямої ще раз, будь ласка.\n";
        assertEquals(expectedMessage, userInput.getLastOutput());
    }
    /**
     * @param inputs - list of input parameters [X1;Y1;X2;Y2;A1;B1;A2;B2], where A1=A2 and B1=B2;
     * For example, [0;1;2;3;1;2;1;2] creates two matching lines, violating the condition that (A1; B1) does not equal to (A2; B2)
     */
    @ParameterizedTest
    @DisplayName("ERROR: Line 3 matches Line 2")
    @CsvFileSource(resources = "14_line2_3_match.csv", delimiter = ';')
    void calculate_line3MatchesLine2(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        runPartialTest(inputs);
        String expectedMessage =
                "ПРЯМА 3 не може співпадати з ПРЯМОЮ 2.\n" +
                "Введіть параметри прямої ще раз, будь ласка.\n";
        assertEquals(expectedMessage, userInput.getLastOutput());
        runPartialTest(inputs);
    }

    /**
     * Runs the test; Allows aborting the test due to insufficient input values
     * @param inputs - input values for the test
     */
    private void runPartialTest(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        userInput.supply(inputs);
        try {
            intersectionCalculator.calculate();
        } catch (OutOfInputsException ignored) {
            /*
             * The exception is thrown and caught in order to skip the calculations performed by IntersectionCalculator
             * This way, the tests can quickly access the state of the userInput
             */
        }
    }

    /**
     * Checks if two intersection messages are equals regardless of the order of the points in the message
     * @param expected - example of the correct message
     * @param actual - actual message, the result of the program
     */
    private static void assertIntersectionMessagesEqual(String expected, String actual) {
        /*
        If example message and actual message match - the assertion passes
         */
        if (expected.equals(actual)) {
            return;
        }
        String errorMessage = String.format("Message mismatch!\nExpected : %s\nActual   : %s", expected, actual);
        /*
        Extract points from both messages and compare them
        The points must be the same
         */
        Set<String> expectedPoints = extractPoints(expected);
        Set<String> actualPoints = extractPoints(actual);
        assertEquals(expectedPoints, actualPoints, errorMessage);

        /*
        Replace points with placeholder and compare messages.
        Since the sensitive part is already checked and replaced with a placeholder,
        the test only needs to compare the structure of the messages
         */
        String expectedPlaceholder = toPlaceholderMessage(expected, expectedPoints);
        String actualPlaceholder = toPlaceholderMessage(actual, actualPoints);
        assertEquals(expectedPlaceholder, actualPlaceholder, errorMessage);
    }

    /**
     * Replaces point coordinates with a placeholder ("%%%%%%")
     * @param message - input message
     * @param points - substrings to replace with the placeholder
     * @return string with placeholders
     */
    private static String toPlaceholderMessage(String message, Set<String> points) {
        String result = message;
        for (String point : points) {
            result = result.replaceAll("\\(" + point + "\\)", "%%%%%");
        }
        return result;
    }

    /**
     * Extracts point coordinates from the message
     * @param message - input message
     * @return a set of substrings, containing point coordinates
     */
    private static Set<String> extractPoints(String message) {
        Pattern pattern = Pattern.compile("\\(([+-]?\\d+(?:\\.\\d+)?),\\s([+-]?\\d+(?:\\.\\d+)?)\\)");
        Set<String> matches = new HashSet<>();
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }
}