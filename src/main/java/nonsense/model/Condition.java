package nonsense.model;

import nonsense.model.util.Enums;

public enum Condition {
    ALERT,
    WARNING,
    IDEAL,
    UNKNOWN;

    public static Condition fromString(String string) {
        return Enums.fromString(string, values(), UNKNOWN);
    }
}
