package nonsense;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;

import javax.inject.Inject;

import nonsense.model.Configuration;
import nonsense.model.Types;
import nonsense.model.account.Account;
import nonsense.model.oauth.AccessToken;
import nonsense.model.trends.TimeScale;
import nonsense.providers.TimelineSource;
import nonsense.providers.TrendsSource;
import spark.ResponseTransformer;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getSimpleName());

    @Inject ResponseTransformer transformer;
    @Inject Configuration configuration;
    @Inject TrendsSource.Factory trendsFactory;
    @Inject TimelineSource.Factory timelineFactory;

    public void init() {
        LOGGER.info("Initializing on port {}", configuration.getPort());

        port(configuration.getPort());
        routes();
    }

    private void routes() {
        standardRoutes();
        accountRoutes();
        trendsRoutes();
        timelineRoutes();

        LOGGER.info("Routes ready");
    }

    private void standardRoutes() {
        get("/", (request, response) -> {
            response.type(Types.JSON);
            return Collections.emptyMap();
        }, transformer);
    }

    private void accountRoutes() {
        post("/v1/oauth2/token", Types.FORM_DATA, (request, response) -> {
            response.type(Types.JSON);
            return AccessToken.createFake();
        }, transformer);

        get("/v1/account", (request, response) -> {
            response.type(Types.JSON);
            return Account.createFake();
        }, transformer);
    }

    private void trendsRoutes() {
        get("/v2/trends/:time-scale", (request, response) -> {
            final TimeScale timeScale = TimeScale.fromString(request.params(":time-scale"));
            LOGGER.info("GET /v2/trends/{}", timeScale);

            response.type(Types.JSON);
            return trendsFactory.create(request)
                                 .getTrendsForTimeScale(timeScale);
        }, transformer);
    }

    private void timelineRoutes() {
        get("/v1/timeline/:date", (request, response) -> {
            final LocalDate timelineDate = LocalDate.parse(request.params(":date"));
            LOGGER.info("GET /v1/timeline/{}", timelineDate);

            response.type(Types.JSON);
            return timelineFactory.create(request).getTimelineV1ForDate(timelineDate);
        }, transformer);

        get("/v2/timeline/:date", (request, response) -> {
            final LocalDate timelineDate = LocalDate.parse(request.params(":date"));
            LOGGER.info("GET /v2/timeline/{}", timelineDate);

            response.type(Types.JSON);
            return timelineFactory.create(request).getTimelineV2ForDate(timelineDate);
        }, transformer);
    }
}
