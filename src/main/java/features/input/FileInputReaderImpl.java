package features.input;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.ReadErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
     *
     * @return a list containing the parsed float values.
     */
    @Override
    public List<Float> readInput() throws ReadErrorException {
        System.out.println("Please enter the path to the file read from. " +
                "\n Acceptable formats are csv and json. " +
                "\n Other formats will result with a read error. ");

        if (format.equalsIgnoreCase("csv")) {
            try {
                if (!validateCsv(filePath)) {
                    throw new ReadErrorException("Invalid file format", 4);
                }
                return readCSV(filePath);
            } catch (IOException e) {
                throw new ReadErrorException("Error while reading the file, {}", e, 2);
            }
        } else if (format.equalsIgnoreCase("json")) {
            try {
                return readJsonFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }

    /**
     * Method to read CSV file by processing all the columns as coma separated list of values
     * where the blank spaces are ignored, and return a list of floats
     */
    private List<Float> readCSV(String filePath) throws ReadErrorException {
        if (!validateJson(filePath)) {
            throw new ReadErrorException("Invalid file format", 4);
        }
        List<Float> values = new ArrayList<>();

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new ReadErrorException(1); // Input is empty
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                return values;
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                for (String column : columns) {
                    // Remove all whitespace characters
                    String sanitizedValue = removeBlankSpaces(column);
                    if (!sanitizedValue.isEmpty()) {
                        try {
                            Float parsedValue = validateAndParseFloat(sanitizedValue);
                            values.add(parsedValue);
                        } catch (ReadErrorException e) {
                            logger.error("Invalid number format {}, exiting the application with status 4", sanitizedValue);
                            System.exit(4);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new ReadErrorException("File not found", 2); // Read error
        } catch (IOException e) {
            throw new ReadErrorException("Error reading the file", e, 2); // Read error
        }
        return values;
    }

    /**
     * Method to read JSON file by parsing the values as array of floating point numbers
     */
    private List<Float> readJsonFile() throws ReadErrorException {
        List<Float> floatList = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Gson gson = new Gson();
            floatList = gson.fromJson(reader, new TypeToken<List<Float>>(){}.getType());
        } catch (FileNotFoundException e) {
            logger.error("Error reading the file {}, file not found, exiting with code 2", filePath);
            throw new ReadErrorException("File not found: " + filePath, e, 2);
        } catch (IOException e) {
            logger.error("Error reading the file {}, exiting with code 2", filePath);
            System.exit(2);
        }
        return floatList;
    }

    /**
     * Validate if String value can be parsed to float
     */
    private Float validateAndParseFloat(String value) throws ReadErrorException {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            logger.warn("Invalid number format: " + value, e);
            throw new ReadErrorException("Invalid number format", e, 4);
        }
    }

    /**
     * Validate CSV file format
     */
    private Boolean validateCsv(String filePath) {
        return filePath.toLowerCase().endsWith(".csv");
    }

    /**
     * Validate JSON file format
     */
    private Boolean validateJson(String filePath) {
        return filePath.toLowerCase().endsWith(".json");
    }

}