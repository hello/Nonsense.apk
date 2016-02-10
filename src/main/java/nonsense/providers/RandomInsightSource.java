package nonsense.providers;

import net._01001111.text.LoremIpsum;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nonsense.model.insights.Category;
import nonsense.model.insights.Insight;
import nonsense.util.Enums;
import nonsense.util.RandomUtil;
import nonsense.util.Requests;

public class RandomInsightSource implements InsightSource {
    private final LoremIpsum textGenerator = new LoremIpsum();
    private final Random random = new Random();
    private final int numberOfInsights;

    public RandomInsightSource(int numberOfInsights) {
        this.numberOfInsights = numberOfInsights;
    }

    public static InsightSource.Factory createFactory() {
        return r -> new RandomInsightSource(Requests.queryParamInteger(r, Requests.LIMIT, 5));
    }

    @Override
    public List<Insight> getInsights(ImageSource imageSource) {
        return Stream.generate(() -> generateInsight(imageSource))
                     .limit(numberOfInsights)
                     .collect(Collectors.toList());
    }

    public Insight generateInsight(ImageSource imageSource) {
        final Category category = Enums.random(Category.values());
        return new Insight(OptionalLong.empty(),
                           category.title,
                           generateMessage(),
                           category,
                           ZonedDateTime.now(),
                           Optional.of(category.title),
                           imageSource.getImageForCategory(category));
    }

    public String generateMessage() {
        final int paragraphCount = RandomUtil.integerInRange(random, 2, 6);
        return textGenerator.paragraphs(paragraphCount);
    }
}
