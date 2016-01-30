package nonsense.providers;

import nonsense.model.trends.TimeScale;
import nonsense.model.trends.Trends;

public interface TrendsSource extends Source {
    Trends getTrendsForTimeScale(TimeScale timeScale);

    @FunctionalInterface
    interface Factory extends Source.Factory<TrendsSource> {
    }
}
