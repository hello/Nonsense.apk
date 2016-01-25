package nonsense.model.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EnumsTests {
    @Test
    public void fromString() {
        assertThat(Enums.fromString("FIRST", Values.values(), Values.UNKNOWN), is(equalTo(Values.FIRST)));
        assertThat(Enums.fromString("first", Values.values(), Values.UNKNOWN), is(equalTo(Values.FIRST)));
        assertThat(Enums.fromString("SECOND", Values.values(), Values.UNKNOWN), is(equalTo(Values.SECOND)));
        assertThat(Enums.fromString("second", Values.values(), Values.UNKNOWN), is(equalTo(Values.SECOND)));
        assertThat(Enums.fromString("THIRD", Values.values(), Values.UNKNOWN), is(equalTo(Values.THIRD)));
        assertThat(Enums.fromString("third", Values.values(), Values.UNKNOWN), is(equalTo(Values.THIRD)));
        assertThat(Enums.fromString("???", Values.values(), Values.UNKNOWN), is(equalTo(Values.UNKNOWN)));
    }

    enum Values {
        FIRST,
        SECOND,
        THIRD,
        UNKNOWN,
    }
}
