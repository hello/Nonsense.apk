package nonsense.model.trends;

import com.fasterxml.jackson.annotation.JsonProperty;

import nonsense.model.Condition;

public class ConditionRange {
    @JsonProperty("min_value")
    public final long minValue;

    @JsonProperty("max_value")
    public final long maxValue;

    @JsonProperty("condition")
    public final Condition condition;

    public ConditionRange(long minValue,
                          long maxValue,
                          Condition condition) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "ConditionRange{" +
                "minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", condition=" + condition +
                '}';
    }
}
