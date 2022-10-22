package com.example.qingge_springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.qingge_springboot.common.Constants;
import com.example.qingge_springboot.common.Result;
import com.example.qingge_springboot.entity.User;
import com.example.qingge_springboot.entity.dto.UserDTO;
import com.example.qingge_springboot.service.UserService;
import com.example.qingge_springboot.utils.TokenUtils;
import com.sun.xml.internal.fastinfoset.stax.events.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
这个注解表示该控制器下所有接口都可以通过跨域访问，注解内可以指定某一域名
也可以配置config类
 */
@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        UserDTO dto = userService.login(userDTO);
        return Result.success(dto);
    }
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO){
        User user = userService.register(userDTO);
        return Result.success(user);
    }

    @GetMapping("/userinfo/{username}")
    public Result getUserInfoByName(@PathVariable String username){
        User one = userService.getOne(username);
        return Result.success(one);
    }
    @GetMapping("/userid")
    public long getUserId(){
        return TokenUtils.getCurrentUser().getId();
    }
    @GetMapping("/user/")
    public Result findAll(){
        List<User> list = userService.list();
        return Result.success(list);
    }
    @PostMapping("/user")
    public Result save(@RequestBody User user){

        boolean isSuccessful = userService.saveOrUpdate(user);
        if(isSuccessful){
            return Result.success();
        }else{
            return Result.error(Constants.CODE_500,"保存失败");
        }
    }

    @DeleteMapping("/user/{id}")
    public Result deleteById(@PathVariable int id){
        boolean isSuccessful = userService.removeById(id);
        if(isSuccessful){
            return Result.success();
        }else{
            return Result.error(Constants.CODE_500,"删除失败");
        }
    }

    @PostMapping("/user/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        boolean isSuccessful = userService.removeBatchByIds(ids);
        if(isSuccessful){
            return Result.success();
        }else{
            return Result.error(Constants.CODE_500,"删除失败");
        }
    }

    @GetMapping("/user/page")
    public Result findPage(@RequestParam int pageNum,
                                @RequestParam int pageSize,
                                String id,
                                String username,
                                String nickname){
            IPage<User> userPage = new Page<>(pageNum,pageSize);
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            if(!Util.isEmptyString(id)){
                userQueryWrapper.like("id",id);
            }
            if(!Util.isEmptyString(username)){
                userQueryWrapper.like("username",username);
            }
            if(!Util.isEmptyString(nickname)){
                userQueryWrapper.like("nickname",nickname);
            }
            userQueryWrapper.orderByDesc("id");
        System.out.println("============"+TokenUtils.getCurrentUser());
        return Result.success(userService.page(userPage,userQueryWrapper));
    }
//    @GetMapping("/page")
//    public Map<String,Object> findPage(@RequestParam int pageNum,
//                                       @RequestParam int pageSize,
//                                        String username,
//                                        String nickname){
//        Map<String,Object> map = new HashMap<>();
//
//        int index = (pageNum - 1) * pageSize;
//        //查询该页信息
//        List<User> data = userService.selectPage(index,pageSize,username,nickname);
//        //查询总人数
//        int total = userService.searchTotal(username,nickname);
//        map.put("total",total);
//        map.put("data",data);
//        return map;
//    }
}
