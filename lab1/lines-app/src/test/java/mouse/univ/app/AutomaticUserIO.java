package mouse.univ.app;

import java.util.Collection;

/**
 * User Input/Output that uses predefined values to communicate with the application
 * Used as a stub; Allows the tests to run automatically, without console input by a user
 */
public interface AutomaticUserIO extends UserIO{
    void supply(Collection<String> inputs);
    String getLastOutput();
}
