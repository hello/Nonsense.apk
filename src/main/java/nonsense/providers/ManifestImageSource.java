package nonsense.providers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import nonsense.model.images.ImageManifest;
import nonsense.model.insights.Category;
import nonsense.model.insights.MultiDensityImage;

public class ManifestImageSource implements ImageSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManifestImageSource.class);

    private final ImageManifest manifest;

    public static Optional<ManifestImageSource.Factory> createFactory(ObjectMapper objectMapper,
                                                                      File insightImageManifest) {
        try (FileInputStream inputStream = new FileInputStream(insightImageManifest)) {
            final ImageManifest imageManifest = objectMapper.readValue(inputStream, ImageManifest.class);
            return Optional.of(request -> new ManifestImageSource(imageManifest));
        } catch (IOException e) {
            LOGGER.error("Could not load insight image manifest", e);
            return Optional.empty();
        }
    }

    public ManifestImageSource(ImageManifest manifest) {
        this.manifest = manifest;
    }

    @Override
    public Optional<MultiDensityImage> getImageForCategory(Category category) {
        return Optional.of(manifest.getImageForCategory(category));
    }
}
