package com.example.qingge_springboot.service;

import com.example.qingge_springboot.entity.Cart;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.qingge_springboot.mapper.CartMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CartService extends ServiceImpl<CartMapper, Cart> {

    @Resource
    private CartMapper cartMapper;

    public List<Map<String,Object>> selectByUserId(Long userId) {
         return cartMapper.selectByUserId(userId);
    }
}
