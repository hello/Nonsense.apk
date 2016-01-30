package nonsense.providers;

import spark.Request;

public interface Provider {
    @FunctionalInterface
    interface Factory<T extends Provider> {
        T create(Request request);
    }
}
