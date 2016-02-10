package nonsense.model.insights;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CategoryTest {
    @Test
    public void getName() {
        assertThat(Category.GENERIC.getName(), is(equalTo("generic")));
        assertThat(Category.AIR_QUALITY.getName(), is(equalTo("air quality")));
        assertThat(Category.BED_LIGHT_INTENSITY_RATIO.getName(), is(equalTo("bed light intensity ratio")));
    }
}
