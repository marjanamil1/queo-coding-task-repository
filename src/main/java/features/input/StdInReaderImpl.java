package features.input;

import exceptions.ReadErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
        logger.info("Opening connection to read input from stdin.");

        System.out.println("Enter float numbers separated by \",\", " +
                "\n Blank spaces are allowed and will be ignored. " +
                "\n To end the list press CTRL+Z (Windows) or CTRL+D (Unix):");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
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
                    throw new ReadErrorException(1); // Error code 1 for empty input error
                }

                String[] values = input.split(",");
                for (String value : values) {
                    try {
                        inputList.add(Float.parseFloat(value));
                        inputReceived = true;
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid float number: {}", value, e);
                        throw new ReadErrorException("Format error for value: " + value, 4); // Error code 4 for format error
                    }
                }
            }

            if (!inputReceived) {
                throw new ReadErrorException(1); // Error code 1 for no valid input received
            }

        } catch (IOException e) {
            logger.error("IOException occurred while reading input.", e);
            throw new ReadErrorException(e, 2); // Error code 2 for read error
        }
        logger.info("Closing connection to read input from stdin.");
        return inputList;
    }
}
