package nonsense.providers;

import nonsense.model.trends.TimeScale;
import nonsense.model.trends.Trends;

public interface TrendsProvider extends Provider {
    Trends getTrendsForTimeScale(TimeScale timeScale);

    @FunctionalInterface
    interface Factory extends Provider.Factory<TrendsProvider> {
    }
}
