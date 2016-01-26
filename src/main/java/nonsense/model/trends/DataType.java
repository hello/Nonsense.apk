package nonsense.model.trends;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import nonsense.model.Condition;

public enum DataType {
    SCORES(40, 100,
           0, 100,
           true) {
        @Override
        public List<ConditionRange> getConditionRanges() {
            return Lists.newArrayList(new ConditionRange(0, 60, Condition.ALERT),
                                      new ConditionRange(60, 80, Condition.WARNING),
                                      new ConditionRange(80, 100, Condition.IDEAL));
        }
    },
    HOURS(4, 12,
          0, 24,
          true),
    PERCENTS(0.0, 1.0,
             0.0, 1.0,
             false);

    public final double generatedMin;
    public final double generatedMax;
    public final double actualMin;
    public final double actualMax;
    public final boolean roundValues;

    DataType(double generatedMin, double generatedMax,
             double actualMin, double actualMax,
             boolean roundValues) {
        this.generatedMin = generatedMin;
        this.generatedMax = generatedMax;
        this.actualMin = actualMin;
        this.actualMax = actualMax;
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

    public Function<Double, Condition> createConditionProvider() {
        final List<ConditionRange> conditionRanges = getConditionRanges();
        if (conditionRanges.isEmpty()) {
            return ignored -> Condition.UNKNOWN;
        } else {
            return value -> {
                for (final ConditionRange range : conditionRanges) {
                    if (value >= range.minValue && value <= range.maxValue) {
                        return range.condition;
                    }
                }

                return Condition.UNKNOWN;
            };
        }
    }
}
