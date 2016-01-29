package nonsense.model.timeline.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import nonsense.model.Condition;
import nonsense.model.Formats;

public class TimelineV1 {
    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE)
    public final LocalDate date;

    @JsonProperty("insights")
    public final List<Insight> insights;

    @JsonProperty("message")
    public final String message;

    @JsonProperty("score")
    public final int score;

    @JsonProperty("segments")
    public final List<Segment> segments;

    @JsonProperty("statistics")
    public final Statistics statistics;

    public TimelineV1(LocalDate date,
                      List<Insight> insights,
                      String message,
                      int score,
                      List<Segment> segments,
                      Statistics statistics) {
        this.date = date;
        this.insights = insights;
        this.message = message;
        this.score = score;
        this.segments = segments;
        this.statistics = statistics;
    }

    @Override
    public String toString() {
        return "TimelineV1{" +
                "date=" + date +
                ", insights=" + insights +
                ", message='" + message + '\'' +
                ", score=" + score +
                ", segments=" + segments +
                ", statistics=" + statistics +
                '}';
    }


    public static class Statistics {
        @JsonProperty("sound_sleep")
        public final OptionalLong soundSleep;

        @JsonProperty("time_to_sleep")
        public final OptionalLong timeToSleep;

        @JsonProperty("times_awake")
        public final OptionalLong timesAwake;

        @JsonProperty("total_sleep")
        public final OptionalLong totalSleep;

        public Statistics(OptionalLong soundSleep,
                          OptionalLong timeToSleep,
                          OptionalLong timesAwake,
                          OptionalLong totalSleep) {
            this.soundSleep = soundSleep;
            this.timeToSleep = timeToSleep;
            this.timesAwake = timesAwake;
            this.totalSleep = totalSleep;
        }

        @Override
        public String toString() {
            return "Statistics{" +
                    "soundSleep=" + soundSleep +
                    ", timeToSleep=" + timeToSleep +
                    ", timesAwake=" + timesAwake +
                    ", totalSleep=" + totalSleep +
                    '}';
        }
    }

    public static class Insight {
        @JsonProperty("condition")
        public final Condition condition;

        @JsonProperty("message")
        public final String message;

        @JsonProperty("sensor")
        public final Sensor sensor;

        public Insight(Condition condition,
                       String message,
                       Sensor sensor) {
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
    }

    public static class Segment {
        @JsonProperty("duration")
        public final long duration;

        @JsonProperty("event_type")
        public final EventType eventType;

        @JsonProperty("id")
        public final long id;

        @JsonProperty("message")
        public final Optional<String> message;

        @JsonProperty("offset_millis")
        public final long tzOffsetMillis;

        @JsonProperty("sleep_depth")
        public final int sleepDepth;

        @JsonProperty("timestamp")
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        public final LocalDateTime timestamp;

        @JsonProperty("sensors")
        public final List<String> sensors;

        public Segment(long duration, EventType eventType,
                       long id,
                       Optional<String> message,
                       long tzOffsetMillis,
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
    }

    public enum EventType {
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
    }

    public enum Sensor {
        HUMIDITY,
        LIGHT,
        PARTICULATES,
        SOUND,
        TEMPERATURE,
    }
}
