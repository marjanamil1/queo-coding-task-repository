package features.output;

import exceptions.WriteErrorException;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StdOutWriterImplTest {

    private final String action1 = "SUM";
    private final String action2 = "MINMAX";
    private final String action3 = "LT4";
    @Test
    public void testWriteMINMAXOutputWithValidInput() throws WriteErrorException {
        StdOutWriterImpl writer = new StdOutWriterImpl(action2);

        String result = writer.writeOutput(Arrays.asList(2.0f, 2.1f, 3.0f, 4.5f));
        assertEquals("2.0,4.5", result);
    }

    @Test
    public void testWriteSUMOutputWithValidInput() throws WriteErrorException {
        StdOutWriterImpl writer = new StdOutWriterImpl(action1);

        String result = writer.writeOutput(Arrays.asList(2.0f, 2.1f, 3.0f, 4.5f));
        assertEquals("11.6", result);
    }

    @Test
    public void testWriteOutputWithEmptyList() {
        StdOutWriterImpl writer = new StdOutWriterImpl(action1);
        WriteErrorException thrown = assertThrows(WriteErrorException.class, () -> {
            writer.writeOutput(Collections.emptyList());
        });

        assertEquals("Input list cannot be empty.", thrown.getMessage());
    }

    @Test
    public void testWriteOutputWithSingleElement() throws WriteErrorException {
        StdOutWriterImpl writer = new StdOutWriterImpl(action1);

        String result = writer.writeOutput(Collections.singletonList(5.5f));
        assertEquals("5.5", result);
    }
}