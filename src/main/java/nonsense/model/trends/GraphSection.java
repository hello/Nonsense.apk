package nonsense.model.trends;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

public class GraphSection {
    @JsonProperty("values")
    public final List<Double> values;

    @JsonProperty("titles")
    public final List<String> titles;

    @JsonProperty("highlighted_values")
    public final List<Integer> highlightedValues;

    @JsonProperty("highlighted_title")
    public final OptionalInt highlightedTitle;

    public GraphSection(List<Double> values,
                        List<String> titles,
                        List<Integer> highlightedValues,
                        OptionalInt highlightedTitle) {
        this.values = values;
        this.titles = titles;
        this.highlightedValues = highlightedValues;
        this.highlightedTitle = highlightedTitle;
    }

    public static GraphSection newSleepDepth(List<Double> values) {
        return new GraphSection(values,
                                Lists.newArrayList("Light", "Medium", "Deep"),
                                Collections.emptyList(),
                                OptionalInt.empty());
    }

    public GraphSection withTitles(List<String> titles) {
        return new GraphSection(values,
                                titles,
                                highlightedValues,
                                highlightedTitle);
    }

    public GraphSection withHighlightedValues(List<Integer> highlightedValues) {
        return new GraphSection(values,
                                titles,
                                highlightedValues,
                                highlightedTitle);
    }

    @Override
    public String toString() {
        return "GraphSection{" +
                "values=" + values +
                ", titles=" + titles +
                ", highlightedValues=" + highlightedValues +
                ", highlightedTitle=" + highlightedTitle +
                '}';
    }
}
