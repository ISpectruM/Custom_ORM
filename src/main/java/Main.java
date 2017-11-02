import orm.Connector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        String user = scanner.nextLine().trim();
        String password = scanner.nextLine().trim();
        String dbName = scanner.nextLine().trim();

        Connector.createConnection(user,password,dbName);
        Connection conn = Connector.getConnection();
    }
}
