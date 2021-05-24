package com.rensource.videorental.modelfactories;

import com.github.heywhy.springentityfactory.contracts.FactoryHelper;
import com.github.heywhy.springentityfactory.contracts.ModelFactory;
import com.github.javafaker.Faker;
import com.rensource.videorental.entity.Rental;

public class RentalFactory implements FactoryHelper<Rental> {

    @Override
    public Class<Rental> getEntity() {
        return Rental.class;
    }

    @Override
    public Rental apply(Faker faker, ModelFactory factory) {
        Rental rental = new Rental();
        rental.setDaysRented(2);
        return rental;
    }
}
