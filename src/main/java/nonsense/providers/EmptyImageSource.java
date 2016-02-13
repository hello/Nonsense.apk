package nonsense.providers;

import java.util.Optional;

import nonsense.model.insights.Category;
import nonsense.model.insights.MultiDensityImage;

public class EmptyImageSource implements ImageSource {
    public static ImageSource.Factory createFactory() {
        final EmptyImageSource singleton = new EmptyImageSource();
        return request -> singleton;
    }

    @Override
    public Optional<MultiDensityImage> getImageForCategory(Category category) {
        return Optional.empty();
    }
}
