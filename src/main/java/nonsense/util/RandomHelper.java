package nonsense.util;

import java.util.Random;

public class RandomHelper {
    public static double doubleInRange(Random random, double min, double max) {
        return min + (random.nextDouble() * (max - min));
    }

    public static int integerInRange(Random random, int min, int max) {
        return min + random.nextInt(max - min);
    }
}
