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

    private final String action1 = "SUM";
    private final String action2 = "MINMAX";
    private final String action3 = "LT4";
    private final String format1 = "CSV";
    private final String format2 = "JSON";
    private final String filePath = "src/test/resources/test";
    private FileOutputWriterImpl outputWriter;
    private ActionsManager actionsManagerMock;

    @BeforeEach
    public void setUp() {
        actionsManagerMock = mock(ActionsManager.class);
    }

    @Test
    public void testWriteSUMCSV() throws WriteErrorException {
        outputWriter = new FileOutputWriterImpl(action1, format1, filePath);
        List<Float> inputList = Arrays.asList(1.0f, 2.0f, 3.0f);

        String result = outputWriter.writeOutput(inputList);
        assertEquals("6.0", result);
        assertTrue(Files.exists(Paths.get(filePath + ".csv")));
    }

    @Test
    public void testWriteMINMAXCSV() throws WriteErrorException {
        outputWriter = new FileOutputWriterImpl(action2, format1, filePath);
        List<Float> inputList = Arrays.asList(1.0f, 2.0f, 3.0f);

        String result = outputWriter.writeOutput(inputList);
        assertEquals("1.0,3.0", result);
        assertTrue(Files.exists(Paths.get(filePath + ".csv")));
    }

    @Test
    public void testWriteLT4CSV() throws WriteErrorException {
        outputWriter = new FileOutputWriterImpl(action3, format1, filePath);
        List<Float> inputList = Arrays.asList(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);

        String result = outputWriter.writeOutput(inputList);
        assertEquals("1.0,2.0,3.0", result);
        assertTrue(Files.exists(Paths.get(filePath + ".csv")));
    }

    @Test
    public void testWriteJSONSumAction() throws WriteErrorException {
        when(actionsManagerMock.sum(anyList())).thenReturn(6.0f);

        outputWriter = new FileOutputWriterImpl(action1, format2, filePath);
        List<Float> inputList = Arrays.asList(1.0f, 2.0f, 3.0f);

        String result = outputWriter.writeOutput(inputList);
        assertTrue(result.contains("\"SUM\":6.0"));

        assertTrue(Files.exists(Paths.get(filePath + ".json")));
    }

    @Test
    public void testWriteJSONMinMaxAction() throws WriteErrorException {

        List<Float> inputList = Arrays.asList(1.0f, 2.0f, 3.0f);
        when(actionsManagerMock.min(inputList)).thenReturn(1.0f);
        when(actionsManagerMock.min(inputList)).thenReturn(3.0f);

        outputWriter = new FileOutputWriterImpl(action2, format2, filePath);

        String result = outputWriter.writeOutput(inputList);
        assertTrue(result.contains("\"MIN\":1.0") && result.contains("\"MAX\":3.0"));

        assertTrue(Files.exists(Paths.get(filePath + ".json")));
    }

    @Test
    public void testWriteJSONLT4Action() throws WriteErrorException {

        List<Float> inputList = Arrays.asList(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        when(actionsManagerMock.filter(inputList)).thenReturn(Arrays.asList(1.0f, 2.0f, 3.0f));
        outputWriter = new FileOutputWriterImpl(action3, format2, filePath);
        String result = outputWriter.writeOutput(inputList);
        assertTrue(result.contains("1.0,2.0,3.0"));

        assertTrue(Files.exists(Paths.get(filePath + ".json")));
    }

    @Test
    public void testInvalidFormat() {
        outputWriter = new FileOutputWriterImpl("SUM", "txt", filePath);
        List<Float> inputList = Arrays.asList(1.0f, 2.0f, 3.0f);

        WriteErrorException exception = assertThrows(WriteErrorException.class, () -> {
            outputWriter.writeOutput(inputList);
        });
        assertEquals("Invalid format of the output file: txt", exception.getMessage());
    }

    // TODO  Tests for the other Actions (MINMAX, LT4, for JSON and CSV format)
}
