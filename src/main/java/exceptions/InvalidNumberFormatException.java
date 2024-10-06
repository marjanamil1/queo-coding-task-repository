package exceptions;

public class InvalidNumberFormatException extends NumberFormatException {
    private final int errorCode;

    public InvalidNumberFormatException(int errorCode) {
        super("Error while reading the file. Error code: " + errorCode);
        this.errorCode = errorCode;
    }

    public InvalidNumberFormatException(String message, int errorCode) {
        super(message + " Error code: " + errorCode);
        this.errorCode = errorCode;
    }

    public InvalidNumberFormatException(String message, Throwable cause, int errorCode) {
        super(message + " Error code: " + errorCode);
        initCause(cause);
        this.errorCode = errorCode;
    }

    public InvalidNumberFormatException(Throwable cause, int errorCode) {
        super();
        initCause(cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}