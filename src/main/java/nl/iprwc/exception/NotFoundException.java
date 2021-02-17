package nl.iprwc.exception;

import nl.iprwc.exception.Base.BaseServerErrorException;

import javax.ws.rs.ServerErrorException;

public class NotFoundException extends BaseServerErrorException {
    private static final int HTTP_CODE = 404;

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public ServerErrorException getHttpError() {
        return new ServerErrorException(HTTP_CODE, this);
    }
}
