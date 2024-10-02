package features.output;

import exceptions.WriteErrorException;

import java.util.List;

/**
 * This interface provides a contract for writing the output data as a string
 */
public interface OutputWriter {

    String writeOutput (List<Float> inputList) throws WriteErrorException;
}
