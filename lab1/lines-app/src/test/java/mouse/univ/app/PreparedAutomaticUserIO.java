package mouse.univ.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Queue-based implementation of Automatic User IO - each supplied input is consumed once and removed from the queue.d
 * Maintains a list of records to replicate the history of input/output, which may be used in debug purposes.
 */
public class PreparedAutomaticUserIO implements AutomaticUserIO {
    private enum RecordType {
        PROMPT, OUTPUT, INPUT
    }
    private record RecordedMessage(RecordType type, String value) {
    }
    private final List<String> preparedInputs;
    private final List<RecordedMessage> recordedMessages;
    public PreparedAutomaticUserIO() {
        preparedInputs = new ArrayList<>();
        recordedMessages = new ArrayList<>();
    }

    @Override
    public String getString() {
       return getString("");
    }

    @Override
    public String getString(String prompt) {
        if (preparedInputs.isEmpty()) {
            throw new OutOfInputsException("Не заданого наступного значення. Неможливо відповісти на запит: '" + prompt + "'");
        }
        String input = preparedInputs.removeFirst();
        recordedMessages.add(new RecordedMessage(RecordType.PROMPT, prompt));
        recordedMessages.add(new RecordedMessage(RecordType.INPUT, input));
        return input;
    }

    @Override
    public void println(String message) {
        recordedMessages.add(new RecordedMessage(RecordType.OUTPUT, message + '\n'));
    }

    @Override
    public void supply(Collection<String> inputs) {
        preparedInputs.addAll(inputs);
    }

    @Override
    public String getLastOutput() {
        int lastInputIndex = recordedMessages.size() - 1;
        while (lastInputIndex >= 0) {
            RecordedMessage message = recordedMessages.get(lastInputIndex);
            if (message.type() == RecordType.OUTPUT) {
                lastInputIndex--;
            } else {
                break;
            }
        }
        int firstOutputIndex = lastInputIndex + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = firstOutputIndex; i < recordedMessages.size(); i++) {
            RecordedMessage outputMessage = recordedMessages.get(i);
            builder.append(outputMessage.value());
        }
        return builder.toString();
    }
}
