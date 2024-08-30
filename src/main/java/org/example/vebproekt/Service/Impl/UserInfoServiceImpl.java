package org.example.vebproekt.Service.Impl;

import org.example.vebproekt.Exceptions.UserNotFoundException;
import org.example.vebproekt.Model.UserInfo;
import org.example.vebproekt.Repository.UserInfoRepository;
import org.example.vebproekt.Service.UserInfoService;
import org.example.vebproekt.Exceptions.InvalidUserInfoIdException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class UserInfoServiceImpl implements UserInfoService, UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    private final PasswordEncoder passwordEncoder;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserInfo> listAllUsers() {
        return userInfoRepository.findAll();
    }

    @Override
    public UserInfo create(String username, String password, String email, String name) {
        return userInfoRepository.save(new UserInfo(username,passwordEncoder.encode(password),email,name));
    }

    @Override
    public UserInfo update(Long id, String username, String password,String email, String name) {
        UserInfo u=userInfoRepository.findById(id).orElseThrow(InvalidUserInfoIdException::new);
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setEmail(email);
        u.setName(name);
        return userInfoRepository.save(u);
    }

    @Override
    public UserInfo delete(Long id) {
        UserInfo u=userInfoRepository.findById(id).orElseThrow(InvalidUserInfoIdException::new);
        userInfoRepository.delete(u);
        return u;
    }

    @Override
    public UserInfo findUserByUsername(String username){
        UserInfo u=userInfoRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return u;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userInfoRepository.findByUsername(username).orElseThrow(InvalidUserInfoIdException::new);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
