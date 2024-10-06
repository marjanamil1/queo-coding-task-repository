package exceptions;

import java.io.IOException;

public class ReadErrorException extends IOException {
    private final int errorCode;

    public ReadErrorException(int errorCode) {
        super("Error while reading the file.");
        this.errorCode = errorCode;
    }

    public ReadErrorException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ReadErrorException(String message, Throwable cause, int errorCode) {
        super(message, cause);
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