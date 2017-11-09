import orm.Connector;
import orm.EntityManager;
import orm.EntityManagerBuilder;
import orm.Strategies.UpdateStrategy;
import orm.scanner.EntityScanner;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
       List<Class> allEnt = EntityScanner.getAllEntities(System.getProperty("user.dir"));
        Connector connector = new Connector(new EntityManagerBuilder());

        EntityManagerBuilder emb = new EntityManagerBuilder();
        EntityManager em = emb
                .configureConnectionString()
                .setDriver("jdbc")
                .setAdapter("mysql")
                .setHost("localhost")
                .setPort("3306")
                .setUser("root")
                .setPass("MonteSq@21")
                .createConnection()
                .configureCreationType()
                .set(UpdateStrategy.class)
                .setDataSource("asd")
                .build();
    }
}
