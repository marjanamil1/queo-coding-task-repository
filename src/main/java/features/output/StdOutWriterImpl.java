package features.output;

import exceptions.WriteErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

public class StdOutWriterImpl implements OutputWriter {

    private static final Logger logger = LoggerFactory.getLogger(StdOutWriterImpl.class);
    /**
     * Implementation of writeOutput method for creating a String of the output data to be printed on the screen
     * No operations on the data are done at this point, this is just to write the passed
     * list to string and display it on the output device as coma separated values
     *
     * @return a string which represents the output data
     */
    public String writeOutput(List<Float> inputList) throws WriteErrorException {
        String outputString;

        try {
            if (inputList == null || inputList.isEmpty()) {
                logger.error("Input list is empty.");
                throw new WriteErrorException("Input list cannot be empty.", 1);
            }
            outputString = inputList.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            logger.info("List of the numbers provided for the output operation: {}", outputString);
            return outputString;

        } catch (Exception e) {
            logger.error("An error occurred while generating output string.", e);
            throw new WriteErrorException("Failed to write output.", e, 3);
        } finally {
            logger.info("Closing connection to write std output.");
        }
    }
}
