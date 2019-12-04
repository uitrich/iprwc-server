package nl.iprwc.sql;

public interface TransactionReturn {
    Object run(DatabaseService databaseService) throws Throwable;
}
