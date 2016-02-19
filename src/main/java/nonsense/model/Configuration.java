package nonsense.model;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.Cli;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.CommandLineInterface;
import com.lexicalscope.jewel.cli.Option;

import java.io.File;
import java.io.PrintStream;
import java.time.LocalDate;

import spark.utils.StringUtils;

@CommandLineInterface(application = "nonsense")
public interface Configuration {
    @Option(shortName = "p",
            longName = "port",
            defaultValue = "3000",
            description = "the port to run the server on")
    int getPort();

    @Option(longName = "account-age",
            defaultValue = "90",
            description = "the age of the fake account")
    int getAccountAge();

    @Option(longName = "today",
            defaultToNull = true,
            description = "the date to use for today",
            pattern = "^(\\d\\d\\d\\d)-(\\d\\d)-(\\d\\d)$")
    String getTodayRaw();

    @Option(longName = "timeline-cache",
            description = "load timelines from cache files",
            defaultToNull = true)
    File getTimelineCache();

    @Option(longName = "trends-cache",
            description = "load trends from cache files",
            defaultToNull = true)
    File getTrendsCache();

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

    /**
     * Extracts the today date from a {@link Configuration} object.
     * @param configuration The configuration to extract today from.
     * @implNote This can't be a default method, JewelCli does not forward to default method implementations.
     * @return  Today, or a reasonable facsimile.
     */
    static LocalDate getToday(Configuration configuration) {
        final String todayRaw = configuration.getTodayRaw();
        if (StringUtils.isEmpty(todayRaw)) {
            return LocalDate.now().minusDays(1);
        } else {
            return LocalDate.parse(todayRaw);
        }
    }
}
