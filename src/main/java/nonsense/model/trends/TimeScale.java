package nonsense.model.trends;

import java.util.ArrayList;
import java.util.List;

import nonsense.model.util.Enums;

public enum TimeScale {
    LAST_WEEK,
    LAST_MONTH,
    LAST_3_MONTHS,
    UNKNOWN;

    public static TimeScale fromString(String string) {
        return Enums.fromString(string, values(), UNKNOWN);
    }

    public static List<TimeScale> fromAccountAge(int days) {
        final List<TimeScale> scales = new ArrayList<>(3);
        if (days >= 3) {
            scales.add(LAST_WEEK);
        }
        if (days >= 28) {
            scales.add(LAST_MONTH);
        }
        if (days >= 90) {
            scales.add(LAST_3_MONTHS);
        }
        return scales;
    }
}
