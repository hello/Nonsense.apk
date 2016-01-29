package nonsense.providers;

import com.google.common.annotations.VisibleForTesting;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import nonsense.model.timeline.v1.TimelineV1;
import nonsense.model.timeline.v1.TimelineV1Insight;
import nonsense.model.timeline.v1.TimelineV1Segment;
import nonsense.model.timeline.v1.TimelineV1Statistics;

public class RandomTimelineProvider implements TimelineProvider {
    @Override
    public TimelineV1 getTimelineV1ForDate(LocalDate date) {
        return null;
    }

    @VisibleForTesting
    TimelineV1Statistics generateStatistics() {
        return null;
    }

    private List<TimelineV1Insight> generateInsights() {
        return null;
    }

    @VisibleForTesting
    TimelineV1Insight generateInsight(Set<TimelineV1Insight.Sensor> excludedSensors) {
        return null;
    }

    @VisibleForTesting
    List<TimelineV1Segment> generateSegments() {
        return null;
    }

    @VisibleForTesting
    TimelineV1Segment generateSleepSegment(boolean awake) {
        return null;
    }

    @VisibleForTesting
    TimelineV1Segment generateMotionEventSegment() {
        return null;
    }

    @VisibleForTesting
    TimelineV1Segment generateDisturbanceEventSegment() {
        return null;
    }
}
