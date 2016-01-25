package nonsense.model.trends;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

import nonsense.model.Condition;

public class Annotation {
    @JsonProperty("title")
    public final String title;

    @JsonProperty("value")
    public final double value;

    @JsonProperty("data_type")
    public final DataType dataType;

    @JsonProperty("condition")
    public final Optional<Condition> condition;

    public Annotation(String title,
                      double value,
                      DataType dataType,
                      Optional<Condition> condition) {
        this.title = title;
        this.value = value;
        this.dataType = dataType;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Annotation{" +
                "title='" + title + '\'' +
                ", value=" + value +
                ", dataType=" + dataType +
                ", condition=" + condition +
                '}';
    }
}
