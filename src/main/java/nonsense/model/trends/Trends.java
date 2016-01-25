package nonsense.model.trends;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Trends {
    @JsonProperty("available_time_scales")
    public final List<TimeScale> availableTimeScales;

    @JsonProperty("graphs")
    public final List<Graph> graphs;

    public Trends(List<TimeScale> availableTimeScales,
                  List<Graph> graphs) {
        this.availableTimeScales = availableTimeScales;
        this.graphs = graphs;
    }

    @Override
    public String toString() {
        return "Trends{" +
                "availableTimeScales=" + availableTimeScales +
                ", graphs=" + graphs +
                '}';
    }
}
