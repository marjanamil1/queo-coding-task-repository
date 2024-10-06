package clientInterface;

import exceptions.ReadErrorException;
import exceptions.WriteErrorException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command line application
 */
public class CLIApp {

    private static final Logger logger = LoggerFactory.getLogger(CLIApp.class);

    public static void main(String[] args) {
        int exitCode = 0;

        String inputPath = null;
        String outputPath = null;

        Map<String, String> options = new HashMap<>();

        // Default values
        options.put("-i", "stdin");
        options.put("-o", "stdout");
        options.put("-a", "");
        options.put("-f", "csv");
        options.put("-F", "csv");

        // Iterate through the command line arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (options.containsKey(arg) && i + 1 < args.length) {
                if ((arg.equals("-i")) && (args[i + 1].stripLeading().equalsIgnoreCase("file") || args[i + 1].stripLeading().equalsIgnoreCase("url"))) {
                    options.put(arg, args[++i]);
                    inputPath = args[++i];
                    logger.info("Input path is :" + inputPath);
                } else {
                    if ((arg.equals("-o")) && (args[i + 1].stripLeading().equalsIgnoreCase("file") || args[i + 1].stripLeading().equalsIgnoreCase("url"))) {
                        options.put(arg, args[++i]);
                        outputPath = args[++i];
                        logger.info("Output path is :" + outputPath);
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
            logger.info("Options selected: \n" +
                    "-i (input type): {} \n" +
                    "-o (output type): {} \n" +
                    "-a (action): {} \n" +
                    "-f (input format if input type is file): {}" +
                    "-F (output format if output type is file: {}",
                    options.get("-i"),
                    options.get("-o"),
                    options.get("-a"),
                    options.get("-f"),
                    options.get("-F")
            );
        }

        printWelcomeMessage();

        // Output the parsed options
        System.out.println("You have selected the following options:");
        for (Map.Entry<String, String> entry : options.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        InputReader inputReader;
        OutputWriter resultOutputWriter;
        String inputType = options.get("-i");
        String inputFilePath = inputPath;
        String action = options.get("-a");
        String outputType = options.get("-o");
        String outputFilePath = outputPath;
        String inputFormat = options.get("-f");
        String outputFormat = options.get("-F");
        List<Float> inputList = null;

        switch (inputType.toUpperCase()) {
            case "STDIN":
                System.out.println("Please enter a list of float numbers separated by \",\"");
                inputReader = new StdInReaderImpl();
                try {
                    inputList = inputReader.readInput();
                    logger.info("Performing the selected operations on the " +
                            "input list " + inputList);
                } catch (ReadErrorException e) {
                    logger.error("Error reading input: {} application will exit with code " + e.getErrorCode(), e.getMessage(), e);
                    exitCode = e.getErrorCode();
                }
                break;
            case "FILE":
                System.out.println("Reading input from the file " + inputFilePath);
                inputReader = new FileInputReaderImpl(inputFilePath, inputFormat);

                try {
                    inputList = inputReader.readInput();
                    logger.info("Performing the selected operations on the " +
                            "input list " + inputList);
                } catch (ReadErrorException e) {
                    logger.error("Error reading input {} the application with code {} ", e.getMessage(), e.getErrorCode());
                    exitCode = e.getErrorCode();
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

        switch (outputType.toUpperCase()) {
            case "STDOUT":
                try {
                    resultOutputWriter = new StdOutWriterImpl(action);
                    printOutput(resultOutputWriter, inputList, action);
                } catch (WriteErrorException e) {
                    logger.error(e.getMessage(), "Exiting the application with code {}", e.getErrorCode());
                    exitCode = e.getErrorCode();
                }
                break;
            case "FILE":
                try {
                    resultOutputWriter = new FileOutputWriterImpl(action, outputFormat, outputFilePath);
                    resultOutputWriter.writeOutput(inputList);
                    System.out.println("The result file of the performed operation " + action +
                            " is contained in the output file " + outputFilePath);
                } catch (WriteErrorException e) {
                    logger.error(e.getMessage(), "Exiting the application with code {}", e.getErrorCode());
                    exitCode = e.getErrorCode();
                }
                break;

            case "URL":
                resultOutputWriter = new UrlOutputWriterImpl();
                // TODO: Implement URL output handling
                System.out.println("URL input handling is not implemented yet.");
                break;
            default:
                System.err.println("Unknown output type, will be ignored.");
                exitCode = 4;
                break;
        }

        Map<Integer, String> exitDescriptions = new HashMap<>();

        exitDescriptions.put(0, "ok");
        exitDescriptions.put(1, "input is empty");
        exitDescriptions.put(2, "read error");
        exitDescriptions.put(3, "write error");
        exitDescriptions.put(4, "read error");

        logger.info("Exiting application with code {}: {}", exitCode, exitDescriptions.get(exitCode));
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
    private static void printOutput(OutputWriter writer, List<Float> values, String action) throws WriteErrorException {
            System.out.println("The result of the specified operation " +
                    action + " is: " + writer.writeOutput(values));
    }

    /**
     * Method to restore the default options for the input type and output type
     * if invalid type is entered after "-i" or  "-o"
     */

    private static void restoreDefaultOptions(Map<String, String> options) {
        if (options.get("-i") == null || !(options.get("-i").equalsIgnoreCase("stdin") ||
                options.get("-i").equalsIgnoreCase("file") ||
                options.get("-i").equalsIgnoreCase("url"))) {
            options.put("-i", "stdin");
        }
        if (options.get("-o") == null || !(options.get("-o").equalsIgnoreCase("stdout") ||
                options.get("-o").equalsIgnoreCase("file") ||
                options.get("-o").equalsIgnoreCase("url"))) {
            options.put("-o", "stdout");
        }
        if (options.get("-f") == null || !(options.get("-f").equalsIgnoreCase("csv") ||
                options.get("-f").equalsIgnoreCase("json"))) {
            options.put("-f", "csv");
        }
        if (options.get("-F") == null || !(options.get("-F").equalsIgnoreCase("csv") ||
                options.get("-F").equalsIgnoreCase("json"))) {
            options.put("-F", "csv");
        }
    }
}