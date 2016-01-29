package nonsense.providers;

import java.time.LocalDate;

import nonsense.model.timeline.VersionTranslator;
import nonsense.model.timeline.v1.TimelineV1;
import nonsense.model.timeline.v2.TimelineV2;

public interface TimelineProvider {
    TimelineV1 getTimelineV1ForDate(LocalDate date);

    default TimelineV2 getTimelineV2ForDate(LocalDate date) {
        final TimelineV1 timelineV1 = getTimelineV1ForDate(date);
        return VersionTranslator.translate(timelineV1);
    }
}
