package com.rensource.videorental.service;

import com.rensource.videorental.entity.User;

import java.util.List;

/**
 * @author r.atenga
 */
public interface UserService {
    void loadDummyData();
    User findUserByName(String name);
    Boolean isEmpty();
}
