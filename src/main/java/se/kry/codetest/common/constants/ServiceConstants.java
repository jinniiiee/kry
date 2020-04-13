package se.kry.codetest.common.constants;

public class ServiceConstants {

    public static class Path {
        public static final String ALL_SERVICE_PATH = "/service";
        public static final String CREATE_SERVICE_PATH = "/create";
        public static final String ROUTE_PATH = "/*";
        public static final String UPDATE_SERVICE_PATH = "/update";
        public static final String DELETE_SERVICE_PATH = "/delete";
    }

    public static class Query {
        public static final String SELECT_ALL = "SELECT name, url, status, createDate FROM service;";
        public static final String UPDATE_STATUS = "UPDATE service SET status=? WHERE url=?";
        public static final String CREATE_RECORD = "INSERT INTO service (name, url, status) VALUES (?, ?, ?);";
        public static final String UPDATE_URL = "UPDATE service SET url=?, status='UNKNOWN' WHERE name=?;";
        public static final String DELETE_RECORD = "DELETE FROM service WHERE name=?";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS service (name VARCHAR(128) PRIMARY KEY" +
                                                    ", url VARCHAR(128) NOT NULL" +
                                                    ", status VARCHAR(128) DEFAULT 'UNKNOWN'" +
                                                    ", createDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
    }

    public static class Property {
        public static final String SERVICE_STATUS = "status";
        public static final String SERVICE_URL = "url";
        public static final String SERVICE_NAME = "name";

    }

    public static class Message {
        public static final String SERVICE_NOT_FOUND = "No services found";
        public static final String APPLICATION_IS_UP = "KRY code test service started";
        public static final String EMPTY_QUERY = "Query is null or empty";
        public static final String TABLE_CREATE = "Completed db migrations. Service table created.";
    }

    public static class Common {
        public static final String URL_PREFIX = "http";
        public static final String FORMAT_URL_PREFIX = "http://";
        public static final long POLL_DELAY = 1000 * 60;
        public static final int APP_PORT = 8080;
        public static final String QUERY_TERMINATOR = ";";
        public static final String PROP_CONTENT_TYPE = "content-type";
        public static final String CONTENT_TYPE = "application/json";

    }

    public static class Database {
        public static final String DB_PATH = "poller.db";
        public static final String PROP_URL = "url";
        public static final String URL = "jdbc:sqlite:";
        public static final String PROP_DRIVER = "driver_class";
        public static final String DRIVER = "org.sqlite.JDBC";
        public static final String PROP_MAX_POOL_SIZE = "max_pool_size";
        public static final int MAX_POOL_SIZE = 30;
    }
}
