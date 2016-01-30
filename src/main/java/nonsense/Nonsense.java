package nonsense;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import dagger.ObjectGraph;
import nonsense.modules.ConfigModule;
import nonsense.modules.ServerModule;
import nonsense.providers.RandomTimelineSource;
import nonsense.providers.RandomTrendsSource;

public class Nonsense {
    private static final Logger LOGGER = LoggerFactory.getLogger(Nonsense.class.getSimpleName());

    public static void main(String[] args) {
        LOGGER.info("Starting up with arguments " + Arrays.toString(args));

        final ObjectGraph graph = ObjectGraph.create(new ServerModule(),
                                                     new ConfigModule(RandomTrendsSource.createFactory(),
                                                                      RandomTimelineSource.createFactory()));
        final Application application = new Application();
        graph.inject(application);
        application.init();
    }
}
