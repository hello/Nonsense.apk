package nonsense.model.trends;

import org.junit.Test;

import java.util.Optional;

import nonsense.model.Condition;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DataTypeTests {
    @Test
    public void getConditionRanges() {
        assertThat(DataType.SCORES.getConditionRanges().isEmpty(), is(false));
        assertThat(DataType.HOURS.getConditionRanges().isEmpty(), is(true));
        assertThat(DataType.PERCENTS.getConditionRanges().isEmpty(), is(true));
    }

    @Test
    public void getConditionForValue() {
        assertThat(DataType.SCORES.getConditionForValue(0), is(equalTo(Optional.of(Condition.ALERT))));
        assertThat(DataType.SCORES.getConditionForValue(59), is(equalTo(Optional.of(Condition.ALERT))));
        assertThat(DataType.SCORES.getConditionForValue(60), is(equalTo(Optional.of(Condition.WARNING))));
        assertThat(DataType.SCORES.getConditionForValue(79), is(equalTo(Optional.of(Condition.WARNING))));
        assertThat(DataType.SCORES.getConditionForValue(80), is(equalTo(Optional.of(Condition.IDEAL))));
        assertThat(DataType.SCORES.getConditionForValue(100), is(equalTo(Optional.of(Condition.IDEAL))));
        assertThat(DataType.SCORES.getConditionForValue(-1), is(equalTo(Optional.empty())));
        assertThat(DataType.SCORES.getConditionForValue(101), is(equalTo(Optional.empty())));

        assertThat(DataType.HOURS.getConditionForValue(0), is(equalTo(Optional.empty())));
        assertThat(DataType.PERCENTS.getConditionForValue(0), is(equalTo(Optional.empty())));
    }
}
