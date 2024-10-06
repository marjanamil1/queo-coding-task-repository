package features.output;

import exceptions.WriteErrorException;
import features.actions.ActionsManager;
import features.actions.ActionsManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * This interface provides a contract for writing the output data as a string
 */
public interface OutputWriter {

    String writeOutput (List<Float> inputList) throws WriteErrorException;
    Logger logger = LoggerFactory.getLogger(OutputWriter.class);

    default List<Float> perfomAction(List<Float> inputList, String action) throws WriteErrorException {
        ActionsManager actionsManager = new ActionsManagerImpl();
        float sum;
        float minValue;
        float maxValue;
        List<Float> filteredValues;
        List<Float> resultValues = new ArrayList<>();

        switch (action.toUpperCase()) {
            case "SUM":
                sum = actionsManager.sum(inputList);
                resultValues.add(sum);
                break;
            case "MINMAX":
                minValue = actionsManager.min(inputList); // 0 if the list is empty or null
                maxValue = actionsManager.max(inputList); // 0 if the list is empty or null
                resultValues.add(minValue);
                resultValues.add(maxValue);
                break;
            case "LT4":
                filteredValues = actionsManager.filter(inputList);
                resultValues.addAll(filteredValues);
                break;
            default:
                logger.error("Unknown operation, will be ignored.");
                throw new WriteErrorException("Write Error: invalid operation, invalid action", 3);
        } return resultValues;
    }
}
