package org.fffd.l23o6.service;

import org.fffd.l23o6.pojo.entity.UserEntity;

public interface UserService {
    void login(String username, String password);
    void register(String username, String password, String name, String idn, String phone, String type);

    UserEntity findByUserName(String username);
    void editInfo(String username, String name, String idn, String phone, String type);
}