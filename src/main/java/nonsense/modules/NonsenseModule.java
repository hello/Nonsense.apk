package nonsense.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nonsense.Application;
import nonsense.model.Configuration;
import nonsense.providers.CacheTimelineSource;
import nonsense.providers.CacheTrendsSource;
import nonsense.providers.RandomTimelineSource;
import nonsense.providers.RandomTrendsSource;
import nonsense.providers.TimelineSource;
import nonsense.providers.TrendsSource;
import nonsense.response.JacksonTransformer;
import spark.ResponseTransformer;

@Module(complete = false,
        injects = {
                Application.class,
        })
public class NonsenseModule {
    private final Configuration configuration;

    public NonsenseModule(Configuration configuration) {
        this.configuration = configuration;
    }

    @Singleton @Provides ObjectMapper provideObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Singleton @Provides ResponseTransformer provideResponseTransformer(ObjectMapper objectMapper) {
        return new JacksonTransformer(objectMapper);
    }

    @Provides Configuration provideConfiguration() {
        return configuration;
    }

    @Singleton @Provides TrendsSource.Factory provideTrendsSourceFactory(ObjectMapper objectMapper) {
        final File trendsCache = configuration.getTrendsCache();
        if (trendsCache != null) {
            return CacheTrendsSource.createFactory(objectMapper, trendsCache)
                                    .orElseGet(() -> RandomTrendsSource.createFactory(configuration));
        } else {
            return RandomTrendsSource.createFactory(configuration);
        }
    }

    @Singleton @Provides TimelineSource.Factory provideTimelineSourceFactory(ObjectMapper objectMapper) {
        final File timelineCache = configuration.getTimelineCache();
        if (timelineCache != null) {
            return CacheTimelineSource.createFactory(objectMapper, timelineCache)
                                      .orElseGet(RandomTimelineSource::createFactory);
        } else {
            return RandomTimelineSource.createFactory();
        }
    }
}
