package nonsense.model.timeline.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.OptionalLong;

public class TimelineV1Statistics {
    @JsonProperty("sound_sleep")
    public final OptionalLong soundSleep;

    @JsonProperty("time_to_sleep")
    public final OptionalLong timeToSleep;

    @JsonProperty("times_awake")
    public final OptionalLong timesAwake;

    @JsonProperty("total_sleep")
    public final OptionalLong totalSleep;

    public static TimelineV1Statistics empty() {
        return new TimelineV1Statistics(OptionalLong.empty(),
                                        OptionalLong.empty(),
                                        OptionalLong.empty(),
                                        OptionalLong.empty());
    }

    public TimelineV1Statistics(@JsonProperty("sound_sleep") OptionalLong soundSleep,
                                @JsonProperty("time_to_sleep") OptionalLong timeToSleep,
                                @JsonProperty("times_awake") OptionalLong timesAwake,
                                @JsonProperty("total_sleep") OptionalLong totalSleep) {
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
