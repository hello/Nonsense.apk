package nonsense.model.images;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import nonsense.model.insights.Category;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class ImageManifestTest {
    @Test
    public void getImageForCategory() {
        final MultiDensityImage generic = MultiDensityImage.newEmpty();
        final MultiDensityImage sleepHygiene = MultiDensityImage.newEmpty();

        final Map<Category, MultiDensityImage> images = new HashMap<>();
        images.put(Category.GENERIC, generic);
        images.put(Category.SLEEP_HYGIENE, sleepHygiene);

        final ImageManifest manifest = new ImageManifest(images);
        assertThat(manifest.getImageForCategory(Category.SLEEP_HYGIENE).get(),
                   is(sameInstance(sleepHygiene)));
        assertThat(manifest.getImageForCategory(Category.GENERIC).get(),
                   is(sameInstance(generic)));
        assertThat(manifest.getImageForCategory(Category.SLEEP_DURATION).get(),
                   is(sameInstance(generic)));
    }
}
