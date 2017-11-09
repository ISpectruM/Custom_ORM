package orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector {
    private EntityManagerBuilder builder;
    private Connection connection;
    private String adapter;
    private String driver;
    private String host;
    private String port;
    private String user;
    private String pass;

    public Connector(EntityManagerBuilder builder) {
        this.builder = builder;
    }

    public Connector setAdapter(String adapter) {
        this.adapter = adapter;
        return this;
    }

    public Connector setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public Connector setHost(String host) {
        this.host = host;
        return this;
    }

    public Connector setPort(String port) {
        this.port = port;
        return this;
    }

    public Connector setUser(String user) {
        this.user = user;
        return this;
    }

    public Connector setPass(String pass) {
        this.pass = pass;
        return this;
    }

    public EntityManagerBuilder createConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", this.user);
        properties.setProperty("password", this.pass);
        this.connection = DriverManager.getConnection(
                this.driver + ":" +
                        this.adapter + "://" +
                        this.host + ":" +
                        this.port + "/" ,properties);
        this.builder.setConnection(this.connection);
        return this.builder;
    }
}
