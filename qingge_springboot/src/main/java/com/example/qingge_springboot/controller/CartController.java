package com.example.qingge_springboot.controller;

import cn.hutool.core.date.DateUtil;
import com.example.qingge_springboot.common.Result;
import com.example.qingge_springboot.entity.Cart;
import com.example.qingge_springboot.service.CartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Resource
    private CartService cartService;

    /*
    查询
    */
    //根据购物车id查询
    @GetMapping("/{id}")
    public Result selectById(@PathVariable Long id) {
        return Result.success(cartService.getById(id));
    }
    //查找所有用户的购物车
    @GetMapping
    public Result findAll() {
        List<Cart> list = cartService.list();
        return Result.success(list);
    }
    //查找某个用户的购物车
    @GetMapping("/userid/{userId}")
    public Result selectByUserId(@PathVariable Long userId) {
        return Result.success(cartService.selectByUserId(userId)) ;
//        LambdaQueryWrapper<Cart> cartQueryWrapper = new LambdaQueryWrapper<>();
//        cartQueryWrapper.eq(Cart::getUserId,userId);
//        List<Cart> list = cartService.list(cartQueryWrapper);
//        return Result.success(list);
    }

    /*
    保存
    */
    @PostMapping
    public Result save(@RequestBody Cart cart) {
        cart.setCreateTime(DateUtil.now());
        cartService.saveOrUpdate(cart);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Cart cart) {
        cartService.updateById(cart);
        return Result.success();
    }

    /*
    删除
    */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        cartService.removeById(id);
        return Result.success();
    }





}
