package org.example.vebproekt.Web;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.vebproekt.Model.UserInfo;
import org.example.vebproekt.Service.Impl.RabbitMQService;
import org.example.vebproekt.Service.UserInfoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UsersController {

    private final UserInfoService userInfoService;

    public UsersController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }


    @GetMapping("/register")
    public String showRegister(){
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // This returns the login.html template
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam String name,
                               HttpServletRequest request,
                               HttpServletResponse response){
        System.out.println("Vleze tuka");
        System.out.println("AAAAAAAAAAAAAAAAA");
        userInfoService.create(username,password,email,name);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id){
         userInfoService.delete(id);
         return "redirect:/users";
    }
}
