package orm.Strategies;

import annotations.Column;
import annotations.Entity;
import annotations.Id;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UpdateStrategy extends AbstractStrategy {

    @Override
    public void execute() throws SQLException, IllegalAccessException {
        List<Class> allEntities = this.getAllEntities();

        for (Class entity : allEntities) {
            Field primary = this.getId(entity);
            primary.setAccessible(true);

            updateTable(entity,primary);
        }

    }

    private Field getId(Class entity){
        return Arrays.stream(entity.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Object does not have primary key"));

    }

    public void doDelete(Class<?> table, String criteria) throws Exception {
        String tableName = table.getAnnotation(Entity.class).name();
        if (!this.checkIfTableExists(tableName)){
            throw new Exception("Table does not exists");
        }
        String query = "DELETE FROM " + tableName + " WHERE "+criteria;
        this.connection.prepareStatement(query).execute();
    }

    private boolean updateTable(Class entity, Field primary) throws IllegalAccessException, SQLException {
        String tableName = getTableName(entity);

        if (!this.checkIfTableExists(tableName)) {
            this.doCreate(entity);
        }

        Field[] entityFields = entity.getDeclaredFields();
        for (int i = 0; i < entityFields.length; i++) {
            Field currField = entityFields[i];
            currField.setAccessible(true);
            Column columnAnnotation = currField.getAnnotation(Column.class);
            if (!this.checkIfFieldExists(tableName, columnAnnotation.name())) {
                this.doAlter(tableName, currField);
            }
        }
        return false;
    }

    private void doAlter(String tableName, Field currField) throws SQLException {
        Column column = currField.getAnnotation(Column.class);
        String query = "ALTER TABLE " +this.dataSource+"."+ tableName +
                " ADD column " + column.name() +
                " " + this.getDatabaseType(currField);
        this.connection.prepareStatement(query).execute();
    }

    private boolean checkIfTableExists(String tableName) throws SQLException {
        String query = "SELECT table_name FROM information_schema.tables " +
                "WHERE table_schema = '"+this.dataSource+"' AND table_name = '"+tableName + "' LIMIT 1;";

        ResultSet rs =  this.connection.prepareStatement(query).executeQuery();

        if (!rs.first()){
            return false;
        }
        return true;
    }

    private boolean checkIfFieldExists(String tableName ,String fieldName) throws SQLException {
        String query = "SELECT column_name FROM information_schema.`COLUMNS` " +
                "WHERE table_schema = '"+this.dataSource+"' " +
                "AND table_name =  '" + tableName +
                "' AND column_name = '" + fieldName + "'";
        ResultSet rs = this.connection.prepareStatement(query).executeQuery();
        if (!rs.first()){
            return false;
        }
        return true;
    }

}
