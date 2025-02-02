package mouse.univ.app;

import mouse.univ.common.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class IntersectionCalculatorTest {
    private IntersectionCalculator intersectionCalculator;
    private AutomaticUserIO userInput;
    @BeforeEach
    void setUp() {
        userInput = new PreparedAutomaticUserIO();
        intersectionCalculator = new IntersectionCalculator(userInput);
    }
    /**
     * @param inputs - Коректні вхідні дані: [X1;Y1;X2;Y2;A1;B1;A2;B2]
     */
    @ParameterizedTest
    @DisplayName("OK: Matching lines")
    @CsvFileSource(resources = "01_matching_lines.csv", delimiter = ';')
    void calculate_sameLine(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(Messages.sameLine(), result);
    }
    /**
     * @param inputs - Коректні вхідні дані: [X1;Y1;X2;Y2;A1;B1;A2;B2]
     */
    @ParameterizedTest
    @DisplayName("OK: Parallel lines")
    @CsvFileSource(resources = "01_matching_lines.csv", delimiter = ';')
    void calculate_parallelLines(@AggregateWith(StringListAggregator.class) List<String> inputs) {
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(Messages.parallel(), result);
    }
    /**
     * @param inputs - Коректні вхідні дані: [X1;Y1;X2;Y2;A1;B1;A2;B2]
     */
    @ParameterizedTest
    @DisplayName("OK: One intersection")
    @CsvFileSource
    void calculate_oneIntersection(List<String> inputs) {
        String expected = inputs.removeFirst();
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(expected, result);
    }
    /**
     * @param inputs - Коректні вхідні дані: [X1;Y1;X2;Y2;A1;B1;A2;B2]
     */
    @ParameterizedTest
    @DisplayName("OK: Two intersections")
    @CsvFileSource
    void calculate_twoIntersections(List<String> inputs) {
        String expected = inputs.removeFirst();
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(expected, result);
    }
    /**
     * @param inputs - Коректні вхідні дані: [X1;Y1;X2;Y2;A1;B1;A2;B2]
     */
    @ParameterizedTest
    @DisplayName("OK: Three intersections")
    @CsvFileSource
    void calculate_threeIntersections(List<String> inputs) {
        String expected = inputs.removeFirst();
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(expected, result);
    }
    /**
     * @param inputs - Коректні вхідні дані - неповний список вхідних параметрів [X1;Y1;X2;Y2;A1;B1;A2;B2], що закінчується вводом 'e'
     * Наприклад, [0;0;1;1;e] - зупинення виконання програми під час вводу A1.
     */
    @ParameterizedTest
    @DisplayName("OK: Terminated")
    @CsvFileSource
    void calculate_terminated(List<String> inputs) {
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(Messages.terminated(), result);
        assertEquals("Зупинення...\n", userInput.getLastOutput());
    }
    /**
     * @param inputs - Коректні вхідні дані - неповний список вхідних параметрів [X1;Y1;X2;Y2;A1;B1;A2;B2], що закінчується вводом 'r'
     * Наприклад, [0;0;1;1;r] - перезапуск виконання програми під час вводу A1.
     */
    @ParameterizedTest
    @DisplayName("OK: Restarted")
    @CsvFileSource
    void calculate_restarted(List<String> inputs) {
        userInput.supply(inputs);
        String result = intersectionCalculator.calculate();
        assertEquals(Messages.restarted(), result);
        assertEquals("Відновлення...\nВизначіть ПРЯМУ 1 за двома точками (X1; Y1), (X2; Y2)\n", userInput.getLastOutput());
    }
    /**
     * @param inputs - Вхідні дані - неповний список вхідних параметрів [X1;Y1;X2;Y2;A1;B1;A2;B2], що закінчується вводом '' (порожній рядок)
     * Наприклад, [0;1;2;] - не надано значення параметру Y2
     */
    @ParameterizedTest
    @DisplayName("ERROR: Missing input")
    @CsvFileSource
    void calculate_missingInput(List<String> inputs) {
        runPartialTest(inputs);
        assertEquals("Відсутнє вхідне значення; Введіть ціле десяткове число з проміжу [-122; 122], будь ласка.\n", userInput.getLastOutput());
    }
    /**
     * @param inputs - Вхідні дані - неповний список вхідних параметрів [X1;Y1;X2;Y2;A1;B1;A2;B2], що закінчується нечисловим вводом
     * Наприклад, [0;1;2;3;4;A] - замість параметру B1 введено значення 'A'
     */
    @ParameterizedTest
    @DisplayName("ERROR: Input is not a number")
    @CsvFileSource
    void calculate_invalidInput(List<String> inputs) {
        runPartialTest(inputs);
        assertEquals("Вхідне значення не є цілим числом; Введіть ціле десяткове число з проміжу [-122; 122], будь ласка.\n", userInput.getLastOutput());
    }
    /**
     * @param inputs - Вхідні дані - неповний список вхідних параметрів [X1;Y1;X2;Y2;A1;B1;A2;B2], що закінчується числом поза дозволеним проміжком
     * Наприклад, [0;1;2;3;4;1000] - замість параметру B1 введено значення 1000 > 122
     */
    @ParameterizedTest
    @DisplayName("ERROR: Input is out of bounds")
    @CsvFileSource
    void calculate_outOfBounds(List<String> inputs) {
        runPartialTest(inputs);
        assertEquals("Дане вхідне число не входить до проміжку [-122, 122]; Введіть ціле десяткове число з проміжу [-122; 122], будь ласка.\n", userInput.getLastOutput());
    }
    /**
     * @param inputs - Вхідні дані - список вхідних параметрів [X1;Y1;X2;Y2], де X1=X2 та Y1=Y2;
     * Наприклад, [0;1;0;1] - утворюють дві точки зі значення (0, 1). Така пара точок не задає прямої.
     */
    @ParameterizedTest
    @DisplayName("ERROR: Line 1 does not exist")
    @CsvFileSource
    void calculate_line1Invalid(List<String> inputs) {
        runPartialTest(inputs);
        String expectedMessage =
                "Некоректно задана пряма: пряма не може бути задана двома співпадаючими точками; Спробуйте ввести дві різні точки, щоб побудувати пряму.\n" +
                "Введіть параметри прямої ще раз, будь ласка.\n";
        assertEquals(expectedMessage, userInput.getLastOutput());
    }
    /**
     * @param inputs - Вхідні дані - список вхідних параметрів [X1;Y1;X2;Y2;A1;B1], де A1=0 та/або B1=0;
     * Наприклад, [0;1;2;3;0;0] - утворюють нульові відрізки на осях OX та OY. Така пара відрізків не задає прямої
     */
    @ParameterizedTest
    @DisplayName("ERROR: Line 2 does not exist")
    @CsvFileSource
    void calculate_line2Invalid(List<String> inputs) {
        String caseIdentifier = inputs.removeFirst();
        runPartialTest(inputs);
        String expectedMessage;
        switch (caseIdentifier) {
            case "ab" -> expectedMessage = "Некоректно задана пряма: пряма не може бути задана двома нулевими відрізками; Надайте ненулеві значення параметрам A та B.\n";
            case "a" -> expectedMessage = "Некоректно задана пряма: пряма не може віттинати нулевий відрізок на осі OX; Спробуйте ввести ненулеве значення параметра A.\n";
            case "b" -> expectedMessage = "Некоректно задана пряма: пряма не може віттинати нулевий відрізок на осі OY; Спробуйте ввести ненулеве значення параметра B.\n";
            default -> throw new IllegalArgumentException("");
        };
        expectedMessage += "Введіть параметри прямої ще раз, будь ласка.\n";
        assertEquals(expectedMessage, userInput.getLastOutput());
    }
    /**
     * @param inputs - Вхідні дані - список вхідних параметрів [X1;Y1;X2;Y2;A1;B1;A2;B2], де A1=A2 та B1=B2;
     * Наприклад, [0;1;2;3;1;2;1;2] - утворюють дві однакові прямі, що суперечить вимозі про різницю пар (A1; B1) та (A2; B2)
     */
    @ParameterizedTest
    @DisplayName("ERROR: Line 3 matches Line 2")
    @CsvFileSource
    void calculate_line3MatchesLine2(List<String> inputs) {
        String expectedMessage =
                "ПРЯМА 3 не може співпадати з ПРЯМОЮ 2.\n" +
                "Введіть параметри прямої ще раз, будь ласка.\n";
        assertEquals(expectedMessage, userInput.getLastOutput());
        runPartialTest(inputs);
    }

    private void runPartialTest(List<String> inputs) {
        userInput.supply(inputs);
        try {
            intersectionCalculator.calculate();
        } catch (OutOfInputsException ignored) {
            /*
             * Дане виключення викидується для того, щоб зупинити виконання програми і уникнути обрахунків у IntersectionCalculator
             * Стан userInput зберігається і доступний до подальшого опрацювання у тестах
             */
        }
    }
}