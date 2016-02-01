package nonsense.model;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.Cli;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.CommandLineInterface;
import com.lexicalscope.jewel.cli.Option;

import java.io.File;
import java.io.PrintStream;

@CommandLineInterface(application = "nonsense")
public interface Configuration {
    @Option(shortName = "p",
            longName = "port",
            defaultValue = "3000",
            description = "the port to run the server on")
    int getPort();

    @Option(shortName = "c",
            longName = "timeline-cache",
            description = "load timelines from cache files",
            defaultToNull = true)
    File getTimelineCache();

    @Option(shortName = "h",
            longName = "help",
            description = "display this message and exit")
    boolean wantsHelp();

    static Configuration parse(String[] args) throws ArgumentValidationException {
        return CliFactory.parseArguments(Configuration.class, args);
    }

    static void printUsage(PrintStream stream) {
        final Cli<Configuration> cli = CliFactory.createCli(Configuration.class);
        final String helpMessage = cli.getHelpMessage();
        stream.println(helpMessage);
    }
}
