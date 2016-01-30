package nonsense;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import dagger.ObjectGraph;
import nonsense.model.Configuration;
import nonsense.modules.ConfigModule;
import nonsense.modules.ServerModule;

public class Nonsense {
    private static final Logger LOGGER = LoggerFactory.getLogger(Nonsense.class.getSimpleName());

    public static void main(String[] args) {
        final Configuration configuration = Configuration.parse(args);
        if (configuration.wantsHelp()) {
            configuration.usage();
        }

        LOGGER.info("Starting up with arguments " + Arrays.toString(args));

        final ObjectGraph graph = ObjectGraph.create(new ServerModule(),
                                                     new ConfigModule(configuration));
        final Application application = new Application();
        graph.inject(application);
        application.init();
    }
}
