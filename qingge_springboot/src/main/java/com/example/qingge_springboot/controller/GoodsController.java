package com.example.qingge_springboot.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.qingge_springboot.annotation.Authority;
import com.example.qingge_springboot.constants.Constants;
import com.example.qingge_springboot.common.Result;
import com.example.qingge_springboot.entity.AuthorityType;
import com.example.qingge_springboot.entity.Goods;
import com.example.qingge_springboot.entity.Standard;
import com.example.qingge_springboot.entity.User;
import com.example.qingge_springboot.service.GoodsService;
import com.example.qingge_springboot.service.StandardService;
import com.example.qingge_springboot.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private UserService userService;
    @Resource
    private StandardService standardService;

    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @Authority(AuthorityType.requireAuthority)
    @PostMapping
    public Result save(@RequestBody Goods goods) {
        System.out.println(123);
        System.out.println(goods);
        return Result.success(goodsService.saveOrUpdateGood(goods));
    }

    @Authority(AuthorityType.requireAuthority)
    @PutMapping
    public Result update(@RequestBody Goods goods) {
        goodsService.update(goods);
        return Result.success();
    }

    @Authority(AuthorityType.requireAuthority)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        goodsService.deleteGood(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        return Result.success(goodsService.getGoodById(id));
    }

    //???????????????????????????
    @GetMapping("/standard/{id}")
    public Result getStandard(@PathVariable int id) {
        return Result.success(goodsService.getStandard(id));
    }
    //????????????????????????recommend=1
    @GetMapping
    public Result findAll() {
        List<Goods> list = goodsService.findAll();
        return Result.success(list);
    }
    //??????????????????
    @GetMapping("/rank")
    public Result getSaleRank(@RequestParam int num){
        return Result.success(goodsService.getSaleRank(num));
    }
    //???????????????????????????
    @PostMapping("/standard")
    public Result saveStandard(@RequestBody List<Standard> standards,@RequestParam int goodId) {
        //????????????????????????
        standardService.deleteAll(goodId);
        //?????????????????????
        for (Standard standard : standards) {
            standard.setGoodId(goodId);
            if(!standardService.save(standard)){
                return Result.error(Constants.CODE_500,"????????????");
            }
        }
        return Result.success();
    }

    //???????????????????????????
    @Authority(AuthorityType.requireAuthority)
    @DeleteMapping("/standard")
    public Result delStandard(@RequestBody Standard standard) {
        boolean delete = standardService.delete(standard);
        if(delete) {
            return Result.success();
        }else {
            return Result.error(Constants.CODE_500,"????????????");
        }
    }

    //???????????????????????????
    @Authority(AuthorityType.requireAuthority)
    @GetMapping("/recommend")
    public Result setRecommend(@RequestParam Long id,@RequestParam Boolean isRecommend){
        return Result.success(goodsService.setRecommend(id,isRecommend));
    }

    @GetMapping("/page")
    public Result findPage(
                            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                            @RequestParam(required = false, defaultValue = "") String searchText,
                            @RequestParam(required = false) Integer categoryId) {

        return Result.success(goodsService.findPage(pageNum,pageSize,searchText,categoryId));
    }

}
