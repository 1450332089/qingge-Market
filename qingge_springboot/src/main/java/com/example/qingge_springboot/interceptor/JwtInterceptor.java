package com.example.qingge_springboot.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.qingge_springboot.constants.Constants;
import com.example.qingge_springboot.constants.UserConstants;
import com.example.qingge_springboot.entity.User;
import com.example.qingge_springboot.exception.ServiceException;
import com.example.qingge_springboot.service.UserService;
import com.example.qingge_springboot.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
第一层拦截器，验证用户token,把redis中的user存到threadlocal
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Resource
    RedisTemplate<String, User> redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        //如果不是映射到方法，直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        //验证是否有token
        if(!StringUtils.hasLength(token)){
            throw  new ServiceException(Constants.TOKEN_ERROR,"token失效,请重新登陆");
        }
        //将redis中的user存到threadlocal
        User user = redisTemplate.opsForValue().get(UserConstants.USER_TOKEN_KEY + token);
        if(user == null){
            throw  new ServiceException(Constants.TOKEN_ERROR,"token失效,请重新登陆");
        }
        UserHolder.saveUser(user);

        //验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getUsername())).build();
        try {
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            throw new ServiceException(Constants.TOKEN_ERROR,"token验证失败，请重新登陆");
        }
        return true;
    }
}
