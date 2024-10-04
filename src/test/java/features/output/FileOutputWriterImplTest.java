package features.output;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import exceptions.WriteErrorException;
import features.actions.ActionsManager;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;

public class FileOutputWriterImplTest {

    private String action;
    private String format;
    private String filePath;
    private FileOutputWriterImpl outputWriter;
    private ActionsManager actionsManagerMock;

    @BeforeEach
    public void setUp() {
        action = "SUM";
        format = "CSV";
        filePath = "src/test/resources/test";
        actionsManagerMock = mock(ActionsManager.class);
        outputWriter = new FileOutputWriterImpl(action, format, filePath);
    }

    @Test
    public void testWriteCSV() throws WriteErrorException {
        List<Float> inputList = Arrays.asList(1.0f, 2.0f, 3.0f);

        String result = outputWriter.writeOutput(inputList);
        assertEquals("1.0,2.0,3.0", result);

        // Verify file creation and content (this part can be improved using temporary files)
        assertTrue(Files.exists(Paths.get(filePath + ".csv")));
    }

    @Test
    public void testWriteJSONSumAction() throws WriteErrorException {
        when(actionsManagerMock.sum(anyList())).thenReturn(6.0f);

        outputWriter = new FileOutputWriterImpl("SUM", "JSON", filePath);
        List<Float> inputList = Arrays.asList(1.0f, 2.0f, 3.0f);

        String result = outputWriter.writeOutput(inputList);
        assertTrue(result.contains("\"SUM\":6.0"));

        // Verify file creation
        assertTrue(Files.exists(Paths.get(filePath + ".json")));
    }

    @Test
    public void testInvalidFormat() {
        outputWriter = new FileOutputWriterImpl("SUM", "TXT", filePath);
        List<Float> inputList = Arrays.asList(1.0f, 2.0f, 3.0f);

        WriteErrorException exception = assertThrows(WriteErrorException.class, () -> {
            outputWriter.writeOutput(inputList);
        });
        assertEquals("Invalid format of the output file: TXT Error code: 4", exception.getMessage());
    }

    // TODO  Tests for the other Actions (MINMAX, LT4, for JSON and CSV format)
}
