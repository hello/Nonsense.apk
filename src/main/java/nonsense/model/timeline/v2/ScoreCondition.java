package nonsense.model.timeline.v2;

import com.fasterxml.jackson.annotation.JsonCreator;

import nonsense.model.util.Enums;

public enum ScoreCondition {
    UNAVAILABLE,
    INCOMPLETE,
    ALERT,
    WARNING,
    IDEAL;

    @JsonCreator
    public static ScoreCondition fromString(String string) {
        return Enums.fromString(string, values(), UNAVAILABLE);
    }
}
