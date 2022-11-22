package com.example.qingge_springboot.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.qingge_springboot.constants.Constants;
import com.example.qingge_springboot.constants.RedisConstants;
import com.example.qingge_springboot.entity.LoginForm;
import com.example.qingge_springboot.entity.User;
import com.example.qingge_springboot.entity.dto.UserDTO;
import com.example.qingge_springboot.exception.ServiceException;
import com.example.qingge_springboot.mapper.UserMapper;
import com.example.qingge_springboot.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;


@Service
public class UserService extends ServiceImpl<UserMapper,User> {
    @Resource
    RedisTemplate<String,User> redisTemplate;

    public UserDTO login(LoginForm loginForm) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",loginForm.getUsername());
        queryWrapper.eq("password",loginForm.getPassword());

        User user = getOne(queryWrapper);
        if(user == null) {
            throw new ServiceException(Constants.CODE_403,"用户名或密码错误");
        }
        String token = TokenUtils.genToken(user.getId().toString(), user.getUsername());
        //把用户存到redis中
        redisTemplate.opsForValue().set(RedisConstants.USER_TOKEN_KEY +token,user);
        //jwt不设置过期时间，只设置redis过期时间。
        redisTemplate.expire(RedisConstants.USER_TOKEN_KEY +token, RedisConstants.USER_TOKEN_TTL, TimeUnit.MINUTES);
        //把查到的user的一些属性赋值给userDTO
        UserDTO userDTO = BeanUtil.copyProperties(user,UserDTO.class);
        //设置token
        userDTO.setToken(token);
        return userDTO;

    }

    public User register(LoginForm loginForm) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",loginForm.getUsername());
        User user = getOne(queryWrapper);
        if(user!=null){
            throw new ServiceException(Constants.CODE_403,"用户名已被使用");
        }else{
            user = new User();
            BeanUtils.copyProperties(loginForm,user);
            user.setNickname("新用户");
            user.setRole("user");
            save(user);
            return user;
        }
    }

    public User getOne(String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return getOne(queryWrapper);
    }

    @Override
    public boolean saveOrUpdate(User user) {
        return super.saveOrUpdate(user);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }




    /*
    以下为引入mybatis-plus之前的操作

    @Autowired
    private UserMapper userMapper;

    public List<User> findFrontGoods(){
       return userMapper.findFrontGoods();
    };
    public int searchTotal(String username,String nickname){
        return userMapper.searchTotal(username,nickname);
    }
    public int insertOrUpdate(User user){
       if(user.getId()!=null){
           return userMapper.update(user);
       }else {
           return userMapper.insert(user);
       }
    }

    public int delete(int id) {
        return userMapper.deleteById(id);
    }

    public List<User> selectPage(int index, int pageSize,String username,String nickname) {
        if(!username.isEmpty() || !nickname.isEmpty()){
            index = 0;
        }
        return userMapper.selectPage(index,pageSize,username,nickname);
    }
*/
}
