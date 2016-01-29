package nonsense.model.timeline.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public final LocalDateTime timestamp;

    @JsonProperty("sensors")
    public final List<String> sensors;

    public TimelineV1Segment(long duration,
                             Type eventType,
                             long id,
                             Optional<String> message,
                             int tzOffsetMillis,
                             int sleepDepth,
                             LocalDateTime timestamp,
                             List<String> sensors) {
        this.duration = duration;
        this.eventType = eventType;
        this.id = id;
        this.message = message;
        this.tzOffsetMillis = tzOffsetMillis;
        this.sleepDepth = sleepDepth;
        this.timestamp = timestamp;
        this.sensors = sensors;
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
        NONE,
        ALARM,
        IN_BED,
        LIGHT,
        LIGHTS_OUT,
        MOTION,
        NOISE,
        OUT_OF_BED,
        PARTNER_MOTION,
        SLEEP,
        SUNRISE,
        SUNSET,
        WAKE_UP,
        SLEEPING,
        SNORING,
        SLEEP_TALK,
    }
}
