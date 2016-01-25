package nonsense;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Locale;

import nonsense.generators.TrendsGenerator;
import nonsense.model.trends.TimeScale;
import nonsense.response.JacksonTransformer;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;

import static spark.Spark.get;
import static spark.Spark.port;

public class Nonsense {
    private static ObjectMapper createObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        return objectMapper;
    }

    private static ResponseTransformer createResponseTransformer() {
        return new JacksonTransformer(createObjectMapper());
    }

    public static void main(String[] args) {
        final ResponseTransformer transformer = createResponseTransformer();
        port(3000);
        get("/", JacksonTransformer.CONTENT_TYPE, Nonsense::index, transformer);
        get("/v2/trends/:time-scale", JacksonTransformer.CONTENT_TYPE, Nonsense::trends, transformer);
    }

    public static Object index(Request request, Response response) {
        return Collections.emptyMap();
    }

    public static Object trends(Request request, Response response) {
        final TimeScale timeScale = TimeScale.fromString(request.params("time-scale"));
        final TrendsGenerator generator = new TrendsGenerator(LocalDate.now().minusDays(1), Locale.getDefault());
        return generator.generateTrends(timeScale);
    }
}
