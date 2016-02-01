package nonsense.model.trends;

import com.google.common.collect.Lists;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class GraphTest {
    @Test
    public void newSleepScore() {
        final List<Annotation> annotations = Lists.newArrayList();
        final Graph graph = Graph.newSleepScore(TimeScale.LAST_WEEK, GraphType.GRID, Collections.emptyList(), annotations);
        assertThat(graph.title, is(equalTo("Sleep Score")));
        assertThat(graph.dataType, is(equalTo(DataType.SCORES)));
        assertThat(graph.graphType, is(equalTo(GraphType.GRID)));
        assertThat(graph.annotations, is(sameInstance(annotations)));
    }

    @Test
    public void newSleepDuration() {
        final List<Annotation> annotations = Lists.newArrayList();
        final Graph graph = Graph.newSleepDuration(TimeScale.LAST_WEEK, Collections.emptyList(), annotations);
        assertThat(graph.title, is(equalTo("Sleep Duration")));
        assertThat(graph.dataType, is(equalTo(DataType.HOURS)));
        assertThat(graph.graphType, is(equalTo(GraphType.BAR)));
        assertThat(graph.annotations, is(sameInstance(annotations)));
    }

    @Test
    public void newSleepDepth() {
        final Graph graph = Graph.newSleepDepth(TimeScale.LAST_WEEK, Collections.emptyList());
        assertThat(graph.title, is(equalTo("Sleep Depth")));
        assertThat(graph.dataType, is(equalTo(DataType.PERCENTS)));
        assertThat(graph.graphType, is(equalTo(GraphType.BUBBLES)));
        assertThat(graph.annotations.isEmpty(), is(true));
    }
}
