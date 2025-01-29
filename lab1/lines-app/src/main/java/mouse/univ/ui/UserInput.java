package mouse.univ.ui;

import java.io.PrintStream;

public interface UserInput {
    String getString();
    String getString(String prompt);
    PrintStream getPrintStream();
}
