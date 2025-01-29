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

    private void askForInput(String prompt) {
        if (prompt == null || prompt.isEmpty()) {
            output.print("> ");
        } else {
            output.print(prompt + " > ");
        }
    }

    @Override
    public String getString() {
        return getString(null);
    }

    @Override
    public String getString(String prompt) {
        askForInput(prompt);
        while (true) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                output.println("Empty input; consider entering a non-empty string.");
                continue;
            }
            return line;
        }
    }

    @Override
    public PrintStream getPrintStream() {
        return output;
    }


}
