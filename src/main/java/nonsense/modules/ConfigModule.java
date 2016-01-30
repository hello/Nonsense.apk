package nonsense.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nonsense.Application;
import nonsense.model.Configuration;
import nonsense.providers.RandomTimelineSource;
import nonsense.providers.RandomTrendsSource;
import nonsense.providers.TimelineSource;
import nonsense.providers.TrendsSource;

@Module(complete = false,
        injects = {
                Application.class,
        })
public class ConfigModule {
    private final Configuration configuration;

    public ConfigModule(Configuration configuration) {
        this.configuration = configuration;
    }

    @Provides Configuration provideConfiguration() {
        return configuration;
    }

    @Singleton @Provides TrendsSource.Factory provideTrendsSourceFactory() {
        return RandomTrendsSource.createFactory();
    }

    @Singleton @Provides TimelineSource.Factory provideTimelineSourceFactory() {
        return RandomTimelineSource.createFactory();
    }
}
