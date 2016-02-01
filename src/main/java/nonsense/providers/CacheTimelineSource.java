package nonsense.providers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import nonsense.model.timeline.v1.TimelineV1;

public class CacheTimelineSource implements TimelineSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheTimelineSource.class);

    private final Random random = new Random();
    private final List<TimelineV1> timelines = new ArrayList<>();

    public static Optional<TimelineSource.Factory> createFactory(ObjectMapper mapper, File source) {
        try {
            final CacheTimelineSource timelineSource = new CacheTimelineSource();
            timelineSource.read(mapper, source);
            return Optional.of(request -> timelineSource);
        } catch (IOException e) {
            LOGGER.error("Could not load timeline cache", e);
            return Optional.empty();
        }
    }

    public void read(ObjectMapper mapper, File source) throws IOException {
        LOGGER.info("Reading timeline cache from {}", source);

        try (final BufferedReader reader = new BufferedReader(new FileReader(source))) {
            read(mapper, reader);
        }

        LOGGER.info("Read {} timelines from {}", timelines.size(), source);
    }

    public void read(ObjectMapper mapper, BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            final TimelineV1 timelineV1 = mapper.readValue(line, TimelineV1.class);
            timelines.add(timelineV1);
        }
    }

    @Override
    public TimelineV1 getTimelineV1ForDate(LocalDate date) {
        final int position = random.nextInt(timelines.size());
        return timelines.get(position);
    }
}
