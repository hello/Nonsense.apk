package nonsense.providers;

import java.util.Optional;

import nonsense.model.images.MultiDensityImage;
import nonsense.model.insights.Category;

public final class EmptyImageProvider implements ImageProvider {
    @Override
    public Optional<MultiDensityImage> getImageForInsightCategory(Category category) {
        return Optional.empty();
    }
}
