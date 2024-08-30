package org.example.vebproekt.Config;

import jakarta.annotation.PostConstruct;
import org.example.vebproekt.Service.UserInfoService;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private final UserInfoService userInfoService;

    public DataInitializer(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    //@PostConstruct
    public void initData(){
        for(int i=0;i<5;i++){
            //userInfoService.create("user"+i,"password"+i);
        }
    }
}
