package mouse.univ.geometry;

/**
 * Exception thrown, when a line cannot be constructed by the given values
 */
public class InvalidLineException extends Exception {
    public InvalidLineException(String message) {
        super(message);
    }
}
