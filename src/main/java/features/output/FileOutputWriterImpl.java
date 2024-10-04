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

public class FileOutputWriterImpl implements OutputWriter {

    private static final Logger logger = LoggerFactory.getLogger(FileOutputWriterImpl.class);
    private final String action;
    private final String format;
    private final String filePath;

    public FileOutputWriterImpl(String action, String format, String filePath) {
        this.action = action;
        this.format = format;
        this.filePath = filePath;
    }

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

    private String writeToCSV(List<Float> list, String filePath) {
        StringBuilder stringBuilder = new StringBuilder();

        // Convert the list to a comma-separated string
        for (Float f : list) {
            stringBuilder.append(f).append(",");
        }
        // Remove the last comma
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        // Write the string to a CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".csv"))) {
            logger.info("Opened connection to write output to a file.");
            writer.write(stringBuilder.toString());
            writer.newLine();
        } catch (IOException e) {
            logger.error("Error while writing to the file {}, exiting with code 3.", e.getMessage());
            System.exit(3);
        } finally { // this block is not necessary since we use try with resources, it is for logging purposes only
            logger.info("Closing connection to write output to csv file.");
        }
        return stringBuilder.toString();
    }

    private String writeToJson(List<Float> inputList, String filePath, String action) {
        JsonObject jsonObject = new JsonObject();
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
                return writeJsonToFile(gson.toJson(inputList), filePath);
            default:
                return null;
        }
        return writeJsonToFile(gson.toJson(jsonObject), filePath);
    }

    private String writeJsonToFile(String jsonString, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".json"))) {
            writer.write(jsonString);
            writer.newLine();
        } catch (IOException e) {
            logger.error("Error {} while writing to json file {}, exiting with code 3", e.getMessage(), filePath);
            System.exit(3);
        }
        return jsonString;
    }
}
