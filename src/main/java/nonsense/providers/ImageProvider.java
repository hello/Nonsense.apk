package nonsense.providers;

import java.util.Optional;

import nonsense.model.images.MultiDensityImage;
import nonsense.model.insights.Category;

public interface ImageProvider {
    Optional<MultiDensityImage> getImageForInsightCategory(Category category);
}
