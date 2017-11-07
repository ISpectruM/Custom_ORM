package orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private Connection connection;

    public void createConnection(String username,String password,String dbName) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user",username);
        props.setProperty("password", password);

        connection = DriverManager.getConnection(URL+dbName,props);
    }

    public Connection getConnection() {
        return this.connection;
    }
}
