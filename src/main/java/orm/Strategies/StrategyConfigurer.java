package orm.Strategies;

import orm.EntityManagerBuilder;

public class StrategyConfigurer {
    private EntityManagerBuilder builder;

    public StrategyConfigurer(EntityManagerBuilder builder) {
        this.builder = builder;
    }

    public <T extends AbstractStrategy> EntityManagerBuilder
            set(Class<T> strategyClass) throws IllegalAccessException, InstantiationException {

        this.builder.setStrategy(strategyClass.newInstance());

        return this.builder;
    }

    //    public EntityManagerBuilder setDropCreateStrategy(){
//        DropCreateStrategy dropCreateStrategy = new DropCreateStrategy();
//        this.builder.setStrategy(dropCreateStrategy);
//
//        return  this.builder;
//    }
//
//    public EntityManagerBuilder setUpdateStrategy(){
//        UpdateStrategy updateStrategy = new UpdateStrategy();
//        this.builder.setStrategy(updateStrategy);
//
//        return this.builder;
//    }

}
