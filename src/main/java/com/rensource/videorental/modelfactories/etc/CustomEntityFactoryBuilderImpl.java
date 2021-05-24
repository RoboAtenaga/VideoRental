package com.rensource.videorental.modelfactories.etc;

import com.github.heywhy.springentityfactory.contracts.EntityFactoryBuilder;
import com.github.heywhy.springentityfactory.contracts.FactoryFunction;
import com.github.javafaker.Faker;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Function;

public class CustomEntityFactoryBuilderImpl<T> implements EntityFactoryBuilder<T> {

    private final Faker faker;
    private final Class<T> tClass;
    private final String name = "default";
    private final EntityManager entityManager;
    private Function<T, T> operator;
    private final Map<Class, FactoryFunction> definitions;
    private Set<String> activeStates = new HashSet<>();
    private final TransactionTemplate transactionTemplate;

    public CustomEntityFactoryBuilderImpl(
            Class<T> tClass,
            Map<Class, FactoryFunction> definitions,
            Faker faker,
            EntityManager entityManager,
            Function<T, T> operator,
            TransactionTemplate transactionTemplate) {
        this.faker = faker;
        this.tClass = tClass;
        this.operator = operator;
        this.definitions = definitions;
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public EntityFactoryBuilder state(String name) {
        activeStates.add(name);
        return this;
    }

    @Override
    public EntityFactoryBuilder state(Set<String> states) {
        activeStates = states;
        return this;
    }

    @Override
    public T create() {
        List<T> instances = create(1);

        return instances.get(0);
    }

    @Override
    public List<T> create(int count) {
        List<T> instances = make(count);
        List<T> savedInstances=new ArrayList<>();
        for (T instance : instances) {
            savedInstances.add(transactionTemplate.execute(status -> {
                entityManager.persist(instance);
                return instance;
            }));
        }

        return savedInstances;
    }

    @Override
    public List<T> make(int count) {
        List<T> instances = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            instances.add(make());
        }

        return instances;
    }

    @Override
    public T make() {
        if (operator != null) {
            return operator.apply(makeInstance());
        }
        return makeInstance();
    }

    @Override
    public EntityFactoryBuilder<T> then(Function<T, T> ttFunction) {
        if (operator != null) {
            Function<T, T> op = operator;
            operator = t -> ttFunction.apply(op.apply(t));
        } else {
            operator = ttFunction;
        }
        return this;
    }

    private <A> T makeInstance() {
        if (!definitions.containsKey(tClass)) {
            String msg = "Unable to locate factory with name [" + name + "] [" + tClass.getSimpleName() + "].";
            throw new IllegalArgumentException(msg);
        }

        FactoryFunction<T> function = (FactoryFunction<T>) definitions.get(tClass);
        return function.apply(faker);
    }
}
