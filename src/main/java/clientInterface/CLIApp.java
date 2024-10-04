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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIApp {

    private static final Logger logger = LoggerFactory.getLogger(CLIApp.class);

    public static void main(String[] args) {

        String inputPath = null;
        String outputPath = null;

        Map<String, String> options = new HashMap<>();

        // Default values
        options.put("-i", "stdin");
        options.put("-o", "stdout");
        options.put("-f", "csv");
        options.put("-F", "csv");
        options.put("-a", "");

        // Iterate through the command line arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (options.containsKey(arg) && i + 1 < args.length) {
                if ((arg.equals("-i")) && (args[i + 1].stripLeading().equalsIgnoreCase("file") || args[i + 1].stripLeading().equalsIgnoreCase("url"))) {
                    options.put(arg, args[++i]);
                    inputPath = args[++i];
                    System.out.println("Input path is :" + inputPath);
                } else {
                    if ((arg.equals("-o")) && (args[i + 1].stripLeading().equalsIgnoreCase("file") || args[i + 1].stripLeading().equalsIgnoreCase("url"))) {
                        options.put(arg, args[++i]);
                        outputPath = args[++i];
                        System.out.println("Output path is :" + outputPath);
                    } else {
                        options.put(arg, args[++i]);
                    }
                }
            } else if (options.containsKey(arg)) {
                // No value provided; use default
                continue; // The default values are already set
            } else {
                System.err.println("Unknown option: " + arg + " will be ignored.");
            }
            // the upper logic may overwrite the default -i and -o options,
            // so will need to check and restore the default in case of invalid option input
            restoreDefaultOptions(options);
        }

        printWelcomeMessage();

        // Output the parsed options
        System.out.println("You have selected the following options:");
        for (Map.Entry<String, String> entry : options.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        InputReader inputReader;
        OutputWriter outputWriter;
        OutputWriter resultOutputWriter;
        String inputTypeString = options.get("-i"); // may contain the file or url path
        String inputType = options.get("-i");
        String inputFilePath = "";
        String action = options.get("-a");
        String outputTypeString = options.get("-o");
        String outputType = options.get("-o");
        String outputFilePath = "";
        String inputFormat = options.get("-f");
        String outputFormat = options.get("-F");
        List<Float> inputList = null;

        if (inputType.equalsIgnoreCase("file") || inputType.equalsIgnoreCase("url")) {
            inputFilePath = inputPath;
        }

        if (outputType.equalsIgnoreCase("file") || outputType.equalsIgnoreCase("url")) {
            outputFilePath = outputPath;
        }

        outputWriter = new StdOutWriterImpl(); // to display the initial list

        switch (inputType.toUpperCase()) {
            case "STDIN":
                System.out.println("Please enter a list of float numbers separated by \",\"");
                inputReader = new StdInReaderImpl();
                try {
                    inputList = inputReader.readInput();
                    logger.info("Performing the selected operations on the " +
                            "input list " + outputWriter.writeOutput(inputList));

                } catch (ReadErrorException e) {
                    logger.error("Error reading input: {} application will exit with code " + e.getErrorCode(), e.getMessage(), e);
                    System.exit(e.getErrorCode());
                } catch (WriteErrorException e) {
                    logger.error("Error {} while writing the output, application will exit with error code " + e.getErrorCode(), e.getErrorCode());
                    System.exit(e.getErrorCode());
                }
                break;
            case "FILE":
                System.out.println("Reading input from the file " + inputFilePath);
                inputReader = new FileInputReaderImpl(inputFilePath, inputFormat);

                try {
                    inputList = inputReader.readInput();
                    logger.info("Performing the selected operations on the " +
                            "input list " + outputWriter.writeOutput(inputList));
                } catch (ReadErrorException e) {
                    logger.error(e.getMessage(), "Exiting the application with code {}", e.getErrorCode());
                    System.exit(e.getErrorCode());
                } catch (IOException e1) {
                    logger.error("IO Error while reading the file path, exiting the application with code 2");
                    System.exit(2);
                }

                break;
            case "URL":
                inputReader = new UrlInputReaderImpl();
                // TODO: Implement URL input handling
                System.out.println("URL input handling is not implemented yet.");
                break;
            default:
                System.err.println("Unknown input type, will be ignored.");
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
            default:
                System.err.println("Unknown operation, will be ignored.");
                break;
        }

        switch (outputType.toUpperCase()) {
            case "STDOUT":
                resultOutputWriter = new StdOutWriterImpl();
                printOutput(resultOutputWriter, resultValues, action);
                break;
            case "FILE":
                try {
                    System.out.println("The result file of the performed operation " + action +
                            " is contained in the output file " + outputFilePath);
                    resultOutputWriter = new FileOutputWriterImpl(action, outputFormat, outputFilePath);
                    resultOutputWriter.writeOutput(resultValues);
                } catch (WriteErrorException e) {
                    logger.error(e.getMessage(), "Exiting the application with code {}", e.getErrorCode());
                    System.exit(e.getErrorCode());
                }
                break;

            case "URL":
                resultOutputWriter = new UrlOutputWriterImpl();
                // TODO: Implement URL output handling
                System.out.println("URL input handling is not implemented yet.");
                break;
            default:
                System.err.println("Unknown output type, will be ignored.");
                break;
        }
    }

    private static void printWelcomeMessage() {
        System.out.print("Welcome to Queo's coding game.\n" +
                "The game performs actions on a list of float point numbers:\n" +
                "Please specify: action \"-a\" followed by one of the options (sum, minMax and LT4 (less than four)), \n" +
                " input type \"-i\" proceeding with the input type value (stdin, FILE or URL), " +
                "if input type is FILE or URL please provide the input file path following the input type," +
                "output type \"-o\" proceeding with the one of the output type values (stdout, FILE or URL), \n" +
                "if output type is FILE or URL please provide the output file path following the input type," +
                "default input option is \"stdin\" \n" +
                "default output option is \"stdout\" \n" +
                "if input option is \"stdin\" you will be asked to enter the list of float point numbers to be used as input." +
                "If you have not selected the input file format, default format is \"csv\" \n" +
                "If you have not selected the output file format, default format is \"csv\" \n");
    }

    /**
     * Method to print output to STDOUT
     */
    private static void printOutput(OutputWriter writer, List<Float> values, String action) {
        try {
            System.out.println("The result of the specified operation " +
                    action + " is: " + writer.writeOutput(values));
            System.exit(0);
        } catch (WriteErrorException e) {
            logger.error("Error writing output: {}, ", e.getMessage(), e);
        }
    }

    /**
     * Method to restore the default options for the input type and output type
     * if invalid type is entered after "-i" or  "-o"
     */

    private static void restoreDefaultOptions(Map<String, String> options) {
        if (!(options.get("-i").equalsIgnoreCase("stdin") ||
                options.get("-i").equalsIgnoreCase("file") ||
                options.get("-i").equalsIgnoreCase("url"))) {
            options.put("-i", "stdin");
        }
        if (!(options.get("-o").equalsIgnoreCase("stdout") ||
                options.get("-o").equalsIgnoreCase("file") ||
                options.get("-o").equalsIgnoreCase("url"))) {
            options.put("-o", "stdout");
        }
    }
}