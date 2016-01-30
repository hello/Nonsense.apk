package nonsense.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nonsense.Application;
import nonsense.providers.TimelineSource;
import nonsense.providers.TrendsSource;

@Module(complete = false,
        injects = {
                Application.class,
        })
public class ConfigModule {
    private final TrendsSource.Factory trendsSourceFactory;
    private final TimelineSource.Factory timelineSourceFactory;

    public ConfigModule(TrendsSource.Factory trendsSourceFactory,
                        TimelineSource.Factory timelineSourceFactory) {
        this.trendsSourceFactory = trendsSourceFactory;
        this.timelineSourceFactory = timelineSourceFactory;
    }

    @Singleton @Provides TrendsSource.Factory provideTrendsSourceFactory() {
        return trendsSourceFactory;
    }

    @Singleton @Provides TimelineSource.Factory provideTimelineSourceFactory() {
        return timelineSourceFactory;
    }
}
