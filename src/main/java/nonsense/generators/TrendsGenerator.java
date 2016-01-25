package nonsense.generators;

import com.google.common.collect.Lists;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import nonsense.model.Condition;
import nonsense.model.trends.Annotation;
import nonsense.model.trends.DataType;
import nonsense.model.trends.Graph;
import nonsense.model.trends.GraphSection;
import nonsense.model.trends.GraphType;
import nonsense.model.trends.TimeScale;
import nonsense.model.trends.Trends;

public class TrendsGenerator {
    private static final int DAYS_IN_WEEK = 7;

    private final LocalDate today;
    private final Locale locale;
    private final Random random = new Random();

    public TrendsGenerator(LocalDate today, Locale locale) {
        this.today = today;
        this.locale = locale;
    }

    public Trends generateTrends(TimeScale timeScale) {
        final List<TimeScale> availableTimeScales = Lists.newArrayList(TimeScale.LAST_WEEK,
                                                                       TimeScale.LAST_MONTH,
                                                                       TimeScale.LAST_3_MONTHS);
        final List<Graph> graphs = new ArrayList<>();
        graphs.add(generateSleepScoreGraph(timeScale));
        graphs.add(generateSleepDurationGraphSection(timeScale));
        graphs.add(generateSleepDepthGraph(timeScale));
        return new Trends(availableTimeScales, graphs);
    }


    //region Generating Graphs

    Graph generateSleepScoreGraph(TimeScale timeScale) {
        final List<GraphSection> sections;
        final GraphType graphType;
        if (timeScale == TimeScale.LAST_WEEK) {
            sections = generateSleepScoreGraphSections(1);
            graphType = GraphType.GRID;
        } else if (timeScale == TimeScale.LAST_MONTH) {
            final int weekCount = (today.getMonth().length(today.isLeapYear()) / DAYS_IN_WEEK) + 1;
            sections = generateSleepScoreGraphSections(weekCount);
            graphType = GraphType.GRID;
        } else if (timeScale == TimeScale.LAST_3_MONTHS) {
            sections = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                final LocalDate current = today.minusMonths(i);
                final int valueCount = current.getMonth().length(current.isLeapYear());
                final String monthName = current.getMonth().getDisplayName(TextStyle.SHORT, locale);
                final List<Double> values = generateValues(DataType.SCORES, valueCount, true);
                sections.add(new GraphSection(values,
                                              Lists.newArrayList(monthName),
                                              Collections.emptyList(),
                                              OptionalInt.empty()));
            }
            graphType = GraphType.OVERVIEW;
        } else {
            throw new IllegalArgumentException();
        }

