# README #

### Running the application ###

* To run the application first you need to package the application using the command:  

mvn clean package

This command will compile the code, run the tests included
packages the compiled code into a distributable JAR format.
The generated packaged files are contained in the target directory found under the root of the project.

Please note, in order to include all the dependencies in the class path 
(including transient ones which may be required for e.g. for logback), 
the maven-shade-plugin is used, which will create original-queo-coding-task-1.0-SNAPSHOT.jar and 
additional queo-coding-task-1.0-SNAPSHOT.jar replacing it, which should be used to run the application.

To run the application, from the root of the application directory, run the following command:

java -jar target/queo-coding-task-1.0-SNAPSHOT.jar 
followed by the OPTIONS:

-i -- input: should be followed by stdin/file/url,
    if "file" "url" is the following option for "-i",
    then the path to the input csv or json file is expected 
    to proceed the "file" or "url" option keyword.
    If "stdin" or invalid or empty string is put after "-i",
    then "stdin" option is considered as default.

-o -- output: should be followed by stdout/file/url,
    if "file" "url" is the following option for "-i",
    then the path to the input csv or json file is expected
    to proceed the "file" or "url" option keyword.
    If "stdout" or invalid or empty string is put after "-o",
    then "stdout" option is considered as default.
    For the file option, the path provided may/should not contain the extension,
    it will be added upon creation.

-a -- action: should be followed by sum/minmax/lt4,
    there is no default action option, so if no or invalid 
    action option is put, the application will exit with no error
    but there will be no operation done to the data,
    Invalid action will be returned with error code 4

-f -- input format: csv or json, if none or invalid format is provided, default is set to csv

-F -- output format: csv or json, if none or invalid format is provided, default is set to csv

The project package has included test files for valid, invalid and empty csv and json files:
target/classes/csv_input_valid.csv
target/classes/csv_input_empty.csv
target/classes/csv_input_invalid.csv
target/classes/json_input_valid.json
target/classes/json_input_empty.json
target/classes/json_input_invalid.json

### Tests ###

This solution includes tests which can be run using the command:

mvn clean test

This command will execute all tests, which are also run when executing the mvn package command.

There are two types of tests included based on the purpose:

- Unit tests for testing the actions functionalities (methods in the ActionsManagerImpl class) 
and the standard input and output which is mocked in the tests for StdInReaderImpl and StdOutWriter classes
  (Testing StInReaderImpl and StWriteImpl with keyboard input and output on the screen would be done by running the application.)
- Integration tests for testing the FileInputReader and FileOutputWriter by reading and writing to files included in the /test/resources directory
