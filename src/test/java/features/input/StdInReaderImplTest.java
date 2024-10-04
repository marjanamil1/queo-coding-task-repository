package features.input;

import exceptions.ReadErrorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.util.List;
import java.io.ByteArrayInputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StdInReaderImplTest {
    private StdInReaderImpl stdInReader;

    private final InputStream originalIn = System.in; // Store the original System.in
    @BeforeEach
    void setUp() {
        stdInReader = new StdInReaderImpl();
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
    }

    @Test
    void testReadInputValidInput() throws ReadErrorException {
            String simulatedInput = "2.1, 4.2, 0.21,7.5,0.2\n";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

            List<Float> result = stdInReader.readInput();
            assertEquals(5, result.size());
            assertEquals(2.1f, result.get(0));
            assertEquals(4.2f, result.get(1));
            assertEquals(0.21f, result.get(2));
            assertEquals(7.5f, result.get(3));
            assertEquals(0.2f, result.get(4));
    }

    @Test
    void testReadInput_IOException() {
        System.setIn(new ByteArrayInputStream(new byte[0]) {
            @Override
            public int read() {
                throw new RuntimeException("Simulated IOException");
            }
        });

        ReadErrorException exception = assertThrows(ReadErrorException.class, () -> {
            stdInReader.readInput();
        });
        assertEquals(2, exception.getErrorCode());
        }
}
