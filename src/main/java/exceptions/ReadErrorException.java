package exceptions;

import java.io.IOException;

public class ReadErrorException extends IOException {
    private final int errorCode;

    public ReadErrorException(int errorCode) {
        super("Error while reading the file. Error code: " + errorCode);
        this.errorCode = errorCode;
    }

    public ReadErrorException(String message, int errorCode) {
        super(message + " Error code: " + errorCode);
        this.errorCode = errorCode;
    }

    public ReadErrorException(String message, Throwable cause, int errorCode) {
        super(message + " Error code: " + errorCode, cause);
        this.errorCode = errorCode;
    }

    public ReadErrorException(Throwable cause, int errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}