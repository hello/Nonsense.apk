package nonsense.model.timeline.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import nonsense.model.Condition;
import nonsense.util.Enums;

public class TimelineV1Insight {
    @JsonProperty("condition")
    public final Condition condition;

    @JsonProperty("message")
    public final String message;

    @JsonProperty("sensor")
    public final Sensor sensor;

    public TimelineV1Insight(@JsonProperty("condition") Condition condition,
                             @JsonProperty("message") String message,
                             @JsonProperty("sensor") Sensor sensor) {
        this.condition = condition;
        this.message = message;
        this.sensor = sensor;
    }

    @Override
    public String toString() {
        return "Insight{" +
                "condition=" + condition +
                ", message='" + message + '\'' +
                ", sensor=" + sensor +
                '}';
    }

    public enum Sensor {
        HUMIDITY,
        LIGHT,
        PARTICULATES,
        SOUND,
        TEMPERATURE,
        UNKNOWN;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        @JsonCreator
        public static Sensor fromString(String string) {
            return Enums.fromString(string, values(), UNKNOWN);
        }
    }
}
