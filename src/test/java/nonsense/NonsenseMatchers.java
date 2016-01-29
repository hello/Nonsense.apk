package nonsense;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class NonsenseMatchers {
    public static <T extends Number & Comparable<T>> Matcher<T> greaterThan(T right) {
        return new GreaterThan<>(right);
    }

    public static <T extends Number & Comparable<T>> Matcher<T> greaterThanOrEqual(T right) {
        return anyOf(greaterThan(right), equalTo(right));
    }

    public static <T extends Number & Comparable<T>> Matcher<T> lessThan(T right) {
        return new LessThan<>(right);
    }

    public static <T extends Number & Comparable<T>> Matcher<T> lessThanOrEqual(T right) {
        return anyOf(lessThan(right), equalTo(right));
    }

    public static class GreaterThan<T extends Number & Comparable<T>> extends BaseMatcher<T> {
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

    public static class LessThan<T extends Number & Comparable<T>> extends BaseMatcher<T> {
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
}
