package features.input;

import exceptions.InvalidNumberFormatException;
import exceptions.ReadErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Input Reader for reading input from stdin
 */
public class StdInReaderImpl implements InputReader {
    private static final Logger logger = LoggerFactory.getLogger(StdInReaderImpl.class);

    /**
     * Implementation of readInput method for reading input data from the
     * stdin (standard input device - keyboard) and returns it as a list of Float values.
     *
     * @return a list containing the parsed float values.
     */
    public List<Float> readInput() throws ReadErrorException {
        List<Float> inputList = new ArrayList<>();

        System.out.println("Enter float numbers separated by \",\" " +
                "\n Blank spaces are allowed and will be ignored. " +
                "\n To end the list press CTRL+Z (Windows) or CTRL+D (Unix):");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            logger.info("Opening connection to read input from stdin.");
            String input;
            boolean inputReceived = false;

            while (true) {
                input = reader.readLine();
                // Check for (CTRL+Z or CTRL+D) for end of input
                if (input == null) {
                    break;
                }
                input = removeBlankSpaces(input);

                if (input.isEmpty()) {
                    throw new ReadErrorException(1);
                }

                String[] values = input.split(",");
                for (String value : values) {
                    try {
                        inputList.add(Float.parseFloat(value));
                        inputReceived = true;
                    } catch (NumberFormatException e) {
                        logger.error("Invalid float number: {}", value, e);
                        throw new InvalidNumberFormatException("Format error for value: " + value, e, 4);
                    }
                }
            }
            if (!inputReceived) {
                throw new ReadErrorException(1);
            }
        } catch (IOException e) {
            logger.error("IOException occurred while reading input.", e);
            throw new ReadErrorException(e, 2);
        } finally {
            logger.info("Closing connection to read input from stdin.");
        }
        return inputList;
    }

    /**
     * Removes all blank spaces in the string.
     * This method can be used by all implementations of this interface.
     *
     * @param value the input string to sanitize
     * @return a sanitized string without any whitespace characters
     */
    private String removeBlankSpaces(String value) {
        return value.replaceAll("\\s+", "");
    }
}
