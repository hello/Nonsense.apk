package nonsense.model.timeline.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import nonsense.model.Formats;

public class TimelineV1 {
    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE)
    public final LocalDate date;

    @JsonProperty("insights")
    public final List<TimelineV1Insight> insights;

    @JsonProperty("message")
    public final String message;

    @JsonProperty("score")
    public final int score;

    @JsonProperty("segments")
    public final List<TimelineV1Segment> segments;

    @JsonProperty("statistics")
    public final Optional<TimelineV1Statistics> statistics;

    public TimelineV1(LocalDate date,
                      List<TimelineV1Insight> insights,
                      String message,
                      int score,
                      List<TimelineV1Segment> segments,
                      Optional<TimelineV1Statistics> statistics) {
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
}
