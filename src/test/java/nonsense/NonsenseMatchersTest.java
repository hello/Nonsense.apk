package nonsense;

import org.junit.Test;

import static nonsense.NonsenseMatchers.greaterThan;
import static nonsense.NonsenseMatchers.greaterThanOrEqual;
import static nonsense.NonsenseMatchers.lessThan;
import static nonsense.NonsenseMatchers.lessThanOrEqual;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class NonsenseMatchersTest {
    @Test
    public void greaterThanNumber() {
        assertThat(10, is(greaterThan(5)));
        assertThat(10L, is(greaterThan(5L)));
        assertThat(10f, is(greaterThan(5f)));
        assertThat(10.0, is(greaterThan(5.0)));

        assertThat(5, is(not(greaterThan(10))));
        assertThat(5L, is(not(greaterThan(10L))));
        assertThat(5f, is(not(greaterThan(10f))));
        assertThat(5.0, is(not(greaterThan(10.0))));
    }

    @Test
    public void greaterThanOrEqualToNumber() {
        assertThat(10, is(greaterThanOrEqual(5)));
        assertThat(10L, is(greaterThanOrEqual(5L)));
        assertThat(10f, is(greaterThanOrEqual(5f)));
        assertThat(10.0, is(greaterThanOrEqual(5.0)));

        assertThat(5, is(not(greaterThanOrEqual(10))));
        assertThat(5L, is(not(greaterThanOrEqual(10L))));
        assertThat(5f, is(not(greaterThanOrEqual(10f))));
        assertThat(5.0, is(not(greaterThanOrEqual(10.0))));

        assertThat(10, is(greaterThanOrEqual(10)));
        assertThat(10L, is(greaterThanOrEqual(10L)));
        assertThat(10f, is(greaterThanOrEqual(10f)));
        assertThat(10.0, is(greaterThanOrEqual(10.0)));
    }

    @Test
    public void lessThanNumber() {
        assertThat(5, is(lessThan(10)));
        assertThat(5L, is(lessThan(10L)));
        assertThat(5f, is(lessThan(10f)));
        assertThat(5.0, is(lessThan(10.0)));

        assertThat(10, is(not(lessThan(5))));
        assertThat(10L, is(not(lessThan(5L))));
        assertThat(10f, is(not(lessThan(5f))));
        assertThat(10.0, is(not(lessThan(5.0))));
    }

    @Test
    public void lessThanOrEqualToNumber() {
        assertThat(5, is(lessThanOrEqual(10)));
        assertThat(5L, is(lessThanOrEqual(10L)));
        assertThat(5f, is(lessThanOrEqual(10f)));
        assertThat(5.0, is(lessThanOrEqual(10.0)));

        assertThat(10, is(not(lessThanOrEqual(5))));
        assertThat(10L, is(not(lessThanOrEqual(5L))));
        assertThat(10f, is(not(lessThanOrEqual(5f))));
        assertThat(10.0, is(not(lessThanOrEqual(5.0))));

        assertThat(10, is(lessThanOrEqual(10)));
        assertThat(10L, is(lessThanOrEqual(10L)));
        assertThat(10f, is(lessThanOrEqual(10f)));
        assertThat(10.0, is(lessThanOrEqual(10.0)));
    }
}
