package nonsense.providers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import nonsense.model.trends.TimeScale;
import nonsense.model.trends.Trends;

public class CacheTrendsSource implements TrendsSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheTrendsSource.class);

    private final Random random = new Random();
    private final List<Trends> trends = new ArrayList<>();

    public static Optional<TrendsSource.Factory> createFactory(ObjectMapper mapper, File source) {
        try {
            final CacheTrendsSource trendsSource = new CacheTrendsSource();
            trendsSource.read(mapper, source);
            return Optional.of(request -> trendsSource);
        } catch (IOException e) {
            LOGGER.error("Could not load trends cache", e);
            return Optional.empty();
        }
    }

    public void read(ObjectMapper mapper, File source) throws IOException {
        LOGGER.info("Reading trend cache from {}", source);

        try (final BufferedReader reader = new BufferedReader(new FileReader(source))) {
            read(mapper, reader);
        }

        LOGGER.info("Read {} trends from {}", trends.size(), source);
    }

    public void read(ObjectMapper mapper, BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            final Trends trends = mapper.readValue(line, Trends.class);
            this.trends.add(trends);
        }
    }

    @Override
    public Trends getTrendsForTimeScale(TimeScale timeScale) {
        final int position = random.nextInt(trends.size());
        return trends.get(position);
    }
}
