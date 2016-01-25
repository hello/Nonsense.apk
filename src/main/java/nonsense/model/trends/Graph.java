package nonsense.model.trends;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class Graph {
    @JsonProperty("time_scale")
    public final TimeScale timeScale;

    @JsonProperty("title")
    public final String title;

    @JsonProperty("data_type")
    public final DataType dataType;

    @JsonProperty("graph_type")
    public final GraphType graphType;

    @JsonProperty("min_value")
    public final double minValue;

    @JsonProperty("max_value")
    public final double maxValue;

    @JsonProperty("sections")
    public final List<GraphSection> sections;

    @JsonProperty("condition_ranges")
    public final List<ConditionRange> conditionRanges;

    @JsonProperty("annotations")
    public final List<Annotation> annotations;

    public Graph(TimeScale timeScale,
                 String title,
                 DataType dataType,
                 GraphType graphType,
                 List<GraphSection> sections,
                 List<Annotation> annotations) {
        this.timeScale = timeScale;
        this.title = title;
        this.dataType = dataType;
        this.graphType = graphType;
        this.minValue = dataType.actualMin;
        this.maxValue = dataType.actualMax;
        this.sections = sections;
        this.conditionRanges = dataType.getConditionRanges();
        this.annotations = annotations;
    }

    public static Graph newSleepScore(TimeScale timeScale,
                                      GraphType graphType,
                                      List<GraphSection> sections,
                                      List<Annotation> annotations) {
        return new Graph(timeScale,
                         "Sleep Score",
                         DataType.SCORES,
                         graphType,
                         sections,
                         annotations);
    }

    public static Graph newSleepDuration(TimeScale timeScale,
                                         List<GraphSection> sections,
                                         List<Annotation> annotations) {
        return new Graph(timeScale,
                         "Sleep Duration",
                         DataType.HOURS,
                         GraphType.BAR,
                         sections,
                         annotations);
    }

    public static Graph newSleepDepth(TimeScale timeScale,
                                      List<GraphSection> sections) {
        return new Graph(timeScale,
                         "Sleep Depth",
                         DataType.PERCENTS,
                         GraphType.BUBBLES,
                         sections,
                         Collections.emptyList());
    }

    @Override
    public String toString() {
        return "Graph{" +
                "timeScale=" + timeScale +
                ", title='" + title + '\'' +
                ", dataType=" + dataType +
                ", graphType=" + graphType +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", sections=" + sections +
                ", conditionRanges=" + conditionRanges +
                ", annotations=" + annotations +
                '}';
    }
}
