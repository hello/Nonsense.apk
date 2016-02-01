package nonsense;

import com.lexicalscope.jewel.cli.ArgumentValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import dagger.ObjectGraph;
import nonsense.model.Configuration;
import nonsense.modules.NonsenseModule;

public class Nonsense {
    private static final Logger LOGGER = LoggerFactory.getLogger(Nonsense.class.getSimpleName());

    public static void main(String[] args) {
        final Configuration configuration;
        try {
            configuration = Configuration.parse(args);
            if (configuration.wantsHelp()) {
                Configuration.printUsage(System.out);
                System.exit(0);
            }
        } catch (ArgumentValidationException e) {
            System.err.println(e.getLocalizedMessage());
            Configuration.printUsage(System.err);
            System.exit(-1);

            return;
        }

        LOGGER.info("Starting up with arguments " + Arrays.toString(args));

        final ObjectGraph graph = ObjectGraph.create(new NonsenseModule(configuration));
        final Application application = new Application();
        graph.inject(application);
        application.init();
    }
}
