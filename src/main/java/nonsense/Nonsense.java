package nonsense;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Locale;
import java.util.logging.Logger;

import nonsense.providers.TrendsProvider;
import nonsense.model.Types;
import nonsense.model.account.Account;
import nonsense.model.oauth.AccessToken;
import nonsense.model.trends.TimeScale;
import nonsense.model.trends.Trends;
import nonsense.response.JacksonTransformer;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class Nonsense {
    private static final Logger LOGGER = Logger.getLogger(Nonsense.class.getSimpleName());

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

        final TrendsProvider.Builder builder = new TrendsProvider.Builder();

        if (request.queryParams().contains("ns_today")) {
            final String todayRaw = request.queryParams("ns_today");
            final LocalDate today = LocalDate.parse(todayRaw);
            builder.setToday(today);
        }

        if (request.queryParams().contains("ns_account_age")) {
            final String accountAgeRaw = request.queryParams("ns_account_age");
            final int accountAge = Integer.valueOf(accountAgeRaw, 10);
            builder.setAccountAgeDays(accountAge);
        }

        if (request.queryParams().contains("ns_locale")) {
            final String localeRaw = request.queryParams("ns_locale");
            final Locale locale = Locale.forLanguageTag(localeRaw);
            builder.setLocale(locale);
        }

        response.type(Types.JSON);
        return builder.build().getTrendsForTimeScale(timeScale);
    }
}
