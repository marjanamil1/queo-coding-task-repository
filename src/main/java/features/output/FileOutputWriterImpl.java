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

    private String writeToCSV(List<Float> list, String filePath) throws WriteErrorException {
        StringBuilder stringBuilder = new StringBuilder();

        if (list.isEmpty()) {
            stringBuilder.append("\n");
        }
        for (Float f : list) {
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
            logger.error("Error while writing to the CSV file {}: {}", filePath, e.getMessage());
            throw new WriteErrorException("Failed to write to CSV file: " + filePath, e, 3);
        }

        logger.info("Successfully wrote to CSV file.");
        return stringBuilder.toString();
    }

    private String writeToJson(List<Float> inputList, String filePath, String action) throws WriteErrorException {
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
                throw new WriteErrorException("Invalid action: " + action, 3);
        }

        return writeJsonToFile(gson.toJson(jsonObject), filePath);
    }

    private String writeJsonToFile(String jsonString, String filePath) throws WriteErrorException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".json"))) {
            writer.write(jsonString);
            writer.newLine();
        } catch (IOException e) {
            logger.error("Error while writing to JSON file {}: {}", filePath, e.getMessage());
            throw new WriteErrorException("Failed to write to JSON file: " + filePath, e, 3);
        }

        logger.info("Successfully wrote to JSON file.");
        return jsonString;
    }
}
