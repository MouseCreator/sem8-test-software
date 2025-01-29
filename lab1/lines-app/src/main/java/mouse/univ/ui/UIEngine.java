package mouse.univ.ui;

import mouse.univ.common.Numbers;
import mouse.univ.geometry.GenericLine;
import mouse.univ.geometry.IntersectionCalculator;
import mouse.univ.geometry.InvalidLineException;
import mouse.univ.geometry.Point;

import java.io.PrintStream;
import java.util.List;

public class UIEngine {
    public final UserInput userInput;
    public final PrintStream printStream;
    private final IntersectionCalculator intersectionCalculator;
    private boolean exited;
    public UIEngine(UserInput userInput, IntersectionCalculator intersectionCalculator) {
        this.userInput = userInput;
        this.printStream = userInput.getPrintStream();
        this.intersectionCalculator = intersectionCalculator;
        exited = false;
    }

    public void start() {
        printStream.println("Welcome to Three Lines Intersection Application");
        printStream.println("Type 'help' to see a help message");
        while (!exited) {
            printStream.println("Type a command");
            String command = userInput.getString();
            try {
                onCommand(command);
            } catch (Exception e) {
                printStream.println("Error: " + e.getMessage());
            }
        }
    }
    private void onCommand(String command) {
        String formattedCommand = getFormattedCommand(command);
        switch (formattedCommand) {
            case "help" -> seeHelp();
            case "exit" -> onExit();
            case "calc" -> onCalculate();
            default -> printStream.println("Unknown command! Print 'help' to see a help message");
        }
    }

    private static String getFormattedCommand(String command) {
        return command.toLowerCase().trim().replace("'", "");
    }

    private void onCalculate() {
        List<GenericLine> arguments;
        try {
            arguments = getArguments();
        } catch (TerminatedException e) {
            return;
        }
        if (arguments.size() != 3) {
            throw new IllegalStateException("Expected to have 3 input lines. Got " + arguments.size() + " instead: " + arguments);
        }
        String result = intersectionCalculator.getIntersections(arguments.get(0), arguments.get(1), arguments.get(2));
        printStream.println(result);
    }
    private List<GenericLine> getArguments() throws TerminatedException {
        GenericLine g1 = lineByTwoPoints("LINE 1");
        GenericLine g2 = lineByTwoSegments("LINE 2");
        GenericLine g3 = lineByTwoSegments("LINE 3");
        return List.of(g1, g2, g3);
    }
    private GenericLine lineByTwoPoints(String lineName) throws TerminatedException {
        while (true) {
            printStream.println("Define " + lineName + " by two points (X1; Y1), (X2; Y2)");
            int x1 = provideRangedIntOrTerminate("X1");
            int y1 = provideRangedIntOrTerminate("Y1");
            int x2 = provideRangedIntOrTerminate("X2");
            int y2 = provideRangedIntOrTerminate("Y2");
            try {
                return GenericLine.fromTwoPoints(
                        new Point(x1, y1),
                        new Point(x2, y2));
            } catch (InvalidLineException e) {
                onInvalidLineEntered(e);
            }
        }
    }

    private void onInvalidLineEntered(InvalidLineException e) {
        printStream.println(e.getMessage());
        printStream.println("Enter the line arguments again, please!");
    }

    private GenericLine lineByTwoSegments(String lineName) throws TerminatedException {
        while (true) {
            printStream.println("Define " + lineName + " by two segments. A - intersection with x axis, B - intersection with y axis");
            int a1 = provideRangedIntOrTerminate("A");
            int b1 = provideRangedIntOrTerminate("B");
            try {
                return GenericLine.fromTwoSegments(a1, b1);
            } catch (InvalidLineException e) {
                onInvalidLineEntered(e);
            }
        }
    }

    private int provideRangedIntOrTerminate(String prompt) throws TerminatedException {
        while (true) {
            String string = userInput.getString(prompt);
            if (getFormattedCommand(string).equals("e")) {
                throw new TerminatedException();
            }
            try {
                int result = Integer.parseInt(string);
                if (Numbers.isOutOfRange(result)) {
                    String err = String.format("Provided integer is out of bounds of allowed box: [-%f, %f]", Numbers.BOX_SIZE, Numbers.BOX_SIZE);
                    printStream.println(err);
                } else {
                    return result;
                }
            } catch (Exception e) {
                printStream.println("Invalid input; Please, enter a decimal integer value.");
            }
        }
    }

    private void onExit() {
        printStream.println("Exiting...");
        exited = true;
    }

    private void seeHelp() {
        String help = """
                === HELP ===
                calc - calculate line intersections;
                    * For each prompt, an integer value is expected. Valid inputs rage: [-122, 122].
                    * If invalid value is entered, you will be asked to reenter the value.
                    * While in calc mode, you may enter 'e' to escape to the main menu
                help - see this message
                exit - close the application
                ============
                """;
        printStream.println(help);
    }

}
