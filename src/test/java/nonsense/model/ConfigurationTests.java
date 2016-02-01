package nonsense.model;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ConfigurationTests {
    @Test
    public void parseEmpty() {
        final String[] args = {};
        final Configuration configuration = Configuration.parse(args);
        assertThat(configuration.getPort(), is(equalTo(3000)));
        assertThat(configuration.getTimelineCache(), is(nullValue()));
        assertThat(configuration.wantsHelp(), is(false));
    }

    @Test
    public void parseWellFormed() {
        final String[] args = {"--port", "2999", "--timeline-cache", "./assets/timeline-cache/timeline_1.txt"};
        final Configuration configuration = Configuration.parse(args);
        assertThat(configuration.getPort(), is(equalTo(2999)));
        assertThat(configuration.getTimelineCache(), is(equalTo(new File("./assets/timeline-cache/timeline_1.txt"))));
        assertThat(configuration.wantsHelp(), is(false));
    }

    @Test
    public void parseHelp() {
        final String[] args = {"--help"};
        final Configuration configuration = Configuration.parse(args);
        assertThat(configuration.getPort(), is(equalTo(3000)));
        assertThat(configuration.getTimelineCache(), is(nullValue()));
        assertThat(configuration.wantsHelp(), is(true));
    }

    @Test
    public void printUsage() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintStream stream = new PrintStream(out);
        Configuration.printUsage(stream);

        final String[] expectedLines = {
                "Usage: nonsense [options]",
                "	[--port -p value] : the port to run the server on",
                "	[--timeline-cache -c value] : load timelines from cache files",
                "	[--help -h] : display this message and exit",
                "",
        };
        final String expected = String.join("\n", expectedLines);
        final String usage = out.toString(Charset.defaultCharset().name());
        assertThat(usage, is(equalTo(expected)));
    }
}
