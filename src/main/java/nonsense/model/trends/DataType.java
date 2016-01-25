package nonsense.model.trends;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

import nonsense.model.Condition;

public enum DataType {
    SCORES(40, 100,
           0, 100) {
        @Override
        public List<ConditionRange> getConditionRanges() {
            return Lists.newArrayList(new ConditionRange(0, 60, Condition.ALERT),
                                      new ConditionRange(60, 80, Condition.WARNING),
                                      new ConditionRange(80, 100, Condition.IDEAL));
        }
    },
    HOURS(4, 12,
          0, 24),
    PERCENTS(0.0, 1.0,
             0.0, 1.0);

    public final double generatedMin;
    public final double generatedMax;
    public final double actualMin;
    public final double actualMax;

    DataType(double generatedMin, double generatedMax,
             double actualMin, double actualMax) {
        this.generatedMin = generatedMin;
        this.generatedMax = generatedMax;
        this.actualMin = actualMin;
        this.actualMax = actualMax;
    }

    public List<ConditionRange> getConditionRanges() {
        return Collections.emptyList();
    }
}
