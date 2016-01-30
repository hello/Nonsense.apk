package nonsense.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nonsense.Application;
import nonsense.response.JacksonTransformer;
import spark.ResponseTransformer;

@Module(complete = false,
        injects = {
                Application.class,
        })
public class ServerModule {
    @Singleton @Provides ObjectMapper provideObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Singleton @Provides ResponseTransformer provideResponseTransformer(ObjectMapper objectMapper) {
        return new JacksonTransformer(objectMapper);
    }
}
