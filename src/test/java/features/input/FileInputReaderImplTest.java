package features.input;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import exceptions.ReadErrorException;
import java.util.List;
class FileInputReaderImplTest {

    private static final String CSV_FILE_PATH = "src/test/resources/test_csv_input_valid.csv";
    private static final String JSON_FILE_PATH = "src/test/resources/test_json_input_valid.json";

    @Test
    void testReadValidCSVInput() throws ReadErrorException {
        FileInputReaderImpl reader = new FileInputReaderImpl(CSV_FILE_PATH, "csv");
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
        FileInputReaderImpl reader = new FileInputReaderImpl(JSON_FILE_PATH, "json");
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
    void testReadEmptyCSVFile() {

    }

    @Test
    void testReadInvalidInconsistentCSVFile () {

    }

    @Test
    void testInvalidJsonFile() {

    }

    @Test
    void testReadInvalidFileFormat() {
        FileInputReaderImpl reader = new FileInputReaderImpl("invalid_path.txt", "txt");
        Exception exception = assertThrows(ReadErrorException.class, reader::readInput);
        assertEquals("Unsupported format: txt Error code: 4", exception.getMessage());
    }

    @Test
    void testReadFileNotFound() {
        FileInputReaderImpl reader = new FileInputReaderImpl("not-existing-file.csv", "csv");
        Exception exception = assertThrows(ReadErrorException.class, reader::readInput);
        assertEquals("Error while reading the file not-existing-file.csv Error code: 2", exception.getMessage());
    }

}