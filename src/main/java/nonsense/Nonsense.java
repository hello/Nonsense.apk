package nonsense;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Locale;
import java.util.logging.Logger;

import nonsense.providers.RandomTimelineProvider;
import nonsense.providers.RandomTrendsProvider;
import nonsense.model.Types;
import nonsense.model.account.Account;
import nonsense.model.oauth.AccessToken;
import nonsense.model.trends.TimeScale;
import nonsense.model.trends.Trends;
import nonsense.providers.TrendsProvider;
import nonsense.response.JacksonTransformer;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class Nonsense {
    private static final Logger LOGGER = Logger.getLogger(Nonsense.class.getSimpleName());
    private static final TrendsProvider.Factory TRENDS_FACTORY = RandomTrendsProvider.createFactory();

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    private static ResponseTransformer createResponseTransformer() {
        return new JacksonTransformer(createObjectMapper());
    }

    public static void main(String[] args) {
        final ResponseTransformer transformer = createResponseTransformer();
        port(3000);
        get("/", Nonsense::index, transformer);
        post("/v1/oauth2/token", Types.FORM_DATA, Nonsense::token, transformer);
        get("/v1/account", Nonsense::account, transformer);
        get("/v2/trends/:time-scale", Nonsense::trends, transformer);
    }

    public static Object index(Request request, Response response) {
        response.type(Types.JSON);
        return Collections.emptyMap();
    }

    public static AccessToken token(Request request, Response response) {
        response.type(Types.JSON);
        return AccessToken.createFake();
    }

    public static Account account(Request request, Response response) {
        response.type(Types.JSON);
        return Account.createFake();
    }

    public static Trends trends(Request request, Response response) {
        final TimeScale timeScale = TimeScale.fromString(request.params("time-scale"));
        LOGGER.info("GET /v2/trends/" + timeScale);
        response.type(Types.JSON);
        return TRENDS_FACTORY.create(request).getTrendsForTimeScale(timeScale);
    }
}
