package mouse.univ.app;

import mouse.univ.common.Messages;

import java.util.Scanner;

public class LinesApp {
    public static void main(String[] args) {
        ConsoleUserIO consoleUserInput = new ConsoleUserIO(System.out, new Scanner(System.in));
        IntersectionCalculator intersectionCalculator = new IntersectionCalculator(consoleUserInput);
        printHelp(consoleUserInput);
        while (true) {
            String result = intersectionCalculator.calculate();
            if (result.equals(Messages.terminated())) {
                break;
            }
        }
        consoleUserInput.println("Exiting...");
    }

    private static void printHelp(UserIO userIO) {
        String help = """
            === INTERSECTION CALCULATOR ===
            * For each prompt, an integer value is expected. Valid inputs rage: [-122, 122].
            * If invalid value is entered, you will be asked to reenter the value.
            * You may enter 'e' instead of an integer to terminate the application.
            ===============================
            """;
        userIO.println(help);
    }
}
