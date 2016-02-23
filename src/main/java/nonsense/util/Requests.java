package nonsense.util;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

import spark.Request;

public class Requests {
    public static final String TIME_ZONE = "ns_time_zone";
    public static final String LOCALE = "ns_locale";
    public static final String TODAY = "ns_today";
    public static final String ACCOUNT_AGE = "ns_account_age";
    public static final String LIMIT = "ns_limit";

    private static <T> T queryParamValue(Request request,
                                         String name,
                                         Function<String, T> factory,
                                         Supplier<T> getDefault) {
        if (request.queryParams().contains(name)) {
            final String param = request.queryParams(name);
            return factory.apply(param);
        } else {
            return getDefault.get();
        }
    }

    public static int queryParamInteger(Request request, String name, int defaultValue) {
        return queryParamValue(request, name,
                               Integer::parseInt,
                               () -> defaultValue);
    }

    public static Locale queryParamLocale(Request request, String name) {
        return queryParamValue(request, name,
                               Locale::forLanguageTag,
                               Locale::getDefault);
    }

    public static LocalDate queryParamLocalDate(Request request, String name, Supplier<LocalDate> getDefault) {
        return queryParamValue(request, name,
                               LocalDate::parse,
                               getDefault);
    }

    public static LocalDate queryParamLocalDate(Request request, String name) {
        return queryParamLocalDate(request, name, LocalDate::now);
    }

    public static DateTimeZone queryParamTimeZone(Request request, String name) {
        return queryParamValue(request, name,
                               DateTimeZone::forID,
                               () -> DateTimeZone.UTC);
    }
}
