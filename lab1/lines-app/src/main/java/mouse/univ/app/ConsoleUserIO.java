package mouse.univ.app;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Stream-based implementation of user input / output
 */
public class ConsoleUserIO implements UserIO {
    private final PrintStream output;
    private final Scanner scanner;

    /**
     * Creates ConsoleUserIO with given output and input streams.
     * When provided with <code>System.out</code> and <code>System.in</code>, a console application is created.
     * @param outputStream - a stream, in which the output is printed
     * @param scanner - scans given input stream
     */
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
    public void println(String message) {
        output.println(message);
    }

}
