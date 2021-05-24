package com.rensource.videorental.modelfactories;

import com.github.heywhy.springentityfactory.contracts.FactoryHelper;
import com.github.heywhy.springentityfactory.contracts.ModelFactory;
import com.github.javafaker.Faker;
import com.rensource.videorental.entity.User;
import com.rensource.videorental.entity.Video;

public class UserFactory implements FactoryHelper<User> {
    @Override
    public Class<User> getEntity() {
        return User.class;
    }

    @Override
    public User apply(Faker faker, ModelFactory factory) {
        User user = new User();
        user.setUsername(faker.name().username());
        return user;
    }
}
