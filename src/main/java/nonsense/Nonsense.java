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
    private final ResponseTransformer transformer;

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    private static ResponseTransformer createResponseTransformer() {
        return new JacksonTransformer(createObjectMapper());
    }

    //region Lifecycle

    public static void main(String[] args) {
        new Nonsense(createResponseTransformer());
    }

    public Nonsense(ResponseTransformer transformer) {
        this.transformer = transformer;

        port(3000);
        setupRoutes();
    }

    //endregion


    //region Routes

    public void setupRoutes() {
        get("/", (request, response) -> {
            response.type(Types.JSON);
            return Collections.emptyMap();
        }, transformer);

        setupAccountRoutes();
        setupTrendsRoutes();
    }

    public void setupAccountRoutes() {
        post("/v1/oauth2/token", Types.FORM_DATA, (request, response) -> {
            response.type(Types.JSON);
            return AccessToken.createFake();
        }, transformer);

        get("/v1/account", (request, response) -> {
            response.type(Types.JSON);
            return Account.createFake();
        }, transformer);
    }

    public void setupTrendsRoutes() {
        get("/v2/trends/:time-scale", (request, response) -> {
            final TimeScale timeScale = TimeScale.fromString(request.params("time-scale"));
            LOGGER.info("GET /v2/trends/" + timeScale);
            response.type(Types.JSON);
            return TRENDS_FACTORY.create(request)
                                 .getTrendsForTimeScale(timeScale);
        }, transformer);
    }

    //endregion
}
