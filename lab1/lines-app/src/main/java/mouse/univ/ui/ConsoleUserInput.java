package mouse.univ.ui;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleUserInput implements UserInput {
    private final PrintStream output;
    private final Scanner scanner;
    public ConsoleUserInput(PrintStream outputStream, Scanner scanner) {
        this.scanner = scanner;
        this.output = outputStream;
    }

    private void askForInput() {
        output.print("> ");
    }

    @Override
    public int getInteger(String prompt) {
        printPrompt(prompt);
        return getInteger();
    }

    public int getInteger() {
        return getIntegerOrPrintError("Unexpected input; consider entering decimal integer value.");
    }
    private int getIntegerOrPrintError(String message) {
        askForInput();
        while (true) {
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line);
            } catch (Exception e) {
                output.println(message);
                askForInput();
            }
        }
    }

    @Override
    public int getRangedInteger(String prompt, int range) {
        if (range < 0) {
            throw new IllegalArgumentException("Given range of the integer is expected to be non-negative");
        }
        printPrompt(prompt);
        int result;
        String err = String.format("Unexpected input; consider entering decimal integer value within range of [-%d, %d]", range, range);
        while (true) {
            result = getIntegerOrPrintError(err);
            if (result <= range) {
                return result;
            } else {
                output.println(err);
            }
        }

    }

    private void printPrompt(String prompt) {
        if (prompt != null && !prompt.isEmpty()) {
            output.println(prompt);
        }
    }

}
