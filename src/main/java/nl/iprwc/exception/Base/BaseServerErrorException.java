package nl.iprwc.exception.Base;

import javax.ws.rs.ServerErrorException;

public abstract class BaseServerErrorException extends BaseHttpException {
    public BaseServerErrorException() {
    }

    public BaseServerErrorException(String message) {
        super(message);
    }

    public BaseServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseServerErrorException(Throwable cause) {
        super(cause);
    }

    public BaseServerErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ServerErrorException getHttpError() {
        return new ServerErrorException(500, this);
    }
}