package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DatabaseConnection class (required by defence format).
 * <p>
 * Requirements satisfied:
 * - getConnection() method
 * - closeConnection() method
 * <p>
 * We also provide closeStatement() and closeResultSet() helpers,
 * so DAO methods can close resources in finally blocks.
 */
public final class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/clothing_store";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASS = "1234";

    private static final String URL = env("DB_URL", DEFAULT_URL);
    private static final String USER = env("DB_USER", DEFAULT_USER);
    private static final String PASS = env("DB_PASS", DEFAULT_PASS);

    static {
        // Ensures driver is available when the program starts.
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC driver not found. Add the JAR to classpath.", e);
        }
    }

    private DatabaseConnection() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Required method name: closeConnection().
     * Safe to call with null.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Warning: could not close connection: " + e.getMessage());
            }
        }
    }

    public static void closeStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                System.out.println("Warning: could not close statement: " + e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.out.println("Warning: could not close ResultSet: " + e.getMessage());
            }
        }
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        if (value == null) return fallback;

        value = value.trim();
        return value.isEmpty() ? fallback : value;
    }
}
