package nonsense.providers;

import java.time.LocalDate;

import nonsense.model.timeline.VersionTranslator;
import nonsense.model.timeline.v1.TimelineV1;
import nonsense.model.timeline.v2.TimelineV2;

public interface TimelineProvider extends Provider {
    TimelineV1 getTimelineV1ForDate(LocalDate date);

    default TimelineV2 getTimelineV2ForDate(LocalDate date) {
        final TimelineV1 timelineV1 = getTimelineV1ForDate(date);
        return VersionTranslator.translate(timelineV1);
    }

    @FunctionalInterface
    interface Factory extends Provider.Factory<TimelineProvider> {
    }
}
