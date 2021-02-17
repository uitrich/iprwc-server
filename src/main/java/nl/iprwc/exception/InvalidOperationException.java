package nl.iprwc.exception;

import nl.iprwc.exception.Base.BaseServerErrorException;

import javax.ws.rs.ServerErrorException;

public class InvalidOperationException extends BaseServerErrorException {
    private static final int HTTP_CODE = 500;

    public InvalidOperationException() {
    }

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOperationException(Throwable cause) {
        super(cause);
    }

    public InvalidOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public ServerErrorException getHttpError() {
        return new ServerErrorException(HTTP_CODE, this);
    }
}
