package mouse.univ.app;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleUserIO implements UserIO {
    private final PrintStream output;
    private final Scanner scanner;
    public ConsoleUserIO(PrintStream outputStream, Scanner scanner) {
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
        return scanner.nextLine();
    }

    @Override
    public void print(String message) {
        output.print(message);
    }

    @Override
    public void println(String message) {
        output.println(message);
    }

}
