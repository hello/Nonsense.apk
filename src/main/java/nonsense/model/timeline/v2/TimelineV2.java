package nonsense.model.timeline.v2;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.OptionalInt;

import nonsense.model.Formats;

public class TimelineV2 {
    @JsonProperty("score")
    public final OptionalInt score;

    @JsonProperty("score_condition")
    public final ScoreCondition scoreCondition;

    @JsonProperty("message")
    public final String message;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE)
    public final LocalDate date;

    @JsonProperty("events")
    public final List<TimelineEvent> events;

    @JsonProperty("metrics")
    public final List<TimelineMetric> metrics;

    public TimelineV2(OptionalInt score,
                      ScoreCondition scoreCondition,
                      String message,
                      LocalDate date,
                      List<TimelineEvent> events,
                      List<TimelineMetric> metrics) {
        this.score = score;
        this.scoreCondition = scoreCondition;
        this.message = message;
        this.date = date;
        this.events = events;
        this.metrics = metrics;
    }


    @Override
    public String toString() {
        return "TimelineV2{" +
                "score=" + score +
                ", scoreCondition=" + scoreCondition +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", events=" + events +
                ", metrics=" + metrics +
                '}';
    }
}
