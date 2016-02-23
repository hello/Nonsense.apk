package nonsense.providers;

import java.util.List;

import nonsense.model.insights.Insight;

public interface InsightSource extends Source {
    List<Insight> getInsights(ImageProvider imageProvider);

    @FunctionalInterface
    interface Factory extends Source.Factory<InsightSource> {
    }
}
