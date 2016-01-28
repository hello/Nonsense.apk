package nonsense.model.trends;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import nonsense.model.Condition;

public enum DataType {
    SCORES(40, 100, true) {
        @Override
        public List<ConditionRange> getConditionRanges() {
            return Lists.newArrayList(new ConditionRange(0, 59, Condition.ALERT),
                                      new ConditionRange(60, 79, Condition.WARNING),
                                      new ConditionRange(80, 100, Condition.IDEAL));
        }
    },
    HOURS(4, 12, true),
    PERCENTS(0.0, 1.0, false);

    public final double generatedMin;
    public final double generatedMax;
    public final boolean roundValues;

    DataType(double generatedMin, double generatedMax, boolean roundValues) {
        this.generatedMin = generatedMin;
        this.generatedMax = generatedMax;
        this.roundValues = roundValues;
    }

    public List<ConditionRange> getConditionRanges() {
        return Collections.emptyList();
    }

    public Optional<Condition> getConditionForValue(double value) {
        final List<ConditionRange> conditionRanges = getConditionRanges();
        if (conditionRanges.isEmpty()) {
            return Optional.empty();
        } else {
            for (final ConditionRange range : conditionRanges) {
                if (value >= range.minValue && value <= range.maxValue) {
                    return Optional.of(range.condition);
                }
            }

            return Optional.empty();
        }
    }

}
