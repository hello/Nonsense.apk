package nonsense.model.timeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import nonsense.model.Condition;
import nonsense.model.timeline.v1.TimelineV1;
import nonsense.model.timeline.v1.TimelineV1Insight;
import nonsense.model.timeline.v1.TimelineV1Segment;
import nonsense.model.timeline.v1.TimelineV1Statistics;
import nonsense.model.timeline.v2.ScoreCondition;
import nonsense.model.timeline.v2.TimelineV2;
import nonsense.model.timeline.v2.TimelineV2Event;
import nonsense.model.timeline.v2.TimelineV2Metric;

public class VersionTranslator {
    public static TimelineV2 translate(TimelineV1 timeline) {
        final List<TimelineV2Event> segments = timeline.segments.stream()
                                                                .map(VersionTranslator::translateSegmentToEvent)
                                                                .collect(Collectors.toList());
        final List<TimelineV2Metric> metrics = translateTimelineInsightsToMetrics(timeline);

        return new TimelineV2(OptionalInt.of(timeline.score),
                              ScoreCondition.fromScore(timeline.score),
                              timeline.message,
                              timeline.date,
                              segments,
                              metrics);
    }


    //region Translating Metrics

    public static List<TimelineV2Metric> translateStatisticsToMetrics(Optional<TimelineV1Statistics> maybeStatistics) {
        final List<TimelineV2Metric> metrics = new ArrayList<>();
        maybeStatistics.ifPresent(statistics -> {
            metrics.add(new TimelineV2Metric(TimelineV2Metric.Name.TOTAL_SLEEP, statistics.totalSleep,
                                             TimelineV2Metric.Unit.MINUTES, Condition.IDEAL));
            metrics.add(new TimelineV2Metric(TimelineV2Metric.Name.SOUND_SLEEP, statistics.soundSleep,
                                             TimelineV2Metric.Unit.MINUTES, Condition.IDEAL));
            metrics.add(new TimelineV2Metric(TimelineV2Metric.Name.TIME_TO_SLEEP, statistics.timeToSleep,
                                             TimelineV2Metric.Unit.MINUTES, Condition.IDEAL));
            metrics.add(new TimelineV2Metric(TimelineV2Metric.Name.TIMES_AWAKE, statistics.timesAwake,
                                             TimelineV2Metric.Unit.QUANTITY, Condition.IDEAL));

            metrics.removeIf(metric -> !metric.value.isPresent());
        });
        return metrics;
    }

    public static List<TimelineV2Metric> translateSegmentsToMetrics(List<TimelineV1Segment> segments) {
        final List<TimelineV2Metric> metrics = new ArrayList<>();

        final OptionalLong fellAsleep = segments.stream()
                                                .filter(segment -> segment.eventType == TimelineV1Segment.Type.SLEEP)
                                                .mapToLong(segment -> segment.timestamp)
                                                .findFirst();
        fellAsleep.ifPresent(timestamp -> {
            metrics.add(new TimelineV2Metric(TimelineV2Metric.Name.FELL_ASLEEP, OptionalLong.of(timestamp),
                                             TimelineV2Metric.Unit.TIMESTAMP, Condition.IDEAL));
        });

        final OptionalLong wokeUp = segments.stream()
                                            .filter(segment -> segment.eventType == TimelineV1Segment.Type.WAKE_UP)
                                            .mapToLong(segment -> segment.timestamp)
                                            .findFirst();
        wokeUp.ifPresent(timestamp -> {
            metrics.add(new TimelineV2Metric(TimelineV2Metric.Name.WOKE_UP, OptionalLong.of(timestamp),
                                             TimelineV2Metric.Unit.TIMESTAMP, Condition.IDEAL));
        });

        return metrics;
    }

    public static List<TimelineV2Metric> translateInsightsToMetrics(List<TimelineV1Insight> insights) {
        return insights.stream()
                       .map(insight -> {
                           final TimelineV2Metric.Name name = translateSensorToName(insight.sensor);
                           return new TimelineV2Metric(name, OptionalLong.empty(),
                                                       TimelineV2Metric.Unit.CONDITION,
                                                       insight.condition);
                       })
                       .collect(Collectors.toList());
    }

