import entities.User;
import orm.Connector;
import orm.EntityManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException, InstantiationException {
        Scanner scanner = new Scanner(System.in);

        String username = "root";
//                scanner.nextLine().trim();
        String password = "MonteSq@21";
//                scanner.nextLine().trim();
        String dbName = "orm_db";
//                scanner.nextLine().trim();

        Connector.createConnection(username,password,dbName);

//        User user  = new User("Petar",20,new Date());
//        user.setId(1);
        EntityManager<User> em = new EntityManager<>(Connector.getConnection());
        Iterable<User> users = em.find(User.class,"age > 15");
        System.out.println();

    }
}
