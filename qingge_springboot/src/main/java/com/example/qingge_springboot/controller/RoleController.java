package com.example.qingge_springboot.controller;

import com.example.qingge_springboot.common.Result;
import com.example.qingge_springboot.entity.User;
import com.example.qingge_springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {
    @PostMapping("/role")
    public Result getUserRole(){
        User currentUser = TokenUtils.getCurrentUser();
        return Result.success(currentUser.getRole());
    }
}
