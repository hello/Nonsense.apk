package nonsense.model.timeline.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TimelineV2Event {
    @JsonProperty("timestamp")
    public final long timestamp;

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

    public TimelineV2Event(long timestamp,
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
        SOUND;

        public static SleepState fromSleepDepth(int sleepDepth) {
            if (sleepDepth < 5) {
                return AWAKE;
            } else if (sleepDepth < 10) {
                return LIGHT;
            } else if (sleepDepth < 70) {
                return MEDIUM;
            }
            return SOUND;
        }
    }

    public enum Action {
        ADJUST_TIME,
        VERIFY,
        REMOVE,
        INCORRECT;

        public static List<Action> forEventType(Type type) {
            switch (type) {
                case FELL_ASLEEP:
                case IN_BED:
                case GOT_OUT_OF_BED:
                case WOKE_UP:
                    return Lists.newArrayList(ADJUST_TIME, VERIFY, INCORRECT);
                case LIGHTS_OUT:
                case GENERIC_MOTION:
                case PARTNER_MOTION:
                case GENERIC_SOUND:
                case SNORED:
                case SLEEP_TALKED:
                    return Lists.newArrayList(VERIFY, INCORRECT);
                default:
                    return Collections.emptyList();
            }
        }
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
}
