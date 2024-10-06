package features.input;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import exceptions.ReadErrorException;
import java.util.List;
class FileInputReaderImplTest {

    private static final String VALID_CSV_FILE_PATH = "src/test/resources/test_csv_input_valid.csv";
    private static final String VALID_JSON_FILE_PATH = "src/test/resources/test_json_input_valid.json";
    private static final String INVALID_CSV_FILE_PATH = "src/test/resources/test_csv_input_invalid.csv";
    private static final String INVALID_JSON_FILE_PATH = "src/test/resources/test_json_input_invalid.json";
    private static final String EMPTY_CSV_FILE_PATH = "src/test/resources/test_csv_input_empty.csv";
    private static final String EMPTY_JSON_FILE_PATH = "src/test/resources/test_json_input_empty.json";

    @Test
    void testReadValidCSVInput() throws ReadErrorException {
        FileInputReaderImpl reader = new FileInputReaderImpl(VALID_CSV_FILE_PATH, "csv");
        List<Float> result = reader.readInput();

        assertNotNull(result);
        assertEquals(6, result.size());
        assertEquals(Float.valueOf(1.0f), result.get(0));
        assertEquals(Float.valueOf(3.2f), result.get(1));
        assertEquals(Float.valueOf(4.3f), result.get(2));
        assertEquals(Float.valueOf(4.2f), result.get(3));
        assertEquals(Float.valueOf(2.1f), result.get(4));
        assertEquals(Float.valueOf(3.3f), result.get(5));
    }

    @Test
    void testReadValidJsonInput() throws ReadErrorException {
        FileInputReaderImpl reader = new FileInputReaderImpl(VALID_JSON_FILE_PATH, "json");
        List<Float> result = reader.readInput();

        assertNotNull(result);
        assertEquals(7, result.size());
        assertEquals(Float.valueOf(1.3f), result.get(0));
        assertEquals(Float.valueOf(2.1f), result.get(1));
        assertEquals(Float.valueOf(2.2f), result.get(2));
        assertEquals(Float.valueOf(1.3f), result.get(3));
        assertEquals(Float.valueOf(3.2f), result.get(4));
        assertEquals(Float.valueOf(6.0f), result.get(5));
        assertEquals(Float.valueOf(0.5f), result.get(6));
    }

    @Test
    void testUnsupportedFormatFile() {
        FileInputReaderImpl reader = new FileInputReaderImpl(VALID_CSV_FILE_PATH, "txt");
        Exception exception = assertThrows(ReadErrorException.class, reader::readInput);
        assertEquals("Unsupported format: txt", exception.getMessage());
    }

    @Test
    void testEmptyCSVFile() {
        FileInputReaderImpl reader = new FileInputReaderImpl(EMPTY_CSV_FILE_PATH, "csv");
        Exception exception = assertThrows(ReadErrorException.class, reader::readInput);
        assertEquals("Error: Input is empty", exception.getMessage());
    }

    @Test
    void testReadInvalidInconsistentCSVFile () {
        FileInputReaderImpl reader = new FileInputReaderImpl(INVALID_CSV_FILE_PATH, "csv");
        Exception exception = assertThrows(ReadErrorException.class, reader::readInput);
        assertEquals("Format Error: Inconsistent number of columns at line 2", exception.getMessage());
    }

    @Test
    void testEmptyJsonFile() {
        FileInputReaderImpl reader = new FileInputReaderImpl(EMPTY_JSON_FILE_PATH, "json");
        Exception exception = assertThrows(ReadErrorException.class, reader::readInput);
        assertEquals("Error: Input is empty.", exception.getMessage());
    }
    @Test
    void testInvalidJsonFile() {
        FileInputReaderImpl reader = new FileInputReaderImpl(INVALID_JSON_FILE_PATH, "json");
        Exception exception = assertThrows(ReadErrorException.class, reader::readInput);
        assertEquals("Invalid JSON format: java.io.EOFException: End of input at line 1 column 31 path $[7]", exception.getMessage());
    }

    @Test
    void testReadInvalidFileFormat() {
        FileInputReaderImpl reader = new FileInputReaderImpl("src/test/resources/invalid_path.txt", "txt");
        Exception exception = assertThrows(ReadErrorException.class, reader::readInput);
        assertEquals("Unsupported format: txt", exception.getMessage());
    }

    @Test
    void testReadFileNotFound() {
        FileInputReaderImpl reader = new FileInputReaderImpl("src/test/resources/not-existing-file.csv", "csv");
        Exception exception = assertThrows(ReadErrorException.class, reader::readInput);
        assertEquals("Read Error: File with path src/test/resources/not-existing-file.csv does not exist or is not readable.", exception.getMessage());
    }

}