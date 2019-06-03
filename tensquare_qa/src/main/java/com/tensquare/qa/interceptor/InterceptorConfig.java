package com.tensquare.qa.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class InterceptorConfig implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("qa interceptor");
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            if (authorization.startsWith("Bearer ")) {
                String token = authorization.substring(7);
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    if (claims != null) {
                        String roles = (String) claims.get("roles");
                        if (roles != null || roles.length() > 0) {
                            if (roles.equals("user")) {
                                request.setAttribute("claims_user", claims);
                            } else if (roles.equals("admin")) {
                                request.setAttribute("claims_admin", claims);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
