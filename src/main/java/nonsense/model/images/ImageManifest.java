package nonsense.model.images;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import nonsense.model.insights.Category;
import nonsense.model.insights.MultiDensityImage;

public class ImageManifest {
    @JsonProperty("insight_images")
    private Map<Category, MultiDensityImage> images;

    @JsonProperty("default_insight_image")
    private MultiDensityImage defaultImage;

    public MultiDensityImage getImageForCategory(Category category) {
        final MultiDensityImage existing = images.get(category);
        if (existing != null) {
            return existing;
        } else {
            return defaultImage;
        }
    }

    @Override
    public String toString() {
        return "ImageManifest{" +
                "images=" + images +
                ", defaultImage=" + defaultImage +
                '}';
    }
}
