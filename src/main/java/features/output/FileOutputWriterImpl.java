package features.output;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exceptions.WriteErrorException;
import features.actions.ActionsManager;
import features.actions.ActionsManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
 * Implementation of OutputWriter interface for creating file of the output data in csv or json format
 */
public class FileOutputWriterImpl implements OutputWriter {

    private static final Logger logger = LoggerFactory.getLogger(FileOutputWriterImpl.class);
    private final String action;
    private final String format;
    private final String filePath;

    /**
     * Constructor
     * @param action
     * @param format
     * @param filePath
     */
    public FileOutputWriterImpl(String action, String format, String filePath) {
        this.action = action;
        this.format = format;
        this.filePath = filePath;
    }

    /**
     * Implementation of writeOutput method for creating a String of the output data
     * to be written in csv or json file
     *
     * @return a string which represents the output data
     */
    @Override
    public String writeOutput(List<Float> inputList) throws WriteErrorException {
        switch (format.toUpperCase()) {
            case "CSV":
                return writeToCSV(inputList, filePath);
            case "JSON":
                return writeToJson(inputList, filePath, action);
            default:
                throw new WriteErrorException("Invalid format of the output file: " + format, 4);
        }
    }

    private String writeToCSV(List<Float> inputList, String filePath) throws WriteErrorException {
        StringBuilder stringBuilder = new StringBuilder();

        List<Float> resultValues = perfomAction(inputList, action);

        if (resultValues.isEmpty()) {
            stringBuilder.append("\n");
        }
        for (Float f : resultValues) {
            stringBuilder.append(f).append(",");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1); // Remove last comma
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".csv"))) {
            logger.info("Opened connection to write output to a file.");
            writer.write(stringBuilder.toString());
            writer.newLine();
        } catch (IOException e) {
            logger.error("Error while writing to the CSV file {}: {}. Connection closed.", filePath, e.getMessage());
            throw new WriteErrorException("Failed to write to CSV file: " + filePath, e, 3);
        }

        logger.info("Successfully wrote to CSV file. Connection closed.");
        return stringBuilder.toString();
    }

    private String writeToJson(List<Float> inputList, String filePath, String action) throws WriteErrorException {
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        perfomJSONAction(inputList, action, jsonObject);
        return writeJsonToFile(gson.toJson(jsonObject) + "\n", filePath);
    }

    private String writeJsonToFile(String jsonString, String filePath) throws WriteErrorException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".json"))) {
            logger.info("Opened connection to write output to a file.");
            writer.write(jsonString);
            writer.newLine();
            logger.info("Successfully wrote to JSON file.");
        } catch (IOException e) {
            logger.error("Error while writing to JSON file {}: {}", filePath, e.getMessage());
            throw new WriteErrorException("Failed to write to JSON file: " + filePath, e, 3);
        }
        return jsonString;
    }

    private void perfomJSONAction(List<Float> inputList, String action, JsonObject jsonObject) throws WriteErrorException {
        ActionsManager actionsManager = new ActionsManagerImpl();
        Gson gson = new Gson();

        switch (action.toUpperCase()) {
            case "SUM":
                float sumValue = actionsManager.sum(inputList);
                jsonObject.addProperty(action.toUpperCase(), sumValue);
                break;
            case "MINMAX":
                float minValue = actionsManager.min(inputList);
                float maxValue = actionsManager.max(inputList);
                jsonObject.addProperty("MIN", minValue);
                jsonObject.addProperty("MAX", maxValue);
                break;
            case "LT4":
                jsonObject.addProperty("LT4", gson.toJson(inputList));
                break;
            default:
                logger.error("Unknown operation, will be ignored.");
                throw new WriteErrorException("Write Error: invalid operation, invalid action", 3);
        }
    }

}
