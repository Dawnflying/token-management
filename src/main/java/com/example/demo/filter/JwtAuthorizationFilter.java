package com.example.demo.filter;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.component.JwtManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtManager jwtManager;

    public JwtAuthorizationFilter(JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取 Authorization
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        // 如果 Authorization 存在且以 Bearer 开头
        if (authorization != null && authorization.startsWith("Bearer ")) {
            // 截取 Bearer 后的字符串作为 token
            try {
                String token = authorization.substring(7);
                String username = jwtManager.extractUsername(token);
                String authorities = jwtManager.extractClaims(token).get("authorities", String.class);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            catch (Exception e) {
                // 如果解析 token 出现异常，返回 401 错误
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(JSONObject.toJSONString("Unauthorized"));
                return;
            }
            // 将 token 中的用户信息存入 SecurityContext
        }
        // 继续执行过滤器链
        filterChain.doFilter(request, response);
    }

}
