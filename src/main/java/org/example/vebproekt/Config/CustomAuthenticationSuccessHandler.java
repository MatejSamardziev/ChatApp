package org.example.vebproekt.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.vebproekt.Model.UserInfo;
import org.example.vebproekt.Service.UserInfoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserInfoService userInfoService;

    public CustomAuthenticationSuccessHandler(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("On authentication success handler");
        String username = authentication.getName();
        String password=(String)authentication.getCredentials();
        UserInfo u = userInfoService.findUserByUsername(username);
        Long userId=u.getId();
        HttpSession session = request.getSession();
        session.setAttribute("userId", userId);
        session.setAttribute("username",username);
        System.out.println("The session userId is: "+userId);
        super.setDefaultTargetUrl("/chat");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
