package com.example.qingge_springboot.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.qingge_springboot.common.Constants;
import com.example.qingge_springboot.entity.User;
import com.example.qingge_springboot.exception.ServiceException;
import com.example.qingge_springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

@Component
public class TokenUtils {
    private static final long EXPIRE_TIME = 180*60*1000; //过期时间，毫秒
    @Resource
    private UserService userService;
    private static UserService staticUserService;

    @PostConstruct
    public void setStaticUserService() {
        staticUserService = userService;
    }

    public static String genToken(String userId, String password){
        Date expireDate = new Date(System.currentTimeMillis()+EXPIRE_TIME);
        String token = JWT.create()
                .withAudience(userId)
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(password));
        return token;
    }


    public static User getCurrentUser(){
        try{
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String token = request.getHeader("token");
            if(StringUtils.hasLength(token)){
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(userId);
            }else
                throw new ServiceException(Constants.CODE_401,"token失效！");
        }catch (Exception e){
            return null;
        }
    }
    public static boolean validateLogin(){
        try{
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String token = request.getHeader("token");
            if(StringUtils.hasLength(token)){
                JWT.decode(token).getAudience();
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_401,"登录状态失效！");
        }
    }

    public static boolean validateAuthority(){
        try{
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String token = request.getHeader("token");
            if(StringUtils.hasLength(token)){
                String userId = JWT.decode(token).getAudience().get(0);
                User user = staticUserService.getById(userId);
                if(user.getRole().equals("admin")){
                    return true;
                }
            }
        }catch (Exception e){
            return false;
        }
        throw new ServiceException(Constants.CODE_403,"无权限！");
    }

}
