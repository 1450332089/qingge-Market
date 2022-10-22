package com.example.qingge_springboot.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.qingge_springboot.common.Constants;
import com.example.qingge_springboot.controller.GoodsController;
import com.example.qingge_springboot.entity.GoodStandard;
import com.example.qingge_springboot.entity.Goods;
import com.example.qingge_springboot.entity.MyFile;
import com.example.qingge_springboot.exception.ServiceException;
import com.example.qingge_springboot.mapper.FileMapper;
import com.example.qingge_springboot.mapper.GoodsMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class GoodsService extends ServiceImpl<GoodsMapper, Goods> {

    @Resource
    private GoodsMapper goodsMapper;

    //查询商品的规格
    public String getStandard(int id){
        List<GoodStandard> standards = goodsMapper.getStandardById(id);
        System.out.println(standards);
        System.out.println(JSON.toJSONString(standards));
        if(standards.size()==0){
            throw new ServiceException(Constants.CODE_510,"无结果");
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
        goodsMapper.fakeDelete(id);
    }
    //保存商品信息
    public Long saveOrUpdateGood(Goods goods) {
        System.out.println("goods"+goods);
        if(goods.getId()==null){
            goodsMapper.insertGood(goods);
        }else{

            saveOrUpdate(goods);
        }
        System.out.println(goods.getId());
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
}
