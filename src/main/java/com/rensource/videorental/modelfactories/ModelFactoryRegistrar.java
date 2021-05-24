package com.rensource.videorental.modelfactories;

import com.github.heywhy.springentityfactory.contracts.ModelFactory;

public class ModelFactoryRegistrar {
    public static void register(ModelFactory modelFactory) {
        modelFactory.register(VideoFactory.class);
        modelFactory.register(UserFactory.class);
        modelFactory.register(RentalFactory.class);
    }
}
