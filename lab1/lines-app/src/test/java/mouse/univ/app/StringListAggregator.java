package mouse.univ.app;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

import java.util.ArrayList;
import java.util.List;

public class StringListAggregator implements ArgumentsAggregator {
    @Override
    public List<String> aggregateArguments(ArgumentsAccessor accessor,
                                           ParameterContext context) throws ArgumentsAggregationException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < accessor.size(); i++) {
            list.add(accessor.getString(i));
        }
        return list;
    }
}
