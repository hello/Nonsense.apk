package nonsense.model.trends;

import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TimeScaleTest {
    @Test
    public void fromAccountAge() {
        assertThat(TimeScale.fromAccountAge(2), is(equalTo(Collections.emptyList())));
        assertThat(TimeScale.fromAccountAge(3), hasItem(TimeScale.LAST_WEEK));
        assertThat(TimeScale.fromAccountAge(28), allOf(hasItem(TimeScale.LAST_WEEK),
                                                       hasItem(TimeScale.LAST_MONTH)));
        assertThat(TimeScale.fromAccountAge(90), allOf(hasItem(TimeScale.LAST_WEEK),
                                                       hasItem(TimeScale.LAST_MONTH),
                                                       hasItem(TimeScale.LAST_3_MONTHS)));
    }
}
