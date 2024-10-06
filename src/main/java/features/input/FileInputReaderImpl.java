package features.input;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import exceptions.InvalidNumberFormatException;
import exceptions.ReadErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
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
        File file = new File(filePath);

        if (!file.exists() || !file.canRead()) {
            String errorMessage = "Read Error: File with path " + filePath +
                    " does not exist or is not readable.";
            logger.error(errorMessage);
            throw new ReadErrorException(errorMessage, 2);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int columnCount;
            int lineNumber = 0;

            // Read the first line to check if the file is empty
            line = br.readLine();
            if (line == null || line.trim().isEmpty()) {
                logger.error("CSV Input file {} is empty. Error code: 1", filePath);
                throw new ReadErrorException("Error: Input is empty", 1);
            }

            // Process the first line and initialize columnCount
            String[] valuesArray = line.split(",");
            columnCount = valuesArray.length;
            lineNumber++;

            // Read the values from the first line
            for (String column : valuesArray) {
                String sanitizedValue = column.trim();
                if (!sanitizedValue.isEmpty()) {
                    values.add(parseFloat(sanitizedValue));
                }
            }

            // Process subsequent lines
            while ((line = br.readLine()) != null) {
                lineNumber++;
                valuesArray = line.split(",");

                if (valuesArray.length != columnCount) {
                    String errorMessage = "Format Error: Inconsistent number of columns at line " + lineNumber;
                    logger.error(errorMessage + " Error code: 4");
                    throw new ReadErrorException(errorMessage, 4);
                }

                for (String column : valuesArray) {
                    String sanitizedValue = column.trim();
                    if (!sanitizedValue.isEmpty()) {
                        values.add(parseFloat(sanitizedValue));
                    }
                }
            }
        } catch (InvalidNumberFormatException e) {
            logger.error("Number Format Error {}", e.getMessage());
            throw new InvalidNumberFormatException(e.getMessage(), e, e.getErrorCode());
        } catch (ReadErrorException e) {
            throw new ReadErrorException(e.getMessage(), e, e.getErrorCode());
        } catch (IOException e) {
            logger.error("Read Error {} the file: {}", e.getMessage(), filePath);
            throw new ReadErrorException("Read Error ", e, 2);
        }

        logger.info("CSV file is valid and values are read successfully.");
        return values;
    }

    private List<Float> readJsonFile() throws ReadErrorException {
        validateFilePath();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Float> jsonList = new Gson().fromJson(reader, new TypeToken<List<Float>>(){}.getType());

            // Check if the result is empty
            if (jsonList == null || jsonList.isEmpty()) {
                throw new ReadErrorException("Error: Input is empty.", 1);
            }
            return jsonList;
        } catch (JsonParseException e) {
            throw new ReadErrorException("Invalid JSON format: " + e.getMessage(), e, 4);
        } catch (ReadErrorException e) {
            throw new ReadErrorException(e.getMessage(), e, e.getErrorCode());
        } catch (IOException e) {
            throw new ReadErrorException("Error while reading the file " + filePath, e, 2);
        }
    }

    private void validateFilePath() throws ReadErrorException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new ReadErrorException("File path cannot be empty", 1);
        }
    }

    private Float parseFloat(String value) throws InvalidNumberFormatException {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new InvalidNumberFormatException("Invalid number format: " + value, e, 4);
        }
    }
}