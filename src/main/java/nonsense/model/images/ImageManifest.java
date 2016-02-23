package nonsense.model.images;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Optional;

import nonsense.model.insights.Category;

public class ImageManifest {
    @JsonProperty("insight_images")
    private Map<Category, MultiDensityImage> images;

    public ImageManifest(@JsonProperty("insight_images") Map<Category, MultiDensityImage> images) {
        this.images = images;
    }

    public Optional<MultiDensityImage> getImageForCategory(Category category) {
        final MultiDensityImage existing = images.get(category);
        if (existing != null) {
            return Optional.of(existing);
        } else {
            return Optional.ofNullable(images.get(Category.GENERIC));
        }
    }

    @Override
    public String toString() {
        return "ImageManifest{" +
                "images=" + images +
                '}';
    }
}
