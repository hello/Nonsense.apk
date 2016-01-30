package nonsense.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import nonsense.model.Condition;
import nonsense.model.timeline.v1.TimelineV1;
import nonsense.model.timeline.v1.TimelineV1Insight;
import nonsense.model.timeline.v1.TimelineV1Segment;
import nonsense.model.timeline.v1.TimelineV1Statistics;
import nonsense.util.Enums;
import nonsense.util.RandomUtil;
import nonsense.util.Requests;

public class RandomTimelineSource implements TimelineSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomTimelineSource.class);

    private static final TimelineV1Segment.Type[] EVENT_TYPES = {
            TimelineV1Segment.Type.ALARM,
            TimelineV1Segment.Type.LIGHT,
            TimelineV1Segment.Type.LIGHTS_OUT,
            TimelineV1Segment.Type.MOTION,
            TimelineV1Segment.Type.NOISE,
            TimelineV1Segment.Type.PARTNER_MOTION,
            TimelineV1Segment.Type.SUNRISE,
            TimelineV1Segment.Type.SUNSET,
            TimelineV1Segment.Type.SNORING,
            TimelineV1Segment.Type.SLEEP_TALK,
            TimelineV1Segment.Type.IN_BED,
            TimelineV1Segment.Type.SLEEP,
            TimelineV1Segment.Type.OUT_OF_BED,
            TimelineV1Segment.Type.WAKE_UP,
    };

    private final Random random = new Random();
    private final ZoneOffset timeZone;
    private final Locale locale;

    public static Factory createFactory() {
        return request -> {
            return new RandomTimelineSource(Requests.queryParamTimeZone(request, Requests.TIME_ZONE),
                                            Requests.queryParamLocale(request, Requests.LOCALE));
        };
    }

    public RandomTimelineSource(ZoneOffset timeZone, Locale locale) {
        this.timeZone = timeZone;
        this.locale = locale;
    }

    @Override
    public TimelineV1 getTimelineV1ForDate(LocalDate date) {
        LOGGER.info("Generating timeline (v1) for date {}", date);

        final LocalDateTime startTimestamp = generateStartTimestamp(date);

        int durationSeconds = generateDurationSeconds();
        int elapsedDurationSeconds = 0;
        int soundSleepDurationSeconds = 0;
        int timesAwake = 0;

        final List<TimelineV1Segment> segments = new ArrayList<>();
        while (elapsedDurationSeconds < durationSeconds) {
            final LocalDateTime timestamp = startTimestamp.plusSeconds(elapsedDurationSeconds);
            final TimelineV1Segment.Type type = generateSegmentType();
            final TimelineV1Segment segment = generateSegment(timestamp, type);
            segments.add(segment);

            elapsedDurationSeconds += segment.duration;
            if (segment.sleepDepth >= 70) {
                soundSleepDurationSeconds += segment.duration;
            }

            if (segment.eventType == TimelineV1Segment.Type.WAKE_UP) {
                timesAwake += 1;
            }
        }

        final TimelineV1Statistics statistics = generateStatistics(soundSleepDurationSeconds / 60,
                                                                   timesAwake,
                                                                   elapsedDurationSeconds / 60);

        final List<TimelineV1Insight> insights = generateInsights();

        final int score = RandomUtil.integerInRange(random, 0, 100);

        return new TimelineV1(date,
                              insights,
                              generateTimelineMessage(statistics),
                              score,
                              segments,
                              Optional.of(statistics));
    }

    public int generateDurationSeconds() {
        return RandomUtil.integerInRange(random, 2, 12) * 60;
    }

    public LocalDateTime generateStartTimestamp(LocalDate date) {
        final int hours = RandomUtil.integerInRange(random, 20/*8pm*/, 26/*2am*/);
        return date.atStartOfDay().plusHours(hours);
    }

    public String generateTimelineMessage(TimelineV1Statistics statistics) {
        String message;
        if (statistics.totalSleep.isPresent()) {
            message = String.format(locale, "You were asleep for **%d hours**",
                                    statistics.totalSleep.getAsLong() / 60);

            if (statistics.soundSleep.isPresent()) {
                message += String.format(locale, ", and sleeping soundly for **%d hours**",
                                         statistics.soundSleep.getAsLong() / 60);
            }
        } else {
            message = "You did not sleep";
        }

        return message;
    }

    public TimelineV1Statistics generateStatistics(long soundSleepMinutes,
                                                   long timesAwake,
                                                   long totalSleepMinutes) {
        final int timeToSleep = RandomUtil.integerInRange(random, 0, 60);
        return new TimelineV1Statistics(OptionalLong.of(soundSleepMinutes),
                                        OptionalLong.of(timeToSleep),
                                        OptionalLong.of(timesAwake),
                                        OptionalLong.of(totalSleepMinutes));
    }

    public List<TimelineV1Insight> generateInsights() {
        return Arrays.stream(TimelineV1Insight.Sensor.values())
                     .filter(Predicate.isEqual(TimelineV1Insight.Sensor.UNKNOWN).negate())
                     .map(this::generateInsight)
                     .collect(Collectors.toList());
    }

    public TimelineV1Insight generateInsight(TimelineV1Insight.Sensor sensor) {
        LOGGER.info("Generating insight for sensory {}", sensor);

        final Condition condition = Enums.random(Condition.ALERT,
                                                 Condition.WARNING,
                                                 Condition.IDEAL);
        final String message;
        switch (condition) {
            case ALERT:
                message = "Get out of your house!";
                break;
            case WARNING:
                message = String.format("The %s is a *bit high*", sensor);
                break;
            case IDEAL:
                message = String.format("The %s is a ideal", sensor);
                break;
            default:
                message = String.format("The %s has gone whack", sensor);
                break;
        }
        return new TimelineV1Insight(condition, message, sensor);
    }

    public TimelineV1Segment generateSegment(LocalDateTime timestamp, TimelineV1Segment.Type type) {
        LOGGER.info("Generating segment for timestamp {} and type {}", timestamp, type);

        final long duration = RandomUtil.integerInRange(random, 0, 60);
        final Optional<String> message = (type == TimelineV1Segment.Type.SLEEPING)
                ? Optional.empty()
                : Optional.of(type.message);
        return new TimelineV1Segment(duration,
                                     type,
                                     random.nextInt(),
                                     message,
                                     timeZone.getTotalSeconds() * 1000,
                                     RandomUtil.integerInRange(random, 0, 100),
                                     timestamp,
                                     Collections.emptyList());
    }

    public TimelineV1Segment.Type generateSegmentType() {
        if (random.nextFloat() > 0.75f) {
            return RandomUtil.inArray(random, EVENT_TYPES);
        } else {
            return TimelineV1Segment.Type.SLEEPING;
        }
    }
}
