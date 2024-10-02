package exceptions;

import java.io.IOException;

public class WriteErrorException extends IOException {
    private final int errorCode;

    public WriteErrorException(int errorCode) {
        super("Error while writing the file. Error code: " + errorCode);
        this.errorCode = errorCode;
    }

    public WriteErrorException(String message, int errorCode) {
        super(message + " Error code: " + errorCode);
        this.errorCode = errorCode;
    }

    public WriteErrorException(String message, Throwable cause, int errorCode) {
        super(message + " Error code: " + errorCode, cause);
        this.errorCode = errorCode;
    }

    public WriteErrorException(Throwable cause, int errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}