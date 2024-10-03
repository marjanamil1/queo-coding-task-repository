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

    public String getErrorDescription() {
        String errorDescription = null;
        if (this.errorCode == 0) {
            errorDescription = "ok";
        }
        if (this.errorCode == 1) {
            errorDescription = "input is empty";
        }
        if (this.errorCode == 2) {
            errorDescription = "read error";
        }
        if (this.errorCode == 4) {
            errorDescription = "format error";
        } return errorDescription;
    }
}