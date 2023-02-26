package com.example.qingge_springboot.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.qingge_springboot.constants.Constants;
import com.example.qingge_springboot.entity.Good;
import com.example.qingge_springboot.entity.GoodStandard;
import com.example.qingge_springboot.entity.dto.GoodDTO;
import com.example.qingge_springboot.exception.ServiceException;
import com.example.qingge_springboot.mapper.GoodMapper;

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
public class GoodService extends ServiceImpl<GoodMapper, Good> {

    @Resource
    private GoodMapper goodMapper;
    @Resource
    private RedisTemplate<String, Good> redisTemplate;

    //查询一个商品的信息
    public Good getGoodById(Long id) {
        String redisKey = GOOD_TOKEN_KEY + id;
        //从redis中查，若有则返回
        ValueOperations<String, Good> valueOperations = redisTemplate.opsForValue();
        Good redisGood = valueOperations.get(redisKey);
        if(redisGood!=null){
            redisTemplate.expire(redisKey,GOOD_TOKEN_TTL, TimeUnit.MINUTES);
            return redisGood;
        }
        //若redis中没有则去数据库查
        LambdaQueryWrapper<Good> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Good::getIsDelete,false);
        queryWrapper.eq(Good::getId,id);
        Good dbGood = getOne(queryWrapper);
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
        List<GoodStandard> standards = goodMapper.getStandardById(id);
        if(standards.size()==0){
            throw new ServiceException(Constants.NO_RESULT,"无结果");
        }
        return JSON.toJSONString(standards);
    }
    //查询某商品的最低规格价
    public BigDecimal getMinPrice(Long id){
        return goodMapper.getMinPrice(id);
    }
    //查询全部（首页推荐商品）
    public List<GoodDTO> findFrontGoods() {
        return goodMapper.findFrontGoods();
    }


    //假删除
    public void deleteGood(Long id) {
        redisTemplate.delete(GOOD_TOKEN_KEY+id);
        goodMapper.fakeDelete(id);
    }
    //保存商品信息
    public Long saveOrUpdateGood(Good good) {
        System.out.println(good);
        if(good.getId()==null){
            goodMapper.insertGood(good);
        }else{
            saveOrUpdate(good);
            redisTemplate.delete(GOOD_TOKEN_KEY + good.getId());
        }
        return good.getId();
    }

    public boolean setRecommend(Long id,Boolean isRecommend) {
        LambdaUpdateWrapper<Good> goodsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        goodsLambdaUpdateWrapper.eq(Good::getId,id)
                .set(Good::getRecommend,isRecommend);
        return update(goodsLambdaUpdateWrapper);
    }

    public List<Good> getSaleRank(int num) {
        return goodMapper.getSaleRank(num);
    }


    public void update(Good good) {
        updateById(good);
        redisTemplate.delete(GOOD_TOKEN_KEY + good.getId());
    }
    //分页查询
    public IPage<GoodDTO> findPage(Integer pageNum, Integer pageSize, String searchText, Integer categoryId) {
        LambdaQueryWrapper<Good> query = Wrappers.<Good>lambdaQuery().orderByDesc(Good::getId);
        //对名称和描述进行模糊查询
        if (StrUtil.isNotBlank(searchText)) {
            query.like(Good::getName, searchText).or().like(Good::getDescription,searchText).or().eq(Good::getId,searchText);
        }
        if(categoryId != null){
            query.eq(Good::getCategoryId,categoryId);
        }
        //筛除掉已被删除的商品
        query.eq(Good::getIsDelete,false);
        IPage<Good> page = this.page(new Page<>(pageNum, pageSize), query);
        //把good转为dto
        IPage<GoodDTO> goodDTOPage = page.convert(good -> {
            GoodDTO goodDTO = new GoodDTO();
            BeanUtil.copyProperties(good, goodDTO);
            return goodDTO;
        });
        for (GoodDTO good : goodDTOPage.getRecords()) {
            //附上最低价格
            good.setPrice(getMinPrice(good.getId()));
        }
        return goodDTOPage;
    }
    public IPage<Good> findFullPage(Integer pageNum, Integer pageSize, String searchText, Integer categoryId) {
        LambdaQueryWrapper<Good> query = Wrappers.<Good>lambdaQuery().orderByDesc(Good::getId);
        //对名称和描述进行模糊查询
        if (StrUtil.isNotBlank(searchText)) {
            query.like(Good::getName, searchText).or().like(Good::getDescription,searchText).or().eq(Good::getId,searchText);
        }
        if(categoryId != null){
            query.eq(Good::getCategoryId,categoryId);
        }
        //筛除掉已被删除的商品
        query.eq(Good::getIsDelete,false);
        IPage<Good> page = this.page(new Page<>(pageNum, pageSize), query);
        for (Good good : page.getRecords()) {
            //附上最低价格
            good.setPrice(getMinPrice(good.getId()));
        }
        return page;
    }
}
