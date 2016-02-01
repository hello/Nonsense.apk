package nonsense.model.timeline.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;

import nonsense.util.Enums;

public class TimelineV1Segment {
    @JsonProperty("duration")
    public final long duration;

    @JsonProperty("event_type")
    public final Type eventType;

    @JsonProperty("id")
    public final long id;

    @JsonProperty("message")
    public final Optional<String> message;

    @JsonProperty("offset_millis")
    public final int tzOffsetMillis;

    @JsonProperty("sleep_depth")
    public final int sleepDepth;

    @JsonProperty("timestamp")
    public final long timestamp;

    @JsonProperty("sensors")
    public final List<String> sensors;

    @JsonProperty("sound")
    public final Optional<String> sound;

    public TimelineV1Segment(@JsonProperty("duration") long duration,
                             @JsonProperty("event_type") Type eventType,
                             @JsonProperty("id") long id,
                             @JsonProperty("message") Optional<String> message,
                             @JsonProperty("offset_millis") int tzOffsetMillis,
                             @JsonProperty("sleep_depth") int sleepDepth,
                             @JsonProperty("timestamp") long timestamp,
                             @JsonProperty("sensors") List<String> sensors,
                             @JsonProperty("sound") Optional<String> sound) {
        this.duration = duration;
        this.eventType = eventType;
        this.id = id;
        this.message = message;
        this.tzOffsetMillis = tzOffsetMillis;
        this.sleepDepth = sleepDepth;
        this.timestamp = timestamp;
        this.sensors = sensors;
        this.sound = sound;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "duration=" + duration +
                ", eventType=" + eventType +
                ", id=" + id +
                ", message=" + message +
                ", tzOffsetMillis=" + tzOffsetMillis +
                ", sleepDepth=" + sleepDepth +
                ", timestamp=" + timestamp +
                ", sensors=" + sensors +
                '}';
    }

    public enum Type {
        NONE(""),
        ALARM("Your alarm rang"),
        IN_BED("You got into bed"),
        LIGHT("There was a bright light"),
        LIGHTS_OUT("The lights went out"),
        MOTION("There was a lot of movement"),
        NOISE("There was a loud sound"),
        OUT_OF_BED("You got out of bed"),
        PARTNER_MOTION("Your partner was moving a lot"),
        SLEEP("You were sleeping"),
        SUNRISE("The sun rose"),
        SUNSET("The sun set"),
        WAKE_UP("You woke up"),
        SLEEPING("You were in bed"),
        SNORING("Someone was snoring"),
        SLEEP_TALK("Someone was sleep talking");

        public final String message;

        Type(String message) {
            this.message = message;
        }

        @JsonCreator
        public static Type fromString(String string) {
            return Enums.fromString(string, values(), NONE);
        }
    }
}
