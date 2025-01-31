package mouse.univ.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PreparedAutomaticUserIO implements AutomaticUserIO {
    private final List<String> preparedInputs;
    private final List<String> recordedOutputs;
    public PreparedAutomaticUserIO() {
        preparedInputs = new ArrayList<>();
        recordedOutputs = new ArrayList<>();
    }

    @Override
    public String getString() {
        if (preparedInputs.isEmpty()) {
            throw new IllegalStateException("No input prepared, when asked for a string");
        }
        String input = preparedInputs.removeFirst();
        String log = "> " + input;
        recordedOutputs.add(log);
        return input;
    }

    @Override
    public String getString(String prompt) {
        if (preparedInputs.isEmpty()) {
            throw new IllegalStateException("No input prepared, when asked for a string with prompt: " + prompt);
        }
        String input = preparedInputs.removeFirst();
        String log = prompt + " > " + input;
        recordedOutputs.add(log);
        return input;
    }

    @Override
    public void print(String message) {
        recordedOutputs.add(message);
    }

    @Override
    public void println(String message) {
        recordedOutputs.add(message + '\n');
    }

    @Override
    public void supply(Collection<String> inputs) {
        preparedInputs.addAll(inputs);
    }


}
