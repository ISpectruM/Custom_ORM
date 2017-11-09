package orm;

import orm.Strategies.AbstractStrategy;
import orm.Strategies.StrategyConfigurer;

import java.sql.Connection;
import java.sql.SQLException;

public class EntityManagerBuilder {
    private Connection connection;
    private String dataSource; //database name
    private AbstractStrategy strategy;

    public EntityManagerBuilder() {
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public EntityManagerBuilder setDataSource(String dataSource) {
        this.dataSource = dataSource;
        return  this;
    }

    public EntityManagerBuilder setStrategy(AbstractStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public EntityManager build() throws SQLException, IllegalAccessException {
        this.strategy.setConnection(this.connection);
        this.strategy.setDataSource(this.dataSource);
        return new EntityManager(this.connection,this.dataSource, this.strategy);
    }

    public Connector configureConnectionString(){
        return new Connector(this);
    }

    public StrategyConfigurer configureCreationType(){
        return new StrategyConfigurer(this);
    }
}
