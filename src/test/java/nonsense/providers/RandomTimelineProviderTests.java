package nonsense.providers;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

import nonsense.model.Condition;
import nonsense.model.timeline.v1.TimelineV1Insight;
import nonsense.model.timeline.v1.TimelineV1Segment;
import nonsense.model.timeline.v1.TimelineV1Statistics;

import static nonsense.NonsenseMatchers.emptyString;
import static nonsense.NonsenseMatchers.greaterThanOrEqual;
import static nonsense.NonsenseMatchers.lessThanOrEqual;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class RandomTimelineProviderTests {
    private final ZoneOffset timeZone = ZoneOffset.UTC;
    private RandomTimelineSource provider;

    @Before
    public void setUp() {
        this.provider = new RandomTimelineSource(timeZone, Locale.US);
    }

    @Test
    public void generateDurationSeconds() {
        final int min = 2 * 60;
        final int max = 12 * 60;

        for (int i = 0; i < 100; i++) {
            assertThat(provider.generateDurationSeconds(),
                       is(allOf(greaterThanOrEqual(min), lessThanOrEqual(max))));
        }
    }

    @Test
    public void generateStartTimestamp() {
        final LocalDate now = LocalDate.of(2016, 1, 29);
        final LocalDateTime min = now.atStartOfDay().plusHours(20);
        final LocalDateTime max = now.atStartOfDay().plusHours(26);

        for (int i = 0; i < 100; i++) {
            assertThat(provider.generateStartTimestamp(now),
                       is(allOf(greaterThanOrEqual(min), lessThanOrEqual(max))));
        }
    }

    @Test
    public void generateTimelineMessage() {
        final TimelineV1Statistics empty = TimelineV1Statistics.empty();
        assertThat(provider.generateTimelineMessage(empty),
                   is(equalTo("You did not sleep")));

        final TimelineV1Statistics withTotalSleep = new TimelineV1Statistics(OptionalLong.empty(),
                                                                             OptionalLong.empty(),
                                                                             OptionalLong.empty(),
                                                                             OptionalLong.of(60 * 8L));
        assertThat(provider.generateTimelineMessage(withTotalSleep),
                   is(equalTo("You were asleep for **8 hours**")));

        final TimelineV1Statistics withAllSleepStats = new TimelineV1Statistics(OptionalLong.of(60 * 2L),
                                                                                OptionalLong.empty(),
                                                                                OptionalLong.empty(),
                                                                                OptionalLong.of(60 * 8L));
        assertThat(provider.generateTimelineMessage(withAllSleepStats),
                   is(equalTo("You were asleep for **8 hours**, and sleeping soundly for **2 hours**")));
    }

    @Test
    public void generateStatistics() {
        final long minutesInSoundSleep = 60 * 8;
        final long timesAwake = 3;
        final long minutesInSleepTotal = 60 * 9;

        final TimelineV1Statistics statistics = provider.generateStatistics(minutesInSoundSleep,
                                                                            timesAwake,
                                                                            minutesInSleepTotal);

        assertThat(statistics.soundSleep, is(not(equalTo(OptionalLong.empty()))));
        assertThat(statistics.soundSleep.getAsLong(), is(equalTo(minutesInSoundSleep)));

        assertThat(statistics.timesAwake, is(not(equalTo(OptionalLong.empty()))));
        assertThat(statistics.timesAwake.getAsLong(), is(equalTo(timesAwake)));

        assertThat(statistics.totalSleep, is(not(equalTo(OptionalLong.empty()))));
        assertThat(statistics.totalSleep.getAsLong(), is(equalTo(minutesInSleepTotal)));

        assertThat(statistics.timeToSleep, is(not(equalTo(OptionalLong.empty()))));
        assertThat(statistics.timeToSleep.getAsLong(), is(greaterThanOrEqual(0L)));
        assertThat(statistics.timeToSleep.getAsLong(), is(lessThanOrEqual(60L)));
    }

    @Test
    public void generateInsights() {
        final List<TimelineV1Insight.Sensor> insightSensors = provider.generateInsights()
                                                                      .stream()
                                                                      .map(i -> i.sensor)
                                                                      .collect(Collectors.toList());
        for (final TimelineV1Insight.Sensor sensor : TimelineV1Insight.Sensor.values()) {
            if (sensor == TimelineV1Insight.Sensor.UNKNOWN) {
                continue;
            }

            assertThat(insightSensors, hasItem(sensor));
        }

    }

    @Test
    public void generateInsight() {
        final TimelineV1Insight insight = provider.generateInsight(TimelineV1Insight.Sensor.TEMPERATURE);
        assertThat(insight.sensor, is(equalTo(TimelineV1Insight.Sensor.TEMPERATURE)));
        assertThat(insight.condition, is(not(equalTo(Condition.UNKNOWN))));
        assertThat(insight.message, is(not(emptyString())));
    }

    @Test
    public void generateSegment() {
        final LocalDateTime timestamp = LocalDateTime.now();
        final TimelineV1Segment notSleepingSegment = provider.generateSegment(timestamp, TimelineV1Segment.Type.IN_BED);
        assertThat(notSleepingSegment.duration, is(allOf(greaterThanOrEqual(0L), lessThanOrEqual(60L))));
        assertThat(notSleepingSegment.eventType, is(equalTo(TimelineV1Segment.Type.IN_BED)));
        assertThat(notSleepingSegment.timestamp, is(equalTo(timestamp)));
        assertThat(notSleepingSegment.sleepDepth, is(allOf(greaterThanOrEqual(0), lessThanOrEqual(100))));
        assertThat(notSleepingSegment.message, is(not(Optional.empty())));
        assertThat(notSleepingSegment.message.get(), is(not(emptyString())));

        final TimelineV1Segment sleepingSegment = provider.generateSegment(timestamp, TimelineV1Segment.Type.SLEEPING);
        assertThat(sleepingSegment.message, is(equalTo(Optional.empty())));
        assertThat(sleepingSegment.eventType, is(equalTo(TimelineV1Segment.Type.SLEEPING)));
    }

    @Test
    public void generateSegmentType() {
        for (int i = 0; i < 100; i++) {
            assertThat(provider.generateSegmentType(),
                       is(not(equalTo(TimelineV1Segment.Type.NONE))));
        }
    }
}
