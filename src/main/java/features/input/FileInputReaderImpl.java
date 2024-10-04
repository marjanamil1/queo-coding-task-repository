package features.input;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import exceptions.ReadErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Input Reader for reading input from file
 */

public class FileInputReaderImpl implements InputReader {

    private static final Logger logger = LoggerFactory.getLogger(FileInputReaderImpl.class);
    private final String filePath;
    private final String format;

    public FileInputReaderImpl(String filePath, String format) {
        this.filePath = filePath;
        this.format = format;
    }

    /**
     * Implementation of readInput method for reading input data from file
     * returns it as a list of Float values.
     */
    @Override
    public List<Float> readInput() throws ReadErrorException {
        logger.info("Please enter the path to the file read from. " +
                "Acceptable formats are csv and json. " +
                "Other formats will result in a read error.");

        switch (format.toLowerCase()) {
            case "csv":
                return readCsvFile();
            case "json":
                return readJsonFile();
            default:
                throw new ReadErrorException("Unsupported format: " + format, 4);
        }
    }

    private List<Float> readCsvFile() throws ReadErrorException {
        validateFilePath();

        List<Float> values = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            validateCsvHeader(headerLine);

            String line;
            while ((line = br.readLine()) != null) {
                for (String column : line.split(",")) {
                    String sanitizedValue = column.trim();
                    if (!sanitizedValue.isEmpty()) {
                        values.add(parseFloat(sanitizedValue));
                    }
                }
            }
        } catch (IOException e) {
            throw new ReadErrorException("Error while reading the file " + filePath, e, 2);
        }
        return values;
    }

    private List<Float> readJsonFile() throws ReadErrorException {
        validateFilePath();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return new Gson().fromJson(reader, new TypeToken<List<Float>>(){}.getType());
        } catch (JsonParseException e) {
            throw new ReadErrorException("Invalid JSON format: " + e.getMessage(), e, 4);
        } catch (IOException e) {
            throw new ReadErrorException("Error while reading the file " + filePath, e, 2);
        }
    }

    private void validateFilePath() throws ReadErrorException {
        if (filePath.trim().isEmpty()) {
            throw new ReadErrorException("File path cannot be empty", 1);
        }
    }

    private void validateCsvHeader(String headerLine) throws ReadErrorException {
        if (headerLine == null || headerLine.trim().isEmpty()) {
            throw new ReadErrorException("The CSV file is empty.", 4);
        }
    }

    private Float parseFloat(String value) throws ReadErrorException {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new ReadErrorException("Invalid number format: " + value, e, 4);
        }
    }
}