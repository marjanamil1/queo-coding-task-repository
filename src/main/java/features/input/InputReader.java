package features.input;

import exceptions.ReadErrorException;

import java.io.IOException;
import java.util.List;

/**
 * This interface provides a contract for reading input data
 * and returning it as a list of Float values.
 */
 public interface InputReader {
    /**
     * Reads input data from the specified source and returns it as a list of
     * Float values.
     *
     * @return a list containing the parsed float values.
     */
    List<Float> readInput() throws ReadErrorException;

    /**
     * Removes all blank spaces in the string.
     * This method can be used by all implementations of this interface.
     *
     * @param value the input string to sanitize
     * @return a sanitized string without any whitespace characters
     */
    default String removeBlankSpaces(String value) {
        return value.replaceAll("\\s+", "");
    }
}
