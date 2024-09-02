package com.example.demo.filter;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.component.JwtManager;
import com.example.demo.service.DefaultUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtManager jwtManager;

    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(JwtManager jwtManager, UserDetailsService userDetailsService) {
        this.jwtManager = jwtManager;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取 Authorization
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        // 如果 Authorization 存在且以 Bearer 开头
        if (authorization != null && authorization.startsWith("Bearer ")) {
            // 截取 Bearer 后的字符串作为 token
            long start = System.currentTimeMillis();
            try {
                String token = authorization.substring(7);
                String username = jwtManager.extractUsername(token);
                String authorities = jwtManager.extractClaims(token).get("authorities", String.class);
                Long userId = jwtManager.extractClaims(token).get("userId", Long.class);
                log.info("token解析耗时：" + (System.currentTimeMillis() - start) + "ms");
                System.out.println("username: " + username + ", authorities: " + authorities + ", userId: " + userId);
                System.out.println("token解析耗时：" + (System.currentTimeMillis() - start) + "ms");
                if (username != null) {
                    //do authorization
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (userDetails != null) {
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                // 如果解析 token 出现异常，返回 401 错误
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(JSONObject.toJSONString("Unauthorized"));
                SecurityContextHolder.clearContext();
                return;
            }

        }
        filterChain.doFilter(request, response);
    }

}
