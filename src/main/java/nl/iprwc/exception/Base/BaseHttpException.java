package nl.iprwc.exception.Base;

import javax.ws.rs.WebApplicationException;

public abstract class BaseHttpException extends Exception {
    public BaseHttpException() {
    }

    public BaseHttpException(String message) {
        super(message);
    }

    public BaseHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseHttpException(Throwable cause) {
        super(cause);
    }

    public BaseHttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public abstract WebApplicationException getHttpError();
}

