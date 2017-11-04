package orm;

import annotations.Column;
import annotations.Entity;
import annotations.Id;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class EntityManager<E> implements DbContext<E> {
    private Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    public boolean persist(E entity) throws IllegalAccessException, SQLException {
        Field primary = this.getId(entity.getClass());
        primary.setAccessible(true);
        Object value = primary.get(entity);

        if(value == null || (Integer)value <= 0){
            return this.doInsert(entity, primary);
        }
        return this.doUpdate(entity,primary);
    }

    public Iterable<E> find(Class<E> table) {
        return null;
    }

    public Iterable<E> find(Class<E> table, String where) {
        return null;
    }

    public E findFirst(Class<E> table) {
        return null;
    }

    @Override
    public E findFirst(Class<E> table, String where)
            throws SQLException, IllegalAccessException, InstantiationException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM " + this.getTableName(table) +" WHERE "+ where + " LIMIT 1";
        ResultSet result =  statement.executeQuery(query);
        E entity = table.newInstance();
        result.next();
        this.fillEntity(table,result,entity);
        return entity;
    }

    private void fillEntity(Class<E> table, ResultSet result, E entity) throws SQLException, IllegalAccessException {
        Field[] fields = table.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field currField = fields[i];
            currField.setAccessible(true);

            this.fillField(currField,result,currField.getAnnotation(Column.class).name(),entity);
        }
    }

    private void fillField(Field currField, ResultSet result, String name, E entity) throws SQLException, IllegalAccessException {
        if (currField.getType() == (Integer.class) ||
                currField.getType() == (int.class)){
            currField.set(entity,result.getInt(name));
        } else if(currField.getType() == (String.class)){
            currField.set(entity,result.getString(name));
        } else if(currField.getType() == (Date.class)){
            currField.set(entity,result.getDate(name));
        }
    }

    private Field getId(Class entity){
        return Arrays.stream(entity.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Object does not have primary key"));

    }

    private boolean doUpdate(E entity, Field primary) throws IllegalAccessException, SQLException {
        String query = "UPDATE " + this.getTableName(entity.getClass()) + " SET ";
        String columnsAndValues = "";
        String where = "";

        Field[] fields = entity.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field currField = fields[i];
            currField.setAccessible(true);

            if (currField.getName().equals(primary.getName())) {
                where += " WHERE `" + currField.getAnnotation(Column.class).name() + "` = " + currField.get(entity);
            } else {
                if (currField.get(entity) instanceof Date) {
                    columnsAndValues += "`" + currField.getAnnotation(Column.class).name()
                            + "` = '" + new SimpleDateFormat("yyyy-MM-dd")
                            .format(currField.get(entity)) + "'";
                } else {
                    columnsAndValues += "`" + currField.getAnnotation(Column.class).name()
                            + "` = '" + currField.get(entity) + "'";
                }
                if (i < fields.length-1){
                    columnsAndValues += ", ";
                }
            }
        }
        query += columnsAndValues + where;
        return connection.prepareStatement(query).execute();
    }

    private boolean doInsert(E entity, Field primary) throws IllegalAccessException, SQLException {
        String tableName = getTableName(entity.getClass());
        Field[] entityFields = entity.getClass().getDeclaredFields();
        String query = "INSERT INTO " + tableName +" (";

        String columns = "";
        String values = "";

        for (int i = 1; i < entityFields.length; i++) {
            Field currField = entityFields[i];
            currField.setAccessible(true);

            columns += "`" + currField.getAnnotation(Column.class).name() + "`";
            Object value;
            if (currField.get(entity) instanceof Date){
                value = new SimpleDateFormat("yyyy-MM-dd")
                        .format(currField.get(entity));
            } else {
                value = currField.get(entity);
            }

            values += "'" + value + "'";

            if (i < entityFields.length-1){
                columns += ", ";
                values += ", ";
            }

        }
        query += columns + ") VALUES (" + values + ");";
        return connection.prepareStatement(query).execute();
    }

    private String getTableName(Class entity){
        String table = "";
        if (entity.isAnnotationPresent(Entity.class)){
            Entity annotation = (Entity) entity.getAnnotation(Entity.class);
            table = annotation.name();
        }

        if(table.equals("")){
            table = entity.getClass().getSimpleName();
        }
        return table;
    }
}
