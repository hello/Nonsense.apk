package nonsense.model.timeline.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.OptionalLong;

import nonsense.model.Condition;

public class TimelineMetric {
    private static final long VALUE_MISSING = Long.MIN_VALUE;

    @JsonProperty("name")
    public final Name name;

    @JsonProperty("value")
    public final OptionalLong value;

    @JsonProperty("unit")
    public final Unit unit;

    @JsonProperty("condition")
    public final Condition condition;


    public TimelineMetric(Name name,
                          OptionalLong value,
                          Unit unit,
                          Condition condition) {
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "TimelineMetric{" +
                "name=" + name +
                ", value=" + value +
                ", unit=" + unit +
                ", condition=" + condition +
                '}';
    }


    public enum Name {
        UNKNOWN,
        TOTAL_SLEEP,
        SOUND_SLEEP,
        TIME_TO_SLEEP,
        TIMES_AWAKE,
        FELL_ASLEEP,
        WOKE_UP,
        TEMPERATURE,
        HUMIDITY,
        PARTICULATES,
        SOUND,
        LIGHT,
    }

    public enum Unit {
        MINUTES,
        QUANTITY,
        TIMESTAMP,
        CONDITION,
    }
}
