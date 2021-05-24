package com.rensource.videorental.service.impl;

import com.rensource.videorental.entity.User;
import com.rensource.videorental.repository.UserRepository;
import com.rensource.videorental.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author r.atenga
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    public void loadDummyData() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        users.add(user);

        user = new User();
        user.setUsername("demo");
        user.setPassword(passwordEncoder.encode("demo"));
        users.add(user);

        try {
            // save to db
            repository.saveAll(users);
        }catch (Exception e){
            LOG.error("loadDummyDataUser error: " + e);
        }
    }

    public User findUserByName(String name) {
        Optional<User> byUser = repository.findUserByUsernameEquals(name);
        if(!byUser.isPresent()){
            return null;
        }
        return byUser.get();
    }

    @Override
    public Boolean isEmpty() {
//        Boolean empty = true;
       return repository.findAll() == null || repository.findAll().isEmpty();
    }
}