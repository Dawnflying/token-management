package com.example.demo.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public void successfulAuthentication(HttpServletRequest request,
                                         HttpServletResponse response,
                                         FilterChain chain,
                                         Authentication authResult) throws IOException, ServletException {

        // 在用户成功认证后创建 HttpSession
        HttpSession session = request.getSession(true);
        session.setAttribute("user", authResult.getPrincipal());

        // 可以在此处添加更多自定义逻辑

        super.successfulAuthentication(request, response, chain, authResult);
    }
}

