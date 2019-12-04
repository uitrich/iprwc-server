package nl.iprwc.sql;

public class TransactionResult {
    private boolean success;
    private Object returned;
    private Throwable thrown;

    public TransactionResult(Object returned)
    {
        success = true;
        this.returned = returned;
    }

    public TransactionResult(Object returned, Throwable thrown)
    {
        success = false;
        this.returned = returned;
        this.thrown = thrown;
    }

    public boolean isSuccess() {
        return success;
    }

    public Throwable getThrown() {
        return thrown;
    }

    public Object getReturned() {
        return returned;
    }
}
