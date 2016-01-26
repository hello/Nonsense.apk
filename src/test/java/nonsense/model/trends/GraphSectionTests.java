package nonsense.model.trends;

import com.google.common.collect.Lists;

import org.junit.Test;

import java.util.Collections;
import java.util.OptionalInt;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class GraphSectionTests {
    @Test
    public void newSleepDepth() {
        final GraphSection section = GraphSection.newSleepDepth(Lists.newArrayList(0.3, 0.3, 0.4));
        assertThat(section.values.size(), is(equalTo(3)));
        assertThat(section.values, allOf(hasItem(0.3), hasItems(0.4)));
        assertThat(section.titles, allOf(hasItem("Light"), hasItem("Medium"), hasItem("Deep")));
        assertThat(section.highlightedValues.isEmpty(), is(true));
        assertThat(section.highlightedTitle.isPresent(), is(false));
    }

    @Test
    public void withTitles() {
        final GraphSection emptySection = new GraphSection(Collections.emptyList(),
                                                           Collections.emptyList(),
                                                           Collections.emptyList(),
                                                           OptionalInt.empty());
        assertThat(emptySection.titles.isEmpty(), is(true));

        final GraphSection newSection = emptySection.withTitles(Lists.newArrayList("hello", "goodbye"));
        assertThat(newSection, is(not(equalTo(emptySection))));
        assertThat(newSection.titles.size(), is(equalTo(2)));
    }

    @Test
    public void withHighlightedValues() {
        final GraphSection emptySection = new GraphSection(Collections.emptyList(),
                                                           Collections.emptyList(),
                                                           Collections.emptyList(),
                                                           OptionalInt.empty());
        assertThat(emptySection.highlightedValues.isEmpty(), is(true));

        final GraphSection newSection = emptySection.withHighlightedValues(Lists.newArrayList(2, 1));
        assertThat(newSection, is(not(equalTo(emptySection))));
        assertThat(newSection.highlightedValues.size(), is(equalTo(2)));
    }
}
