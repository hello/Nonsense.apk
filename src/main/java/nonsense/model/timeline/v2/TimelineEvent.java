package nonsense.model.timeline.v2;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import nonsense.model.Formats;

public class TimelineEvent {
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public final LocalDate timestamp;

    @JsonProperty("timezone_offset")
    public final int timezoneOffset;

    @JsonProperty("duration_millis")
    public final long durationMillis;

    @JsonProperty("message")
    public final Optional<String> message;

    @JsonProperty("sleep_depth")
    public final int sleepDepth;

    @JsonProperty("sleep_state")
    public final SleepState sleepState;

    @JsonProperty("event_type")
    public final Type type;

    @JsonProperty("valid_actions")
    public final List<Action> validActions;

    public TimelineEvent(LocalDate timestamp,
                         int timezoneOffset,
                         long durationMillis,
                         Optional<String> message,
                         int sleepDepth,
                         SleepState sleepState,
                         Type type,
                         List<Action> validActions) {
        this.timestamp = timestamp;
        this.timezoneOffset = timezoneOffset;
        this.durationMillis = durationMillis;
        this.message = message;
        this.sleepDepth = sleepDepth;
        this.sleepState = sleepState;
        this.type = type;
        this.validActions = validActions;
    }

    @Override
    public String toString() {
        return "TimelineEvent{" +
                "timestamp=" + timestamp +
                ", timezoneOffset=" + timezoneOffset +
                ", durationMillis=" + durationMillis +
                ", message='" + message + '\'' +
                ", sleepDepth=" + sleepDepth +
                ", sleepState=" + sleepState +
                ", type=" + type +
                ", validActions=" + validActions +
                '}';
    }


    public enum SleepState {
        AWAKE,
        LIGHT,
        MEDIUM,
        SOUND,
    }

    public enum Action {
        ADJUST_TIME,
        VERIFY,
        REMOVE,
        INCORRECT,
    }

    public enum Type {
        IN_BED,
        GENERIC_MOTION,
        PARTNER_MOTION,
        GENERIC_SOUND,
        SNORED,
        SLEEP_TALKED,
        LIGHT,
        LIGHTS_OUT,
        SUNSET,
        SUNRISE,
        GOT_IN_BED,
        FELL_ASLEEP,
        GOT_OUT_OF_BED,
        WOKE_UP,
        ALARM_RANG,
        UNKNOWN,
    }

    public static class TimeAmendment {
        @JsonProperty("new_event_time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.TIME)
        public final LocalTime newTime;

        @JsonProperty("timezone_offset")
        public final long timeZoneOffset;

        public TimeAmendment(@JsonProperty("new_event_time") LocalTime newTime,
                             @JsonProperty("timezone_offset") long timeZoneOffset) {
            this.newTime = newTime;
            this.timeZoneOffset = timeZoneOffset;
        }

        @Override
        public String toString() {
            return "TimeAmendment{" +
                    "newTime=" + newTime +
                    '}';
        }
    }
}
