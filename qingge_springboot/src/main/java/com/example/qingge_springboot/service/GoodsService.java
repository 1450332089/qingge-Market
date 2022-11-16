package com.example.qingge_springboot.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.qingge_springboot.constants.Constants;
import com.example.qingge_springboot.entity.GoodStandard;
import com.example.qingge_springboot.entity.Goods;
import com.example.qingge_springboot.exception.ServiceException;
import com.example.qingge_springboot.mapper.GoodsMapper;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.qingge_springboot.constants.RedisConstants.GOOD_TOKEN_KEY;
import static com.example.qingge_springboot.constants.RedisConstants.GOOD_TOKEN_TTL;

@Service
public class GoodsService extends ServiceImpl<GoodsMapper, Goods> {

    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private RedisTemplate<String,Goods> redisTemplate;

    //查询一个商品的信息
    public Goods getGoodById(Long id) {
        String redisKey = GOOD_TOKEN_KEY + id;
        //从redis中查，若有则返回
        ValueOperations<String, Goods> valueOperations = redisTemplate.opsForValue();
        Goods redisGood = valueOperations.get(redisKey);
        if(redisGood!=null){
            return redisGood;
        }
        //若redis中没有则去数据库查
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goods::getIsDelete,false);
        queryWrapper.eq(Goods::getId,id);
        Goods dbGood = getOne(queryWrapper);
        if(dbGood!=null){
            //将商品信息存入redis
            valueOperations.set(redisKey,dbGood);
            redisTemplate.expire(redisKey,GOOD_TOKEN_TTL, TimeUnit.MINUTES);
            return dbGood;
        }
        //数据库中没有则返回异常
        throw new ServiceException(Constants.NO_RESULT,"无结果");

    }
    //查询商品的规格
    public String getStandard(int id){
        List<GoodStandard> standards = goodsMapper.getStandardById(id);
        if(standards.size()==0){
            throw new ServiceException(Constants.NO_RESULT,"无结果");
        }
        return JSON.toJSONString(standards);
    }
    //查询某商品的最低规格价
    public BigDecimal getMinPrice(Long id){
        return goodsMapper.getMinPrice(id);
    }
    //查询全部（首页推荐商品）
    public List<Goods> findAll() {
        return  goodsMapper.findAll();
    }
    //分页查询
    public IPage<Goods> findPage(Integer pageNum, Integer pageSize, String searchText, Integer categoryId) {
        System.out.println(searchText+"    "+ categoryId);
        LambdaQueryWrapper<Goods> query = Wrappers.<Goods>lambdaQuery().orderByDesc(Goods::getId);
        //对名称和描述进行模糊查询
        if (StrUtil.isNotBlank(searchText)) {
            query.like(Goods::getName, searchText).or().like(Goods::getDescription,searchText).or().eq(Goods::getId,searchText);
        }
        if(categoryId != null){
            query.eq(Goods::getCategoryId,categoryId);
        }
        //筛除掉已被删除的商品
        query.eq(Goods::getIsDelete,false);
        IPage<Goods> page = this.page(new Page<>(pageNum, pageSize), query);
        for (Goods good : page.getRecords()) {
            //附上最低价格
            good.setPrice(getMinPrice(good.getId()));
        }
        return page;
    }

    //假删除
    public void deleteGood(Long id) {
        redisTemplate.delete(GOOD_TOKEN_KEY+id);
        goodsMapper.fakeDelete(id);
    }
    //保存商品信息
    public Long saveOrUpdateGood(Goods goods) {
        System.out.println(goods);
        if(goods.getId()==null){
            goodsMapper.insertGood(goods);
        }else{
            saveOrUpdate(goods);
            redisTemplate.delete(GOOD_TOKEN_KEY + goods.getId());
        }
        return goods.getId();
    }

    public boolean setRecommend(Long id,Boolean isRecommend) {
        LambdaUpdateWrapper<Goods> goodsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        goodsLambdaUpdateWrapper.eq(Goods::getId,id)
                .set(Goods::getRecommend,isRecommend);
        return update(goodsLambdaUpdateWrapper);
    }

    public List<Goods> getSaleRank(int num) {
        return goodsMapper.getSaleRank(num);
    }


    public void update(Goods goods) {
        updateById(goods);
        redisTemplate.delete(GOOD_TOKEN_KEY + goods.getId());
    }
}
