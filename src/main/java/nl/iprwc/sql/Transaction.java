package nl.iprwc.sql;

public interface Transaction {
    void run(DatabaseService databaseService) throws Throwable;
}