        return Graph.newSleepScore(timeScale,
                                   graphType,
                                   sections,
                                   generateAnnotations(DataType.SCORES, Optional.empty()));
    }

    Graph generateSleepDurationGraphSection(TimeScale timeScale) {
        final List<GraphSection> sections = new ArrayList<>();
        if (timeScale == TimeScale.LAST_WEEK) {
            final List<String> titles = daysOfWeek();
            final OptionalInt highlightedTitle = OptionalInt.of(DayOfWeek.from(today).getValue());
            sections.add(generateSleepDurationGraphSection(DAYS_IN_WEEK, titles, highlightedTitle));
        } else if (timeScale == TimeScale.LAST_MONTH) {
            if (today.getDayOfMonth() > 1) {
                final Month thisMonth = today.getMonth();
                final String thisMonthName = thisMonth.getDisplayName(TextStyle.SHORT, locale);
                final int daysInThisMonth = thisMonth.length(today.isLeapYear());

                final Month lastMonth = today.getMonth().minus(1);
                final String lastMonthName = lastMonth.getDisplayName(TextStyle.SHORT, locale);
                final int daysInLastMonth = daysInThisMonth - today.getDayOfMonth();

                sections.add(generateSleepDurationGraphSection(daysInLastMonth,
                                                               Lists.newArrayList(lastMonthName),
                                                               OptionalInt.empty()));
                sections.add(generateSleepDurationGraphSection(daysInThisMonth,
                                                               Lists.newArrayList(thisMonthName),
                                                               OptionalInt.of(0)));
            } else {
                final Month month = today.getMonth();
                final String monthName = month.getDisplayName(TextStyle.SHORT, locale);
                final int daysInMonth = month.length(today.isLeapYear());
                sections.add(generateSleepDurationGraphSection(daysInMonth,
                                                               Lists.newArrayList(monthName),
                                                               OptionalInt.of(0)));
            }
        } else if (timeScale == TimeScale.LAST_3_MONTHS) {
            for (int i = 0; i < 3; i++) {
                final LocalDate sectionDate = today.minusMonths(i);
                final Month month = sectionDate.getMonth();
                final String name = month.getDisplayName(TextStyle.SHORT, locale);

                final int daysInMonth;
                final OptionalInt highlightedTitle;
                if (i == 0) {
                    daysInMonth = today.getDayOfMonth();
                    highlightedTitle = OptionalInt.of(0);
                } else {
                    daysInMonth = month.length(sectionDate.isLeapYear());
                    highlightedTitle = OptionalInt.empty();
                }

                sections.add(0, generateSleepDurationGraphSection(daysInMonth,
                                                                  Lists.newArrayList(name),
                                                                  highlightedTitle));
            }
        }

        return Graph.newSleepDuration(timeScale,
                                      sections,
                                      generateAnnotations(DataType.HOURS, Optional.empty()));
    }

    Graph generateSleepDepthGraph(TimeScale timeScale) {
        final List<Double> values = generateValues(DataType.PERCENTS, 3, false);
        final List<GraphSection> sections = Lists.newArrayList(GraphSection.newSleepDepth(values));
        return Graph.newSleepDepth(timeScale, sections);
    }

    //endregion


    //region Generating Values

    List<String> daysOfWeek() {
        return Arrays.stream(DayOfWeek.values())
                     .map(day -> day.getDisplayName(TextStyle.SHORT, locale))
                     .collect(Collectors.toList());
    }

    List<Annotation> generateAnnotations(DataType dataType,
                                         Optional<Function<Double, Condition>> conditionProvider) {
        final List<Annotation> annotations = new ArrayList<>();
        annotations.add(generateAnnotation("Weekdays", dataType, conditionProvider));
        annotations.add(generateAnnotation("Weekends", dataType, conditionProvider));
        annotations.add(generateAnnotation("Average", dataType, conditionProvider));
        return annotations;
    }

    Annotation generateAnnotation(String title,
                                  DataType dataType,
                                  Optional<Function<Double, Condition>> conditionProvider) {
        final double value = randomValue(dataType.generatedMin, dataType.generatedMax);
        final Optional<Condition> condition = conditionProvider.map(f -> f.apply(value));
        return new Annotation(title, value, dataType, condition);
    }

    List<GraphSection> generateSleepScoreGraphSections(int weekCount) {
        final List<Double> allValues = generateValues(DataType.SCORES, DAYS_IN_WEEK * weekCount, true);
        final int start = DayOfWeek.from(today).getValue() - 1;
        final int end = DAYS_IN_WEEK - start;
        pad(allValues, start, end);

        final List<GraphSection> sections = new ArrayList<>();
        for (final List<Double> sectionValues : Lists.partition(allValues, DAYS_IN_WEEK)) {
            final GraphSection section = new GraphSection(sectionValues,
                                                          Collections.emptyList(),
                                                          Collections.emptyList(),
                                                          OptionalInt.of(start));
            sections.add(section);
        }

        sections.set(0, sections.get(0).withTitles(daysOfWeek()));

        return sections;
    }

    GraphSection generateSleepDurationGraphSection(int count, List<String> titles, OptionalInt highlightedTitle) {
        final List<Double> values = generateValues(DataType.HOURS, count, true);
        final double min = values.stream().min(Double::compare).orElse(0.0);
        final double max = values.stream().max(Double::compare).orElse(0.0);

        final List<Integer> highlightedValues = Lists.newArrayList(values.indexOf(min), values.lastIndexOf(max));
        return new GraphSection(values, titles, highlightedValues, highlightedTitle);
    }

    List<Double> generateValues(DataType dataType, int count, boolean round) {
        final List<Double> values = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final double value = randomValue(dataType.generatedMin,
                                             dataType.generatedMax);
            if (round) {
                values.add(Math.floor(value));
            } else {
                values.add(value);
            }
        }
        return values;
    }

    void pad(List<Double> values, int startCount, int endCount) {
        for (int i = 0; i < startCount; i++) {
            values.add(0, null);
        }

        for (int i = 0; i < endCount; i++) {
            values.add(null);
        }
    }

    double randomValue(double min, double max) {
        return min + (random.nextDouble() * (max - min));
    }

    //endregion
}
