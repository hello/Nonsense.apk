package nonsense.model.timeline;

import com.google.common.collect.Lists;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

import nonsense.model.Condition;
import nonsense.model.timeline.v1.TimelineV1Insight;
import nonsense.model.timeline.v1.TimelineV1Segment;
import nonsense.model.timeline.v1.TimelineV1Statistics;
import nonsense.model.timeline.v2.TimelineV2Event;
import nonsense.model.timeline.v2.TimelineV2Metric;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class VersionTranslatorTest {
    private static final int TZ_OFFSET = ZoneOffset.UTC.getTotalSeconds() * 1000;

    @Test
    public void translateStatisticsToMetrics() {
        final TimelineV1Statistics completeStatistics = new TimelineV1Statistics(OptionalLong.of(8),
                                                                                 OptionalLong.of(1),
                                                                                 OptionalLong.of(2),
                                                                                 OptionalLong.of(9));
        final List<TimelineV2Metric> completeMetrics = VersionTranslator.translateStatisticsToMetrics(Optional.of(completeStatistics));
        assertThat(completeMetrics.size(), is(equalTo(4)));

        final List<TimelineV2Metric.Name> completeMetricNames = completeMetrics.stream()
                                                                               .map(m -> m.name)
                                                                               .collect(Collectors.toList());
        assertThat(completeMetricNames, hasItems(TimelineV2Metric.Name.SOUND_SLEEP,
                                                 TimelineV2Metric.Name.TOTAL_SLEEP,
                                                 TimelineV2Metric.Name.TIME_TO_SLEEP,
                                                 TimelineV2Metric.Name.TIMES_AWAKE));

        final TimelineV1Statistics incompleteStatistics = new TimelineV1Statistics(OptionalLong.of(8),
                                                                                   OptionalLong.empty(),
                                                                                   OptionalLong.of(2),
                                                                                   OptionalLong.empty());
        final List<TimelineV2Metric> incompleteMetrics = VersionTranslator.translateStatisticsToMetrics(Optional.of(incompleteStatistics));
        assertThat(incompleteMetrics.size(), is(equalTo(2)));

        final List<TimelineV2Metric.Name> incompleteMetricNames = incompleteMetrics.stream()
                                                                                   .map(m -> m.name)
                                                                                   .collect(Collectors.toList());
        assertThat(incompleteMetricNames, hasItems(TimelineV2Metric.Name.SOUND_SLEEP,
                                                   TimelineV2Metric.Name.TIMES_AWAKE));


        final List<TimelineV2Metric> noMetrics = VersionTranslator.translateStatisticsToMetrics(Optional.empty());
        assertThat(noMetrics.size(), is(equalTo(0)));
    }

    @Test
    public void translateSegmentsToMetrics() {
        final LocalDateTime now = LocalDateTime.now();
        final TimelineV1Segment fellAsleep = new TimelineV1Segment(100, TimelineV1Segment.Type.SLEEP,
                                                                   0, Optional.empty(),
                                                                   TZ_OFFSET, 50,
                                                                   now.minusHours(10L).toEpochSecond(ZoneOffset.UTC) * 1000L,
                                                                   Collections.emptyList(),
                                                                   Optional.empty());
        final TimelineV1Segment wokeUp = new TimelineV1Segment(100, TimelineV1Segment.Type.WAKE_UP,
                                                               0, Optional.empty(),
                                                               TZ_OFFSET, 50,
                                                               now.minusHours(10L).toEpochSecond(ZoneOffset.UTC) * 1000L,
                                                               Collections.emptyList(),
                                                               Optional.empty());

        final List<TimelineV2Metric> metrics = VersionTranslator.translateSegmentsToMetrics(Lists.newArrayList(fellAsleep, wokeUp));
        assertThat(metrics.size(), is(equalTo(2)));

        final TimelineV2Metric fellAsleepMetric = metrics.get(0);
        assertThat(fellAsleepMetric.name, is(equalTo(TimelineV2Metric.Name.FELL_ASLEEP)));
        assertThat(fellAsleepMetric.unit, is(equalTo(TimelineV2Metric.Unit.TIMESTAMP)));
        assertThat(fellAsleepMetric.condition, is(equalTo(Condition.IDEAL)));
        assertThat(fellAsleepMetric.value, is(not(equalTo(OptionalLong.empty()))));

        final TimelineV2Metric wokeUpMetric = metrics.get(1);
        assertThat(wokeUpMetric.name, is(equalTo(TimelineV2Metric.Name.WOKE_UP)));
        assertThat(wokeUpMetric.unit, is(equalTo(TimelineV2Metric.Unit.TIMESTAMP)));
        assertThat(wokeUpMetric.condition, is(equalTo(Condition.IDEAL)));
        assertThat(wokeUpMetric.value, is(not(equalTo(OptionalLong.empty()))));
    }

    @Test
    public void translateInsightsToMetrics() {
        final List<TimelineV1Insight> insights = new ArrayList<>();
        insights.add(new TimelineV1Insight(Condition.ALERT,
                                           "Consider moving somewhere better",
                                           TimelineV1Insight.Sensor.PARTICULATES));
        insights.add(new TimelineV1Insight(Condition.WARNING,
                                           "It's awfully warm in here...",
                                           TimelineV1Insight.Sensor.TEMPERATURE));

        final List<TimelineV2Metric> metrics = VersionTranslator.translateInsightsToMetrics(insights);
        assertThat(metrics.size(), is(equalTo(2)));

        final TimelineV2Metric particulates = metrics.get(0);
        assertThat(particulates.name, is(equalTo(TimelineV2Metric.Name.PARTICULATES)));
        assertThat(particulates.condition, is(equalTo(Condition.ALERT)));
        assertThat(particulates.unit, is(equalTo(TimelineV2Metric.Unit.CONDITION)));
        assertThat(particulates.value, is(OptionalLong.empty()));

        final TimelineV2Metric temperature = metrics.get(1);
        assertThat(temperature.name, is(equalTo(TimelineV2Metric.Name.TEMPERATURE)));
        assertThat(temperature.condition, is(equalTo(Condition.WARNING)));
        assertThat(temperature.unit, is(equalTo(TimelineV2Metric.Unit.CONDITION)));
        assertThat(temperature.value, is(OptionalLong.empty()));
    }

    @Test
    public void translateSegmentToEvent() {
        final long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000L;
        final TimelineV1Segment awakeInBed = new TimelineV1Segment(100, TimelineV1Segment.Type.NONE,
                                                                   0, Optional.empty(),
                                                                   TZ_OFFSET, 50,
                                                                   timestamp,
                                                                   Collections.emptyList(),
                                                                   Optional.empty());
        final TimelineV2Event awakeInBedEvent = VersionTranslator.translateSegmentToEvent(awakeInBed);
        assertThat(awakeInBedEvent.sleepState, is(equalTo(TimelineV2Event.SleepState.AWAKE)));
        assertThat(awakeInBedEvent.type, is(equalTo(TimelineV2Event.Type.IN_BED)));

        final TimelineV1Segment normalSegment = new TimelineV1Segment(60, TimelineV1Segment.Type.WAKE_UP,
                                                                      0, Optional.of("You woke up"),
                                                                      TZ_OFFSET, 50, timestamp,
                                                                      Collections.emptyList(),
                                                                      Optional.empty());
        final TimelineV2Event normalEvent = VersionTranslator.translateSegmentToEvent(normalSegment);
        assertThat(normalEvent.type, is(equalTo(TimelineV2Event.Type.WOKE_UP)));
        assertThat(normalEvent.sleepState, is(equalTo(TimelineV2Event.SleepState.MEDIUM)));
        assertThat(normalEvent.sleepDepth, is(equalTo(50)));
        assertThat(normalEvent.durationMillis, is(equalTo(60 * 1000L)));
        assertThat(normalEvent.message, is(equalTo(Optional.of("You woke up"))));
        assertThat(normalEvent.timestamp, is(equalTo(normalSegment.timestamp)));
        assertThat(normalEvent.timezoneOffset, is(equalTo(TZ_OFFSET)));
        assertThat(normalEvent.validActions, hasItems(TimelineV2Event.Action.ADJUST_TIME,
                                                      TimelineV2Event.Action.VERIFY,
                                                      TimelineV2Event.Action.INCORRECT));
    }

    @Test
    public void translateSensorToName() {
        assertThat(VersionTranslator.translateSensorToName(TimelineV1Insight.Sensor.HUMIDITY),
                   is(equalTo(TimelineV2Metric.Name.HUMIDITY)));
        assertThat(VersionTranslator.translateSensorToName(TimelineV1Insight.Sensor.LIGHT),
                   is(equalTo(TimelineV2Metric.Name.LIGHT)));
        assertThat(VersionTranslator.translateSensorToName(TimelineV1Insight.Sensor.PARTICULATES),
                   is(equalTo(TimelineV2Metric.Name.PARTICULATES)));
        assertThat(VersionTranslator.translateSensorToName(TimelineV1Insight.Sensor.SOUND),
                   is(equalTo(TimelineV2Metric.Name.SOUND)));
        assertThat(VersionTranslator.translateSensorToName(TimelineV1Insight.Sensor.TEMPERATURE),
                   is(equalTo(TimelineV2Metric.Name.TEMPERATURE)));
    }

    @Test
    public void translateEventType() {
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.ALARM),
                   is(equalTo(TimelineV2Event.Type.ALARM_RANG)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.IN_BED),
                   is(equalTo(TimelineV2Event.Type.GOT_IN_BED)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.LIGHT),
                   is(equalTo(TimelineV2Event.Type.LIGHT)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.LIGHTS_OUT),
                   is(equalTo(TimelineV2Event.Type.LIGHTS_OUT)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.MOTION),
                   is(equalTo(TimelineV2Event.Type.GENERIC_MOTION)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.NOISE),
                   is(equalTo(TimelineV2Event.Type.GENERIC_SOUND)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.OUT_OF_BED),
                   is(equalTo(TimelineV2Event.Type.GOT_OUT_OF_BED)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.PARTNER_MOTION),
                   is(equalTo(TimelineV2Event.Type.PARTNER_MOTION)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.SLEEP),
                   is(equalTo(TimelineV2Event.Type.FELL_ASLEEP)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.SUNRISE),
                   is(equalTo(TimelineV2Event.Type.SUNRISE)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.SUNSET),
                   is(equalTo(TimelineV2Event.Type.SUNSET)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.WAKE_UP),
                   is(equalTo(TimelineV2Event.Type.WOKE_UP)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.SLEEPING),
                   is(equalTo(TimelineV2Event.Type.IN_BED)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.SNORING),
                   is(equalTo(TimelineV2Event.Type.SNORED)));
        assertThat(VersionTranslator.translateEventType(TimelineV1Segment.Type.SLEEP_TALK),
                   is(equalTo(TimelineV2Event.Type.SLEEP_TALKED)));
    }
}
