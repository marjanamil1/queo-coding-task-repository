package features.output;

import exceptions.WriteErrorException;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StdOutWriterImplTest {

@Test
public void testWriteOutputWithValidInput() throws WriteErrorException {
    StdOutWriterImpl writer = new StdOutWriterImpl();

    String result = writer.writeOutput(Arrays.asList(2.0f, 2.1f, 3.0f, 4.5f));
    assertEquals("2.0,2.1,3.0,4.5", result);
}

    @Test
    public void testWriteOutputWithEmptyList() {
        StdOutWriterImpl writer = new StdOutWriterImpl();
        WriteErrorException thrown = assertThrows(WriteErrorException.class, () -> {
            writer.writeOutput(Collections.emptyList());
        });

        assertEquals("Failed to write output. Error code: 3", thrown.getMessage());
    }
@Test
public void testWriteOutputWithSingleElement() throws WriteErrorException {
    StdOutWriterImpl writer = new StdOutWriterImpl();

    String result = writer.writeOutput(Collections.singletonList(5.5f));
    assertEquals("5.5", result);
}
}