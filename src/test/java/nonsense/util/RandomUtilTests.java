package nonsense.util;

import org.junit.Test;

import java.util.Random;

import static nonsense.NonsenseMatchers.greaterThanOrEqual;
import static nonsense.NonsenseMatchers.lessThanOrEqual;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RandomUtilTests {
    @Test
    public void doubleInRange() {
        final Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            final double value = RandomUtil.doubleInRange(random, -10.0, 10.0);
            assertThat(value, is(greaterThanOrEqual(-10.0)));
            assertThat(value, is(lessThanOrEqual(10.0)));
        }
    }

    @Test
    public void integerInRange() {
        final Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            final int value = RandomUtil.integerInRange(random, -10, 10);
            assertThat(value, is(greaterThanOrEqual(-10)));
            assertThat(value, is(lessThanOrEqual(10)));
        }
    }
}
