package nonsense.providers;

import java.time.LocalDate;
import java.util.List;

import nonsense.model.timeline.VersionTranslator;
import nonsense.model.timeline.v1.TimelineV1;
import nonsense.model.timeline.v2.TimelineV2;

public interface TimelineSource extends Source {
    List<TimelineV1> getTimelinesV1ForDate(LocalDate date);

    default TimelineV2 getTimelineV2ForDate(LocalDate date) {
        final TimelineV1 timelineV1 = getTimelinesV1ForDate(date).get(0);
        return VersionTranslator.translate(timelineV1);
    }

    @FunctionalInterface
    interface Factory extends Source.Factory<TimelineSource> {
    }
}
