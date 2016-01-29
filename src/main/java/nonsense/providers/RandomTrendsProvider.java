package nonsense.providers;

import com.google.common.annotations.VisibleForTesting;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;

import nonsense.model.Condition;
import nonsense.model.trends.Annotation;
import nonsense.model.trends.DataType;
import nonsense.model.trends.Graph;
import nonsense.model.trends.GraphSection;
import nonsense.model.trends.GraphType;
import nonsense.model.trends.TimeScale;
import nonsense.model.trends.Trends;
import nonsense.util.RandomHelper;

public class RandomTrendsProvider implements TrendsProvider {
    private static final Logger LOGGER = Logger.getLogger(RandomTrendsProvider.class.getSimpleName());

    private static final int DAYS_IN_WEEK = 7;
    private static final int MIN_AGE_SCORES = 3;
    private static final int MIN_AGE_DURATIONS = 7;
    private static final int MIN_AGE_DEPTHS = 7;

    private final LocalDate today;
    private final Locale locale;
    private final int accountAgeDays;
    private final Random random = new Random();

    RandomTrendsProvider(LocalDate today,
                         Locale locale,
                         int accountAgeDays) {
        this.today = today;
        this.locale = locale;
        this.accountAgeDays = accountAgeDays;
    }

    @Override
    public Trends getTrendsForTimeScale(TimeScale timeScale) {
        final List<TimeScale> availableTimeScales = TimeScale.fromAccountAge(accountAgeDays);
        final List<Graph> graphs = new ArrayList<>();
        if (accountAgeDays >= MIN_AGE_SCORES) {
            graphs.add(generateSleepScoreGraph(timeScale));
        }
        if (accountAgeDays >= MIN_AGE_DURATIONS) {
            graphs.add(generateSleepDurationGraph(timeScale));
        }
        if (accountAgeDays >= MIN_AGE_DEPTHS) {
            graphs.add(generateSleepDepthGraph(timeScale));
        }
        LOGGER.info("Generated " + graphs.size() + " graphs, available time scales: " + availableTimeScales);
        return new Trends(availableTimeScales, graphs);
    }


    //region Generating Graphs

    @VisibleForTesting
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
                final List<Double> values = generateValues(DataType.SCORES, valueCount);
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
                                   generateAnnotations(DataType.SCORES));
    }

    @VisibleForTesting
    Graph generateSleepDurationGraph(TimeScale timeScale) {
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

        if (sections.size() > 1) {
            for (int i = 1, length = sections.size(); i < length; i++) {
                sections.get(i).highlightedValues.clear();
            }
        }

        return Graph.newSleepDuration(timeScale,
                                      sections,
                                      generateAnnotations(DataType.HOURS));
    }

    @VisibleForTesting
    Graph generateSleepDepthGraph(TimeScale timeScale) {
        final List<Double> values = generateValues(DataType.PERCENTS, 3);
        final List<GraphSection> sections = Lists.newArrayList(GraphSection.newSleepDepth(values));
        return Graph.newSleepDepth(timeScale, sections);
    }

    //endregion


    //region Generating Values

    @VisibleForTesting
    List<String> daysOfWeek() {
        return Arrays.stream(DayOfWeek.values())
                     .map(day -> day.getDisplayName(TextStyle.SHORT, locale))
                     .collect(Collectors.toList());
    }

    @VisibleForTesting
    List<Annotation> generateAnnotations(DataType dataType) {
        final List<Annotation> annotations = new ArrayList<>();
        annotations.add(generateAnnotation("Weekdays", dataType));
        annotations.add(generateAnnotation("Weekends", dataType));
        annotations.add(generateAnnotation("Average", dataType));
        return annotations;
    }

    @VisibleForTesting
    Annotation generateAnnotation(String title, DataType dataType) {
        final double value = RandomHelper.doubleInRange(random, dataType.generatedMin, dataType.generatedMax);
        final Optional<Condition> condition = dataType.getConditionForValue(value);
        return new Annotation(title, value, dataType, condition);
    }

    private List<GraphSection> generateSleepScoreGraphSections(int weekCount) {
        final List<Double> allValues;
        if (weekCount == 1 && accountAgeDays < DAYS_IN_WEEK) {
            allValues = generateValues(DataType.SCORES, accountAgeDays);
            final int backFillCount = DAYS_IN_WEEK - accountAgeDays;
            LOGGER.info("Account age less than 1 week, back filling " + backFillCount + " value(s)");
            for (int i = 0; i < backFillCount; i++) {
                allValues.add(0, -1.0);
            }
        } else {
            allValues = generateValues(DataType.SCORES, DAYS_IN_WEEK * weekCount);
        }

        final int start = DayOfWeek.from(today).getValue() - 1;
        final int end = DAYS_IN_WEEK - start;
        if (end < DAYS_IN_WEEK) {
            nullPadList(allValues, start, end);
        }

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

    private GraphSection generateSleepDurationGraphSection(int count, List<String> titles, OptionalInt highlightedTitle) {
        final List<Double> values = generateValues(DataType.HOURS, count);
        final double min = values.stream().min(Double::compare).orElse(0.0);
        final double max = values.stream().max(Double::compare).orElse(0.0);

        final List<Integer> highlightedValues = Lists.newArrayList(values.indexOf(max), values.lastIndexOf(min));
        return new GraphSection(values, titles, highlightedValues, highlightedTitle);
    }

    @VisibleForTesting
    List<Double> generateValues(DataType dataType, int count) {
        LOGGER.info("Generating " + count + " values(s) of " + dataType);

        final List<Double> values = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final double value = RandomHelper.doubleInRange(random, dataType.generatedMin,
                                                            dataType.generatedMax);
            if (dataType.roundValues) {
                values.add(Math.floor(value));
            } else {
                values.add(value);
            }
        }
        return values;
    }

    @VisibleForTesting
    static void nullPadList(List<Double> values, int startCount, int endCount) {
        for (int i = 0; i < startCount; i++) {
            values.add(0, null);
        }

        for (int i = 0; i < endCount; i++) {
            values.add(null);
        }
    }

    //endregion


    public static class Builder {
        private LocalDate today = LocalDate.now().minusDays(1);
        private Locale locale = Locale.getDefault();
        private int accountAgeDays = 90;

        public Builder setToday(LocalDate today) {
            this.today = today;
            return this;
        }

        public Builder setLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public Builder setAccountAgeDays(int accountAgeDays) {
            this.accountAgeDays = accountAgeDays;
            return this;
        }

        public RandomTrendsProvider build() {
            return new RandomTrendsProvider(today, locale, accountAgeDays);
        }
    }
}
