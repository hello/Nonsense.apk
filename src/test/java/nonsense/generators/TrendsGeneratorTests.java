package nonsense.generators;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nonsense.model.trends.Annotation;
import nonsense.model.trends.DataType;
import nonsense.model.trends.Graph;
import nonsense.model.trends.GraphSection;
import nonsense.model.trends.GraphType;
import nonsense.model.trends.TimeScale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class TrendsGeneratorTests {
    private TrendsGenerator generator;

    @Before
    public void setUp() {
        this.generator = new TrendsGenerator.Builder()
                .setLocale(Locale.US)
                .setToday(LocalDate.of(2016, 1, 25))
                .build();
    }

    @Test
    public void generateSleepScoreGraph() {
        final Graph lastWeek = generator.generateSleepScoreGraph(TimeScale.LAST_WEEK);
        assertThat(lastWeek.graphType, is(equalTo(GraphType.GRID)));
        assertThat(lastWeek.sections.size(), is(equalTo(1)));

        final Graph lastMonth = generator.generateSleepScoreGraph(TimeScale.LAST_MONTH);
        assertThat(lastMonth.graphType, is(equalTo(GraphType.GRID)));
        assertThat(lastMonth.sections.size(), is(equalTo(5)));

        final Graph last3Months = generator.generateSleepScoreGraph(TimeScale.LAST_3_MONTHS);
        assertThat(last3Months.graphType, is(equalTo(GraphType.OVERVIEW)));
        assertThat(last3Months.sections.size(), is(equalTo(3)));
    }

    @Test
    public void generateSleepDurationGraph() {
        final Graph lastWeek = generator.generateSleepDurationGraph(TimeScale.LAST_WEEK);
        assertThat(lastWeek.graphType, is(equalTo(GraphType.BAR)));
        assertThat(lastWeek.sections.size(), is(equalTo(1)));
        assertThat(getHighlightedValueCount(lastWeek.sections), is(equalTo(2)));

        final Graph lastMonth = generator.generateSleepDurationGraph(TimeScale.LAST_MONTH);
        assertThat(lastMonth.graphType, is(equalTo(GraphType.BAR)));
        assertThat(lastMonth.sections.size(), is(equalTo(2)));
        assertThat(getHighlightedValueCount(lastMonth.sections), is(equalTo(2)));

        final Graph last3Months = generator.generateSleepDurationGraph(TimeScale.LAST_3_MONTHS);
        assertThat(last3Months.graphType, is(equalTo(GraphType.BAR)));
        assertThat(last3Months.sections.size(), is(equalTo(3)));
        assertThat(getHighlightedValueCount(last3Months.sections), is(equalTo(2)));
    }

    @Test
    public void generateSleepDepthGraph() {
        final Graph graph = generator.generateSleepDepthGraph(TimeScale.LAST_WEEK);
        assertThat(graph.sections.size(), is(equalTo(1)));
        assertThat(graph.sections.get(0).values.size(), is(equalTo(3)));
        assertThat(graph.minValue, is(equalTo(0.0)));
        assertThat(graph.maxValue, is(equalTo(1.0)));
        assertThat(graph.dataType, is(equalTo(DataType.PERCENTS)));
        assertThat(graph.graphType, is(equalTo(GraphType.BUBBLES)));
    }

    @Test
    public void daysOfWeek() {
        final List<String> weekdayNames = generator.daysOfWeek();
        assertThat(weekdayNames.size(), is(equalTo(7)));
        assertThat(weekdayNames, hasItem("Mon"));
        assertThat(weekdayNames, hasItem("Tue"));
        assertThat(weekdayNames, hasItem("Wed"));
        assertThat(weekdayNames, hasItem("Thu"));
        assertThat(weekdayNames, hasItem("Fri"));
        assertThat(weekdayNames, hasItem("Sat"));
        assertThat(weekdayNames, hasItem("Sun"));
    }

    @Test
    public void generateAnnotations() {
        final List<Annotation> annotations = generator.generateAnnotations(DataType.SCORES);
        assertThat(annotations.size(), is(equalTo(3)));

        final Set<String> titles = annotations.parallelStream()
                                              .map(a -> a.title)
                                              .collect(Collectors.toSet());
        assertThat(titles, hasItem("Weekdays"));
        assertThat(titles, hasItem("Weekends"));
        assertThat(titles, hasItem("Average"));
    }

    @Test
    public void generateAnnotation() {
        for (final DataType dataType : DataType.values()) {
            final Annotation annotation = generator.generateAnnotation("Fake", dataType);
            assertThat(annotation.title, is(equalTo("Fake")));
            assertThat(annotation.value >= dataType.generatedMin, is(true));
            assertThat(annotation.value <= dataType.generatedMax, is(true));
            assertThat(annotation.dataType, is(equalTo(dataType)));
        }
    }

    @Test
    public void generateValues() {
        for (final DataType dataType : DataType.values()) {
            final List<Double> values = generator.generateValues(dataType, 10);
            assertThat(values.size(), is(equalTo(10)));
            for (final double value : values) {
                assertThat(value >= dataType.generatedMin, is(true));
                assertThat(value <= dataType.generatedMax, is(true));
            }
        }
    }

    @Test
    public void pad() {
        final List<Double> values = new ArrayList<>();
        values.add(42.0);

        TrendsGenerator.nullPadList(values, 5, 0);
        assertThat(values.size(), is(equalTo(6)));

        TrendsGenerator.nullPadList(values, 0, 5);
        assertThat(values.size(), is(equalTo(11)));

        TrendsGenerator.nullPadList(values, 1, 1);
        assertThat(values.size(), is(equalTo(13)));

        assertThat(values, is(equalTo(Lists.newArrayList(null, null, null, null, null, null,
                                                         42.0,
                                                         null, null, null, null, null, null))));
    }

    @Test
    public void randomValue() {
        for (int i = 0; i < 1000; i++) {
            final double value = generator.randomValue(-10, 10);
            assertThat(value > -10.0, is(true));
            assertThat(value < 10.0, is(true));
        }
    }


    private static int getHighlightedValueCount(List<GraphSection> sections) {
        return sections.stream()
                       .flatMap(s -> s.highlightedValues.stream())
                       .collect(Collectors.counting())
                       .intValue();
    }
}
