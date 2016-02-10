package nonsense.providers;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import nonsense.model.insights.Insight;

import static nonsense.NonsenseMatchers.greaterThan;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class RandomInsightSourceTest {
    private final ImageSource imageSource = category -> Optional.empty();
    private RandomInsightSource insightSource;

    @Before
    public void setUp() {
        this.insightSource = new RandomInsightSource(5);
    }

    @Test
    public void getInsights() {
        final List<Insight> insights = insightSource.getInsights(imageSource);
        assertThat(insights.size(), is(equalTo(5)));
        assertThat(insights.contains(null), is(false));
    }

    @Test
    public void generateInsight() {
        final Insight insight = insightSource.generateInsight(imageSource);
        assertThat(insight, is(notNullValue()));
        assertThat(insight.accountId, is(OptionalLong.empty()));
        assertThat(insight.category, is(notNullValue()));
        assertThat(insight.infoPreview.isPresent(), is(true));
        assertThat(insight.image.isPresent(), is(false));
        assertThat(insight.title, is(notNullValue()));
        assertThat(insight.message, is(notNullValue()));
        assertThat(insight.message.length(), is(greaterThan(0)));
    }

    @Test
    public void generateMessage() {
        final String message = insightSource.generateMessage();
        assertThat(message, is(notNullValue()));
        assertThat(message.length(), is(greaterThan(0)));
    }
}
