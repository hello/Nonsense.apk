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

    public TimelineV1Statistics(OptionalLong soundSleep,
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
