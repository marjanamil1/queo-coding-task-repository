package features.output;

import exceptions.WriteErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

public class StdOutWriterImpl implements OutputWriter {

    private static final Logger logger = LoggerFactory.getLogger(StdOutWriterImpl.class);

    private final String action;

    /**
     * Constructor
     * @param action action to be performed on the data (sum, minmax or ltf)
     */
    public StdOutWriterImpl(String action) {
        this.action = action;
    }

    /**
     * Implementation of writeOutput method for creating a String of the output data to be printed on the screen
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
            List<Float> outputList = perfomAction(inputList, action);
            outputString = outputList.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")) + "\n";

            logger.info("List of the numbers provided for the output operation: {}", outputString);
            return outputString;

        } finally {
            logger.info("Closing connection to write std output.");
        }
    }
}
