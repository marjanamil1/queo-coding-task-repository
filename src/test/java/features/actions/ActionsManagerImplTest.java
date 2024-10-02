package features.actions;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActionsManagerImplTest {

    private ActionsManagerImpl actionsManager;

    @Before
    public void setUp() {
        actionsManager = new ActionsManagerImpl();
    }

    @Test
    public void testSumValidList() {
        List<Float> inputList = Arrays.asList(1.0f, 2.0f, 3.0f);
        assertEquals(6.0f, actionsManager.sum(inputList), 0.0);
    }
    @Test
    public void testSumNullInput() {
        assertEquals(0, actionsManager.sum(null), 0.0);
    }

    @Test
    public void testSumEmptyList() {
        assertEquals(0, actionsManager.sum(Collections.emptyList()), 0.0);
    }

    @Test
    public void testSumWithNullElements() {
        List<Float> inputList = Arrays.asList(1.5f, null, 3.77f);
        assertEquals(5.27f, actionsManager.sum(inputList), 0.0);
    }

    @Test
    public void testMinWithNullInput() {
        assertEquals(0, actionsManager.min(null), 0.0);
    }

    @Test
    public void testMinEmptyList() {
        assertEquals(0, actionsManager.min(Collections.emptyList()), 0.0);
    }

    @Test
    public void testMinValidList() {
        List<Float> inputList = Arrays.asList(9.0f, 2.1f, 3.1f,5.3f);
        assertEquals(2.1f, actionsManager.min(inputList), 0.0);
    }

    @Test
    public void testMinWithNullElements() {
        List<Float> inputList = Arrays.asList(null, 3.5f, 1.5f);
        assertEquals(1.5f, actionsManager.min(inputList), 0.0);
    }

    @Test
    public void testMaxNullInput() {
        assertEquals(0, actionsManager.max(null), 0.0);
    }

    @Test
    public void testMaxEmptyList() {
        assertEquals(0, actionsManager.max(Collections.emptyList()), 0.0);
    }

    @Test
    public void testMaxValidList() {
        List<Float> inputList = Arrays.asList(1.0f, 3.0f, 2.0f);
        assertEquals(3.0f, actionsManager.max(inputList), 0.0);
    }

    @Test
    public void testMaxWithNullElements() {
        List<Float> inputList = Arrays.asList(null, 3.0f, 1.0f);
        assertEquals(3.0f, actionsManager.max(inputList), 0.0);
    }
    @Test
    public void testFilterValidList() {
        List<Float> inputList = Arrays.asList(1.5f, 4.0f, 3.7f, 2.0f);
        List<Float> expectedOutput = Arrays.asList(1.5f,3.7f, 2.0f);
        assertEquals(expectedOutput, actionsManager.filter(inputList));
    }
    @Test
    public void testFilterEmptyList() {
        assertEquals(Collections.emptyList(), actionsManager.filter(Collections.emptyList()));
    }
    @Test
    public void testFilterWithNullValues() {
        List<Float> inputList = Arrays.asList(null, 2.5f, 4.0f, 0.2f, 3.9f);
        List<Float> expectedOutput = Arrays.asList(2.5f, 0.2f, 3.9f);
        assertEquals(expectedOutput, actionsManager.filter(inputList));
    }

    @Test
    public void testFilterNullInput() {
        assertEquals(Collections.emptyList(), actionsManager.filter(null));
    }

}
