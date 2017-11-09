package orm.Strategies;

import java.sql.SQLException;
import java.util.List;

public class DropCreateStrategy extends AbstractStrategy{
    private final String DROP_DATABASE_QUERY = "DROP DATABASE IF EXISTS `%S`;";
    private final String CREATE_DATABASE_QUERY = "CREATE DATABASE `%S`;";

    public DropCreateStrategy() {}

    @Override
    public void execute() throws SQLException {
        String query = String.format(DROP_DATABASE_QUERY, this.dataSource);

        this.connection.prepareStatement(query).executeUpdate();

        String createQuery = String.format(CREATE_DATABASE_QUERY, this.dataSource);
        this.connection.prepareStatement(createQuery).execute();

        List<Class> entities = getAllEntities();

        for (Class entity : entities) {
            doCreate(entity);
        }
    }


}