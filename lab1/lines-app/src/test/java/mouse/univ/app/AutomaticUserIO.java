package mouse.univ.app;

import java.util.Collection;

public interface AutomaticUserIO extends UserIO{
    void supply(Collection<String> inputs);
    String getLastOutput();
}
