package nonsense.model.trends;

import nonsense.model.util.Enums;

public enum TimeScale {
    LAST_WEEK,
    LAST_MONTH,
    LAST_3_MONTHS,
    UNKNOWN;

    public static TimeScale fromString(String string) {
        return Enums.fromString(string, values(), UNKNOWN);
    }
}
