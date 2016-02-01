package nonsense.util;

import com.google.common.collect.Sets;

import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Locale;

import spark.Request;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class RequestsTest {
    private static Request emptyRequest() {
        final Request request = mock(Request.class);
        doReturn(Collections.emptySet())
                .when(request)
                .queryParams();
        doReturn(null)
                .when(request)
                .queryParams(any());
        return request;
    }

    private static Request requestWithQueryParam(String name, String value) {
        final Request request = mock(Request.class);
        doReturn(Sets.newHashSet(name))
                .when(request)
                .queryParams();
        doReturn(value)
                .when(request)
                .queryParams(name);
        return request;
    }

    @Test
    public void queryParamInteger() {
        final Request withAccountAge = requestWithQueryParam(Requests.ACCOUNT_AGE, "80");
        assertThat(Requests.queryParamInteger(withAccountAge, Requests.ACCOUNT_AGE, 90),
                   is(equalTo(80)));

        final Request withoutAccountAge = emptyRequest();
        assertThat(Requests.queryParamInteger(withoutAccountAge, Requests.ACCOUNT_AGE, 90),
                   is(equalTo(90)));
    }

    @Test
    public void queryParamLocale() {
        final Request withLocale = requestWithQueryParam(Requests.LOCALE, "en_UK");
        assertThat(Requests.queryParamLocale(withLocale, Requests.LOCALE),
                   is(equalTo(Locale.forLanguageTag("en_UK"))));

        final Request withoutLocale = emptyRequest();
        assertThat(Requests.queryParamLocale(withoutLocale, Requests.LOCALE),
                   is(equalTo(Locale.US)));
    }

    @Test
    public void queryParamLocalDate() {
        final LocalDate now = LocalDate.now();

        final Request withLocale = requestWithQueryParam(Requests.LOCALE, "2015-12-21");
        assertThat(Requests.queryParamLocalDate(withLocale, Requests.LOCALE, () -> now),
                   is(equalTo(LocalDate.of(2015, 12, 21))));

        final Request withoutLocale = emptyRequest();
        assertThat(Requests.queryParamLocalDate(withoutLocale, Requests.LOCALE, () -> now),
                   is(equalTo(now)));
    }

    @Test
    public void queryParamTimeZone() {
        final Request withTimeZone = requestWithQueryParam(Requests.TIME_ZONE, "-8");
        assertThat(Requests.queryParamTimeZone(withTimeZone, Requests.TIME_ZONE),
                   is(equalTo(ZoneOffset.of("-8"))));

        final Request withoutTimeZone = emptyRequest();
        assertThat(Requests.queryParamTimeZone(withoutTimeZone, Requests.TIME_ZONE),
                   is(equalTo(ZoneOffset.UTC)));
    }
}
