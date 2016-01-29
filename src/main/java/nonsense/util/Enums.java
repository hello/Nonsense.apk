package nonsense.util;

import java.util.Random;

public class Enums {
    public static <T extends Enum> T fromString(String string, T[] values, T defaultValue) {
        for (final T value : values) {
            if (value.toString().equalsIgnoreCase(string)) {
                return value;
            }
        }
        return defaultValue;
    }

    @SafeVarargs
    public static <T extends Enum> T random(T... values) {
        final int position = new Random().nextInt(values.length);
        return values[position];
    }
}
