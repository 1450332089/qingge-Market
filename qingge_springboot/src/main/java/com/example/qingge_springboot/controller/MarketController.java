package com.example.qingge_springboot.controller;

import com.example.qingge_springboot.common.Result;
import com.example.qingge_springboot.entity.market.MarketGood;
import com.example.qingge_springboot.entity.market.MarketMessage;
import com.example.qingge_springboot.service.MarketService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    @Resource
    private MarketService marketService;

    /*
    获得学校列表
     */
    @GetMapping("/schools")
    public Result getAllSchools(){
        return Result.success(marketService.getAllSchools());
    }
    /*
    获得学校下的所有商品
     */
    @GetMapping("/schoolGood/{schoolId}")
    public Result getSchoolGoods(@PathVariable int schoolId){
        return Result.success(marketService.getSchoolGoods(schoolId));
    }
    //获取某人发布的商品
    @GetMapping("/userid/{userId}")
    public Result getGoodByUserId(@PathVariable Integer userId){
        return Result.success(marketService.getGoodByUserId(userId));
    }
    //根据id查询商品
    @GetMapping("/good/{goodId}")
    public Result getGoodById(@PathVariable int goodId){
        return Result.success(marketService.getGoodById(goodId));
    }
    //根据商品id查询所有留言
    @GetMapping("/message/{goodId}")
    public Result getMessageByGoodId(@PathVariable Integer goodId){
        return Result.success(marketService.getMessageByGoodId(goodId));
    }
    /*
      保存
     */
    //保存商品信息
    @PostMapping("/good")
    public Result uploadGood(@RequestBody MarketGood marketGood,@RequestParam Integer schoolId){
        marketService.uploadGood(marketGood,schoolId);
        return Result.success();
    }
    //保存留言
    @PostMapping("/message")
    public Result sendMessage(@RequestBody MarketMessage message){
        marketService.sendMessage(message);
        return Result.success();
    }



    /*
    删除
     */
    //删除留言
    @DeleteMapping("/message/{id}")
    public Result delMessage(@PathVariable Integer id){
        return Result.success(marketService.delMessage(id));
    }





}
