package mouse.univ.app;

/**
 * The interface for a class that handles user input and output
 * Encapsulates the input and output streams, allowing the application to scan and print messages
 * regardless of the source of the messages
 */
public interface UserIO {
    String getString();
    String getString(String prompt);
    void println(String message);
}
