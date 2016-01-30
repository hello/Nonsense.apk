package nonsense.model;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Configuration {
    @Parameter(
            names = {"--timeline-cache", "-c"},
            description = "load timelines from cache files",
            required = false
    )
    private String timelineCache;

    @Parameter(
            names = {"--port", "-p"},
            description = "the port to run the server on",
            required = false
    )
    private int port = 3000;

    @Parameter(
            names = {"-h", "--help"},
            help = true,
            description = "print this message",
            required = false
    )
    private boolean help;

    private final JCommander commander;

    private Configuration(JCommander commander) {
        this.commander = commander;
    }

    public static Configuration parse(String[] args) {
        final JCommander commander = new JCommander();
        final Configuration configuration = new Configuration(commander);
        commander.addObject(configuration);
        commander.parse(args);
        return configuration;
    }

    public String getTimelineCache() {
        return timelineCache;
    }

    public int getPort() {
        return port;
    }

    public boolean wantsHelp() {
        return help;
    }

    public void usage() {
        commander.usage();
        System.exit(0);
    }
}
