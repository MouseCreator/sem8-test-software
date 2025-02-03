package mouse.univ.app;

/**
 * Thrown by Automatic User Input/Output, when ran out of supplied inputs
 * Interrupts the application execution
 */
public class OutOfInputsException extends RuntimeException {
    public OutOfInputsException(String message) {
        super(message);
    }
}
