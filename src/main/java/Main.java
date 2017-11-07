import entities.ExampleEntity;
import entities.User;
import orm.Connector;
import orm.EntityManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        String username = "root";
//                scanner.nextLine().trim();
        String password = "MonteSq@21";
//                scanner.nextLine().trim();
        String dbName = "orm_db";
//                scanner.nextLine().trim();

        Connector connector = new Connector();
        connector.createConnection(username,password,dbName);

        User user  = new User("Petar",20,new Date());
        ExampleEntity ex = new ExampleEntity("kolyo","pomo");
        EntityManager<ExampleEntity> em = new EntityManager<>(connector.getConnection());
        em.doDelete(ExampleEntity.class, "full_name LIKE 'kolyo'");

    }
}
