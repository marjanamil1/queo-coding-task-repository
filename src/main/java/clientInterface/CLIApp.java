package clientInterface;

import exceptions.ReadErrorException;
import exceptions.WriteErrorException;
import features.actions.ActionsManager;
import features.actions.ActionsManagerImpl;
import features.input.FileInputReaderImpl;
import features.input.InputReader;
import features.input.StdInReaderImpl;
import features.input.UrlInputReaderImpl;
import features.output.FileOutputWriterImpl;
import features.output.OutputWriter;
import features.output.StdOutWriterImpl;
import features.output.UrlOutputWriterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIApp {

    private static final Logger logger = LoggerFactory.getLogger(CLIApp.class);

    public static void main(String[] args) {
        // Map for the operations (operation flag key, value)
        Map<String, String> options = new HashMap<>();

        // Iterate through the command line arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "-i":
                    if (i + 1 < args.length) {
                        options.put("-i", args[++i]); // the value args[++i] is the input type
                    } else {
                        options.put("-i", "stdin");
                    }
                    break;
                case "-o":
                    if (i + 1 < args.length) {
                        options.put("-o", args[++i]); // the value args[++i] is the output type
                    } else {
                        options.put("-o", "stdout");
                    }
                    break;
                case "-f":
                    if (i + 1 < args.length) {
                        options.put("-f", args[++i]); // the value args[++i] is the input format type
                    } else {
                        options.put("-f", "csv");
                    }
                    break;
                case "-F":
                    if (i + 1 < args.length) {
                        options.put("-F", args[++i]);// the value args[++i] is the output format type
                    } else {
                        options.put("-F", "csv");
                    }
                    break;
                case "-a":
                    if (i + 1 < args.length) {
                        options.put("-a", args[++i]); // the value args[++i] is the operation to be performed on the input list
                    } else {
                        options.put("-a", ""); // if no action option is provided no operation will be performed
                    }
                    break;
                default:
                    System.err.println("Unknown option: " + arg + "will be ignored.");
            }
        }

        System.out.print("Welcome to Queo's coding game.\n" +
                "The game performs actions on a list of float point numbers:\n" +
                "sum, minMax and LT4 (less than four) based on the action \"-a\" option specified in the command, \n" +
                "input values from input type -i (stdin, FILE or URL) and values provided, \n" +
                "and output values in the output type (stdout, FILE or URL) -o defined in the command. \n" +
                "If the selected -i option is \"stdin\" or you have ot specified it," +
                "you will be asked to enter the list of float point numbers to be used as input." +
                "If you have not selected the input file format, default format is \"csv\" \n" +
                "If you have not selected the output file format, default format is \"csv\" \n");


        // Output the parsed options
        System.out.println("You have selected the following options:");
        for (Map.Entry<String, String> entry : options.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        InputReader inputReader;
        OutputWriter outputWriter;
        String inputType = options.get("-i");
        String action = options.get("-a");
        String outputType = options.get("-o");
        String inputFormat = options.get("-f");
        String outputFormat = options.get("-F");
        List<Float> inputList = null;

        switch (inputType.toUpperCase()) {
            case "STDIN":
                System.out.println("Please enter a list of float numbers separated by \",\"");
                inputReader = new StdInReaderImpl();
                outputWriter = new StdOutWriterImpl(); // to display the initial list
                try {
                    inputList = inputReader.readInput();
                    logger.info("Thank you, we will perform the selected operations on the" +
                            "Std input list " + outputWriter.writeOutput(inputList));
                } catch (ReadErrorException e) {
                    logger.error("Error reading input: {}", e.getMessage(), e);
                } catch (WriteErrorException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "FILE":
                inputReader = new FileInputReaderImpl();
                // TODO: Implement file input handling
                System.out.println("File input handling is not implemented yet.");
                switch (inputFormat.toUpperCase()) {
                    case "CSV":
                        // TODO: Implement file input handling for csv
                        System.out.println("File input handling is not implemented yet.");
                        break;
                    case "JSON":
                        // TODO: Implement file input handling for json
                        System.out.println("File input handling is not implemented yet.");
                        break;
                }
                break;
            case "URL":
                inputReader = new UrlInputReaderImpl();
                // TODO: Implement URL input handling
                System.out.println("URL input handling is not implemented yet.");
                break;
        }

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
        }

        OutputWriter resultOutputWriter;

        switch (outputType.toUpperCase()) {
            case "STDOUT":
                resultOutputWriter = new StdOutWriterImpl(); // to display the result on the screen
                try {
                    System.out.println("The result of the specified operation " +
                            action + "is: " + resultOutputWriter.writeOutput(resultValues));
                } catch (WriteErrorException e) {
                    logger.error("Error reading input: {}", e.getMessage(), e);
                }
                break;
            case "FILE":
                resultOutputWriter = new FileOutputWriterImpl();
                // TODO: Implement file input handling
                System.out.println("File output handling is not implemented yet.");
                switch (outputFormat.toUpperCase()) {
                    case "CSV":
                        // TODO: Implement file input handling for csv
                        System.out.println("File output handling is not implemented yet.");
                        break;
                    case "JSON":
                        // TODO: Implement file input handling for json
                        System.out.println("File output handling is not implemented yet.");
                        break;
                }
                break;
            case "URL":
                resultOutputWriter = new UrlOutputWriterImpl();
                // TODO: Implement URL output handling
                System.out.println("URL input handling is not implemented yet.");
                break;
        }

    }
}



