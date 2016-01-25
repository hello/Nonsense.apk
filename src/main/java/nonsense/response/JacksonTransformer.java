package nonsense.response;

import com.fasterxml.jackson.databind.ObjectMapper;

import spark.ResponseTransformer;

public class JacksonTransformer implements ResponseTransformer {
    public static final String CONTENT_TYPE = "application/json";

    private final ObjectMapper objectMapper;

    public JacksonTransformer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String render(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }
}
