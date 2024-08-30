package org.example.vebproekt.Web;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.vebproekt.Model.UserInfo;
import org.example.vebproekt.Service.UserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {
    private final UserInfoService userInfoService;

    public ChatController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/chat")
    public String createPrivateChat(HttpServletRequest request, Model model){

        String currentUser=(String)request.getSession().getAttribute("username"); //current logged in user
        model.addAttribute("currentUser",currentUser);
        List<String>otherUsers=new ArrayList<>();
        for(UserInfo u: userInfoService.listAllUsers()){
            if(!u.getUsername().equals(currentUser)){
                otherUsers.add(u.getUsername());
            }
        }
        model.addAttribute("otherUsers",otherUsers);
        return "chat";
    }
}
