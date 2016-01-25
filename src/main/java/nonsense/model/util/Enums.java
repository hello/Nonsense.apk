package nonsense.model.util;

public class Enums {
    public static <T extends Enum> T fromString(String string, T[] values, T defaultValue) {
        for (final T value : values) {
            if (value.toString().equalsIgnoreCase(string)) {
                return value;
            }
        }
        return defaultValue;
    }
}
