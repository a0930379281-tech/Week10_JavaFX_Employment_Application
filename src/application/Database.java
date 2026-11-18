package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/employment_db?serverTimezone=UTC";
    private static final String USER = "root";
    private Database() {}

    public static Connection connect() throws SQLException {
        String password = System.getenv("MYSQL_PASSWORD");
        if (password == null) password = "";
        return DriverManager.getConnection(URL, USER, password);
    }
}
