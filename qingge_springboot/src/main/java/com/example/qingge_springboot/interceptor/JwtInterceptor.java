package com.example.qingge_springboot.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.qingge_springboot.common.Constants;
import com.example.qingge_springboot.entity.User;
import com.example.qingge_springboot.exception.ServiceException;
import com.example.qingge_springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        //如果不是映射到方法，直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        //验证是否有token
        if(!StringUtils.hasLength(token)){
            throw  new ServiceException(Constants.CODE_402,"token失效,请重新登陆");
        }
        //根据token验证是否有该用户
        User user = null;
        try{
            String userId = JWT.decode(token).getAudience().get(0);
            user = userService.getById(userId);
            if(user==null){
                throw new ServiceException(Constants.CODE_402,"用户不存在，请重新登陆");
            }
        }catch (JWTDecodeException e){
            throw new ServiceException(Constants.CODE_402,"token失效，请重新登陆");
        }

        //验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            throw new ServiceException(Constants.CODE_402,"token验证失败，请重新登陆");
        }
        return true;
    }
}
