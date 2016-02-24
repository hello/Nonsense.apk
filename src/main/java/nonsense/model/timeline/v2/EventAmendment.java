package nonsense.model.timeline.v2;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.joda.time.LocalTime;

import nonsense.model.Formats;

public class EventAmendment {
    @JsonProperty("new_event_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.TIME)
    public final LocalTime newTime;

    @JsonProperty("timezone_offset")
    public final long timeZoneOffset;

    public EventAmendment(@JsonProperty("new_event_time") LocalTime newTime,
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
