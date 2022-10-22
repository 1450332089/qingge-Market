package com.example.qingge_springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.qingge_springboot.common.Constants;
import com.example.qingge_springboot.entity.User;
import com.example.qingge_springboot.entity.dto.UserDTO;
import com.example.qingge_springboot.exception.ServiceException;
import com.example.qingge_springboot.mapper.UserMapper;
import com.example.qingge_springboot.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class UserService extends ServiceImpl<UserMapper,User> {

    public UserDTO login(UserDTO dto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",dto.getUsername());
        queryWrapper.eq("password",dto.getPassword());

        User user = getOne(queryWrapper);
        if(user!=null){
            UserDTO userDTO = new UserDTO();
            //把查到的user的一些属性赋值给userDTO
            BeanUtils.copyProperties(user,userDTO,"password");
            //设置token
            String token = TokenUtils.genToken(user.getId().toString(), user.getPassword());
            userDTO.setToken(token);
            return userDTO;
        }else{
            throw new ServiceException(Constants.CODE_403,"用户名或密码错误");
        }
    }

    public User register(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDTO.getUsername());
        User user = getOne(queryWrapper);
        if(user!=null){
            throw new ServiceException(Constants.CODE_403,"用户名已被使用");
        }else{
            user = new User();
            BeanUtils.copyProperties(userDTO,user);
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

    public List<User> findAll(){
       return userMapper.findAll();
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
