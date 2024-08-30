package org.example.vebproekt.Config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;


import java.io.IOException;


@Component
public class CustomPreAuthFilter implements Filter {



    public CustomPreAuthFilter() {

    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String username = servletRequest.getParameter("username");
        String password = servletRequest.getParameter("password");

        if ("/login".equals(request.getServletPath())){
            // Add your logic here to handle username and password
            //System.out.println("Username: " + username);
           // System.out.println("Password: " + password);
            if(username!=null && password!=null){
                //System.out.println("vlaga tuka");

            }

        }


        // Proceed with the filter chain
        filterChain.doFilter(servletRequest, servletResponse);
    }
}