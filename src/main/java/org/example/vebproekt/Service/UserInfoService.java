package org.example.vebproekt.Service;

import org.example.vebproekt.Model.UserInfo;

import java.util.List;

public interface UserInfoService {
    List<UserInfo> listAllUsers();

    UserInfo create(String username, String password, String email, String name);

    UserInfo update(Long id, String username, String password, String email, String name);

    UserInfo delete(Long id);

    UserInfo findUserByUsername(String username);

}