    public static List<TimelineV2Metric> translateTimelineInsightsToMetrics(TimelineV1 timelineV1) {
        final List<TimelineV2Metric> statisticMetrics = translateStatisticsToMetrics(timelineV1.statistics);
        final List<TimelineV2Metric> segmentMetrics = translateSegmentsToMetrics(timelineV1.segments);
        final List<TimelineV2Metric> insightMetrics = translateInsightsToMetrics(timelineV1.insights);

        final List<TimelineV2Metric> metrics = new ArrayList<>(statisticMetrics.size() + segmentMetrics.size() + insightMetrics.size());
        metrics.addAll(statisticMetrics);
        metrics.addAll(segmentMetrics);
        metrics.addAll(insightMetrics);
        return metrics;
    }

    //endregion


    //region Translating Events

    public static TimelineV2Event translateSegmentToEvent(TimelineV1Segment segment) {
        final TimelineV2Event.SleepState sleepState;
        final TimelineV2Event.Type type;
        if (segment.eventType == TimelineV1Segment.Type.NONE) {
            sleepState = TimelineV2Event.SleepState.AWAKE;
            type = TimelineV2Event.Type.IN_BED;
        } else {
            sleepState = TimelineV2Event.SleepState.fromSleepDepth(segment.sleepDepth);
            type = translateEventType(segment.eventType);
        }
        return new TimelineV2Event(segment.timestamp,
                                   segment.tzOffsetMillis,
                                   TimeUnit.SECONDS.toMillis(segment.duration),
                                   segment.message,
                                   segment.sleepDepth,
                                   sleepState,
                                   type,
                                   TimelineV2Event.Action.forEventType(type));
    }

    //endregion


    //region Translating Enums

    public static TimelineV2Metric.Name translateSensorToName(TimelineV1Insight.Sensor sensor) {
        switch (sensor) {
            case HUMIDITY:
                return TimelineV2Metric.Name.HUMIDITY;
            case LIGHT:
                return TimelineV2Metric.Name.LIGHT;
            case PARTICULATES:
                return TimelineV2Metric.Name.PARTICULATES;
            case SOUND:
                return TimelineV2Metric.Name.SOUND;
            case TEMPERATURE:
                return TimelineV2Metric.Name.TEMPERATURE;
            default:
                return TimelineV2Metric.Name.UNKNOWN;
        }
    }

    public static TimelineV2Event.Type translateEventType(TimelineV1Segment.Type type) {
        switch (type) {
            case ALARM:
                return TimelineV2Event.Type.ALARM_RANG;
            case IN_BED:
                return TimelineV2Event.Type.GOT_IN_BED;
            case LIGHT:
                return TimelineV2Event.Type.LIGHT;
            case LIGHTS_OUT:
                return TimelineV2Event.Type.LIGHTS_OUT;
            case MOTION:
                return TimelineV2Event.Type.GENERIC_MOTION;
            case NOISE:
                return TimelineV2Event.Type.GENERIC_SOUND;
            case OUT_OF_BED:
                return TimelineV2Event.Type.GOT_OUT_OF_BED;
            case PARTNER_MOTION:
                return TimelineV2Event.Type.PARTNER_MOTION;
            case SLEEP:
                return TimelineV2Event.Type.FELL_ASLEEP;
            case SUNRISE:
                return TimelineV2Event.Type.SUNRISE;
            case SUNSET:
                return TimelineV2Event.Type.SUNSET;
            case WAKE_UP:
                return TimelineV2Event.Type.WOKE_UP;
            case SLEEPING:
                return TimelineV2Event.Type.IN_BED;
            case SNORING:
                return TimelineV2Event.Type.SNORED;
            case SLEEP_TALK:
                return TimelineV2Event.Type.SLEEP_TALKED;
            default:
                return TimelineV2Event.Type.UNKNOWN;
        }
    }

    //endregion
}
