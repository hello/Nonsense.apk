package nonsense.providers;

import java.util.Optional;

import nonsense.model.insights.Category;
import nonsense.model.insights.MultiDensityImage;

public interface ImageSource extends Source {
    Optional<MultiDensityImage> getImageForCategory(Category category);

    @FunctionalInterface
    interface Factory extends Source.Factory<ImageSource> {
    }
}
