package nonsense.providers;

import spark.Request;

public interface Source {
    @FunctionalInterface
    interface Factory<T extends Source> {
        T create(Request request);
    }
}
