package org.example.vebproekt.Service.Impl;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class OnlineUserService {
    private final ConcurrentMap<String,Boolean> onlineUsers = new ConcurrentHashMap<>();
    public void addUser(String username) {
        onlineUsers.put(username, true);
    }

    public void removeUser(String username) {
        onlineUsers.remove(username);
    }

    public boolean isUserOnline(String username) {
        return onlineUsers.containsKey(username);
    }
}
