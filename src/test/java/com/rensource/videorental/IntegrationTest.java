package com.rensource.videorental;


import com.github.heywhy.springentityfactory.contracts.ModelFactory;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.rensource.videorental.modelfactories.ModelFactoryRegistrar;
import com.rensource.videorental.modelfactories.etc.ModelFactoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@SpringBootTest(classes = VideoRentalApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IntegrationTest {

    @Autowired
    protected Gson gson;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    protected TransactionTemplate transactionTemplate;
    protected ModelFactory modelFactory;

    @PostConstruct
    public void postConstruct() {
        modelFactory = new ModelFactoryImpl(new Faker(), transactionTemplate, entityManager);
        ModelFactoryRegistrar.register(modelFactory);
    }

    @AfterEach
    public void after() {
        transactionTemplate.executeWithoutResult(status -> {
            entityManager.flush();
        });
//        databaseCleanupService.truncate();
    }
}
