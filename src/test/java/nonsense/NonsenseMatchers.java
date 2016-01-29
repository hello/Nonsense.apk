package nonsense;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class NonsenseMatchers {
    //region Factories

    @Factory
    public static <T extends Comparable<T>> Matcher<T> greaterThan(T right) {
        return new GreaterThan<>(right);
    }

    @Factory
    public static <T extends Comparable<T>> Matcher<T> greaterThanOrEqual(T right) {
        return anyOf(greaterThan(right), equalTo(right));
    }

    @Factory
    public static <T extends Comparable<T>> Matcher<T> lessThan(T right) {
        return new LessThan<>(right);
    }

    @Factory
    public static <T extends Comparable<T>> Matcher<T> lessThanOrEqual(T right) {
        return anyOf(lessThan(right), equalTo(right));
    }

    @Factory
    public static <T> Matcher<T> emptyString() {
        return new EmptyString<>();
    }

    //endregion


    //region Implementations

    public static class GreaterThan<T extends Comparable<T>> extends BaseMatcher<T> {
        private T right;

        GreaterThan(T right) {
            this.right = right;
        }

        @Override
        public boolean matches(Object left) {
            //noinspection unchecked
            return (right.getClass().isAssignableFrom(left.getClass()) &&
                    (right.compareTo((T) left) < 0));
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("greater than ");
            description.appendValue(right);
        }
    }

    public static class LessThan<T extends Comparable<T>> extends BaseMatcher<T> {
        private T right;

        LessThan(T right) {
            this.right = right;
        }

        @Override
        public boolean matches(Object left) {
            //noinspection unchecked
            return (right.getClass().isAssignableFrom(left.getClass()) &&
                    (right.compareTo((T) left) > 0));
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("greater than ");
            description.appendValue(right);
        }
    }

    public static class EmptyString<T> extends BaseMatcher<T> {
        @Override
        public boolean matches(Object o) {
            return (o == null || o.toString().isEmpty());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("empty string");
        }
    }

    //endregion
}
