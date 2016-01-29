package nonsense.util;

import java.util.Random;

public class RandomUtil {
    public static double doubleInRange(Random random, double min, double max) {
        return min + (random.nextDouble() * (max - min));
    }

    public static int integerInRange(Random random, int min, int max) {
        return min + random.nextInt(max - min);
    }

    public static <T> T inArray(Random random, T[] values) {
        final int position = random.nextInt(values.length);
        return values[position];
    }
}
