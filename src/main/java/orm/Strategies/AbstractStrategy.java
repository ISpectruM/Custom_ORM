package orm.Strategies;

import annotations.Column;
import annotations.Entity;
import annotations.Id;
import entities.User;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public abstract class AbstractStrategy implements SchemaInitializationStrategy {

    Connection connection;
    String dataSource;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    List<Class> getAllEntities() {
        return Collections.singletonList(User.class);
    }

    void doCreate(Class entity) throws SQLException {
        String query = "CREATE TABLE " + this.dataSource+"."+ this.getTableName(entity)+ "(";

        Field[] fields = entity.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {

            Field currField = fields[i];
            currField.setAccessible(true);

            query += "`" + currField.getAnnotation(Column.class).name() + "` " +
                    this.getDatabaseType(currField) ;
            if(currField.isAnnotationPresent(Id.class)){
                query += " PRIMARY KEY AUTO_INCREMENT";
            }
            if (i < fields.length-1){
                query += ", ";
            }
        }
        query += ")";
        this.connection.prepareStatement(query).execute();
    }

    String getTableName(Class<?> entity) {
        String table = "";
        if (entity.isAnnotationPresent(Entity.class)){
            Entity annotation = entity.getAnnotation(Entity.class);
            table = annotation.name();
        }

        if(table.equals("")){
            table = entity.getClass().getSimpleName();
        }
        return table;
    }

    String getDatabaseType(Field field) {
        String type = "";
        switch (field.getType().getSimpleName()){
            case "int" :
                type = "INT";
                break;
            case "Integer":
                type = "INT";
                break;
            case "String":
                type = "VARCHAR(30)";
                break;
            case "Date":
                type = "DATETIME";
                break;
        }
        return  type;
    }


}
