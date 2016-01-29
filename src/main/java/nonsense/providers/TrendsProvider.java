package nonsense.providers;

import nonsense.model.trends.TimeScale;
import nonsense.model.trends.Trends;

public interface TrendsProvider {
    Trends getTrendsForTimeScale(TimeScale timeScale);
}
