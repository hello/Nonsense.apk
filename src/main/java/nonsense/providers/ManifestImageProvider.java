package nonsense.providers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import nonsense.model.images.ImageManifest;
import nonsense.model.images.MultiDensityImage;
import nonsense.model.insights.Category;

public class ManifestImageProvider implements ImageProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManifestImageProvider.class);

    private final ImageManifest manifest;

    public static Optional<ImageProvider> create(ObjectMapper objectMapper,
                                                 File insightImageManifest) {
        try (FileInputStream inputStream = new FileInputStream(insightImageManifest)) {
            final ImageManifest imageManifest = objectMapper.readValue(inputStream, ImageManifest.class);
            return Optional.of(new ManifestImageProvider(imageManifest));
        } catch (IOException e) {
            LOGGER.error("Could not load insight image manifest", e);
            return Optional.empty();
        }
    }

    public ManifestImageProvider(ImageManifest manifest) {
        this.manifest = manifest;
    }

    @Override
    public Optional<MultiDensityImage> getImageForInsightCategory(Category category) {
        return manifest.getImageForCategory(category);
    }
}
