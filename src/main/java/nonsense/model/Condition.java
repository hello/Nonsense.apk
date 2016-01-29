package nonsense.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import nonsense.util.Enums;

public enum Condition {
    ALERT,
    WARNING,
    IDEAL,
    UNKNOWN;

    @JsonCreator
    public static Condition fromString(String string) {
        return Enums.fromString(string, values(), UNKNOWN);
    }
}
