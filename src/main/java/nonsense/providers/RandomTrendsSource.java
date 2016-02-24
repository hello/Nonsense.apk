package nonsense.providers;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;

import nonsense.model.Condition;
import nonsense.model.Configuration;
import nonsense.model.trends.Annotation;
import nonsense.model.trends.DataType;
import nonsense.model.trends.Graph;
import nonsense.model.trends.GraphSection;
import nonsense.model.trends.GraphType;
import nonsense.model.trends.TimeScale;
import nonsense.model.trends.Trends;
import nonsense.util.RandomUtil;
import nonsense.util.Requests;

public class RandomTrendsSource implements TrendsSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomTrendsSource.class.getSimpleName());

    private static final int DAYS_IN_WEEK = 7;
    private static final int MIN_AGE_SCORES = 3;
    private static final int MIN_AGE_DURATIONS = 7;
    private static final int MIN_AGE_DEPTHS = 7;

    private final LocalDate today;
    private final Locale locale;
    private final int accountAgeDays;
    private final Random random = new Random();

    public static Factory createFactory(Configuration configuration) {
        return request -> {
            final LocalDate today = Requests.queryParamLocalDate(request, Requests.TODAY, () -> Configuration.getToday(configuration));
            final Locale locale = Requests.queryParamLocale(request, Requests.LOCALE);
            final int accountAge = Requests.queryParamInteger(request, Requests.ACCOUNT_AGE, configuration.getAccountAge());
            return new RandomTrendsSource(today, locale, accountAge);
        };
    }

    public RandomTrendsSource(LocalDate today,
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
        LOGGER.info("Generated {} graphs, available time scales: {}", graphs.size(), availableTimeScales);
        return new Trends(availableTimeScales, graphs);
    }


    //region Generating Graphs

    public Graph generateSleepScoreGraph(TimeScale timeScale) {
        final List<GraphSection> sections;
        final GraphType graphType;
        if (timeScale == TimeScale.LAST_WEEK) {
            sections = generateSleepScoreGraphSections(1);
            graphType = GraphType.GRID;
        } else if (timeScale == TimeScale.LAST_MONTH) {
            sections = generateSleepScoreGraphSections(4);
            graphType = GraphType.GRID;
        } else if (timeScale == TimeScale.LAST_3_MONTHS) {
            sections = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                final LocalDate current = today.minusMonths(i);
                final int valueCount = current.dayOfMonth().getMaximumValue();
                final List<Double> values = generateValues(DataType.SCORES, valueCount, true);
                final String monthName = current.toString("MMM", locale);
                sections.add(0, new GraphSection(values,
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

    public Graph generateSleepDurationGraph(TimeScale timeScale) {
        final List<GraphSection> sections = new ArrayList<>();
        if (timeScale == TimeScale.LAST_WEEK) {
            final List<String> titles = daysOfWeek();
            final OptionalInt highlightedTitle = OptionalInt.of(today.getDayOfWeek());
            sections.add(generateSleepDurationGraphSection(DAYS_IN_WEEK, titles, highlightedTitle));
        } else if (timeScale == TimeScale.LAST_MONTH) {
            if (today.getDayOfMonth() > 1) {
                final LocalDate.Property thisMonth = today.dayOfMonth();
                final int daysInThisMonth = thisMonth.getMaximumValue();
                final String thisMonthName = today.toString("MMM", locale);

                final LocalDate.Property lastMonth = today.minusMonths(1).dayOfMonth();
                final String lastMonthName = lastMonth.getAsShortText(locale);
                final int daysInLastMonth = daysInThisMonth - today.getDayOfMonth();

                sections.add(generateSleepDurationGraphSection(daysInLastMonth,
                                                               Lists.newArrayList(lastMonthName),
                                                               OptionalInt.empty()));
                sections.add(generateSleepDurationGraphSection(daysInThisMonth,
                                                               Lists.newArrayList(thisMonthName),
                                                               OptionalInt.of(0)));
            } else {
                final LocalDate.Property month = today.dayOfMonth();
                final int daysInMonth = month.getMaximumValue();
                final String monthName = today.toString("MMM", locale);
                sections.add(generateSleepDurationGraphSection(daysInMonth,
                                                               Lists.newArrayList(monthName),
                                                               OptionalInt.of(0)));
            }
        } else if (timeScale == TimeScale.LAST_3_MONTHS) {
            for (int i = 0; i < 3; i++) {
                final LocalDate sectionDate = today.minusMonths(i);
                final LocalDate.Property month = sectionDate.dayOfMonth();
                final String name = sectionDate.toString("MMM", locale);

                final int daysInMonth;
                final OptionalInt highlightedTitle;
                if (i == 0) {
                    daysInMonth = today.getDayOfMonth();
                    highlightedTitle = OptionalInt.of(0);
                } else {
                    daysInMonth = month.getMaximumValue();
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

    public Graph generateSleepDepthGraph(TimeScale timeScale) {
        final double min = DataType.PERCENTS.generatedMin;
        final double max = DataType.PERCENTS.generatedMax;

        final double medium = RandomUtil.doubleInRange(random, min, max);
        final double deep = RandomUtil.doubleInRange(random, min, max - medium);
        final double light = max - medium - deep;

        final List<Double> values = Lists.newArrayList(light, medium, deep);
        final List<GraphSection> sections = Lists.newArrayList(GraphSection.newSleepDepth(values));
        return Graph.newSleepDepth(timeScale, sections);
    }

    //endregion


    //region Generating Values

    public List<String> daysOfWeek() {
        return Arrays.stream(DayOfWeek.values())
                     .map(day -> day.getDisplayName(TextStyle.SHORT, locale))
                     .collect(Collectors.toList());
    }

    public List<Annotation> generateAnnotations(DataType dataType) {
        final List<Annotation> annotations = new ArrayList<>();
        annotations.add(generateAnnotation("Weekdays", dataType));
        annotations.add(generateAnnotation("Weekends", dataType));
        annotations.add(generateAnnotation("Average", dataType));
        return annotations;
    }

    public Annotation generateAnnotation(String title, DataType dataType) {
        final double value = RandomUtil.doubleInRange(random, dataType.generatedMin, dataType.generatedMax);
        final Optional<Condition> condition = dataType.getConditionForValue(value);
        return new Annotation(title, value, dataType, condition);
    }

    private List<GraphSection> generateSleepScoreGraphSections(int weekCount) {
        final List<Double> allValues;
        if (weekCount == 1 && accountAgeDays < DAYS_IN_WEEK) {
            allValues = generateValues(DataType.SCORES, accountAgeDays, true);
            final int backFillCount = DAYS_IN_WEEK - accountAgeDays;
            LOGGER.info("Account age less than 1 week, back filling {} value(s)", backFillCount);
            for (int i = 0; i < backFillCount; i++) {
                allValues.add(0, -1.0);
            }
        } else {
            allValues = generateValues(DataType.SCORES, DAYS_IN_WEEK * weekCount, true);
        }

        final int start = today.getDayOfWeek();
        final int end = DAYS_IN_WEEK - start;
        if (end < DAYS_IN_WEEK - 1) {
            nullPadList(allValues, start, end);
        }

        final List<GraphSection> sections = new ArrayList<>();
        for (final List<Double> sectionValues : Lists.partition(allValues, DAYS_IN_WEEK)) {
            final GraphSection section = new GraphSection(sectionValues,
                                                          Collections.emptyList(),
                                                          Collections.emptyList(),
                                                          OptionalInt.of(start - 1));
            sections.add(section);
        }

        sections.set(0, sections.get(0).withTitles(daysOfWeek()));

        final int lastSectionIndex = sections.size() - 1;
        final GraphSection lastSection = sections.get(lastSectionIndex);
        final int firstNullIndex = lastSection.values.indexOf(null);
        if (firstNullIndex > 0) {
            final List<Integer> highlightedValues = Lists.newArrayList(firstNullIndex - 1);
            sections.set(lastSectionIndex, lastSection.withHighlightedValues(highlightedValues));
        } else {
            final List<Integer> highlightedValues = Lists.newArrayList(lastSection.values.size() - 1);
            sections.set(lastSectionIndex, lastSection.withHighlightedValues(highlightedValues));
        }

        return sections;
    }

    private GraphSection generateSleepDurationGraphSection(int count, List<String> titles, OptionalInt highlightedTitle) {
        final List<Double> values = generateValues(DataType.HOURS, count, false);
        final double min = values.stream().min(Double::compare).orElse(0.0);
        final double max = values.stream().max(Double::compare).orElse(0.0);

        final List<Integer> highlightedValues = Lists.newArrayList(values.indexOf(max), values.lastIndexOf(min));
        return new GraphSection(values, titles, highlightedValues, highlightedTitle);
    }

    @VisibleForTesting
    List<Double> generateValues(DataType dataType, int count, boolean includeMissingValues) {
        LOGGER.info("Generating {} values(s) of {}", count, dataType);

        final List<Double> values = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            if (includeMissingValues && random.nextFloat() > 0.75f) {
                values.add(-1.0);
            } else {
                final double value = RandomUtil.doubleInRange(random, dataType.generatedMin,
                                                              dataType.generatedMax);
                if (dataType.roundValues) {
                    values.add(Math.floor(value));
                } else {
                    values.add(value);
                }
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
}
