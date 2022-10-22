package com.example.qingge_springboot.mapper;

import com.example.qingge_springboot.entity.GoodStandard;
import com.example.qingge_springboot.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("select * from good_standard where good_id = #{id}")
    public List<GoodStandard> getStandardById(int id);

    public List<Goods> findAll();

    @Update("update goods set is_delete = 1 where id = #{id}")
    void fakeDelete(Long id);

    void insertGood(@Param("goods") Goods goods);

    @Select("SELECT MIN(price) FROM good_standard gs, goods WHERE goods.id = gs.good_id AND goods.id = #{id} ")
    BigDecimal getMinPrice(Long id);

    boolean saleGood(@Param("id")Long goodId,@Param("count") int count,@Param("money") BigDecimal totalPrice);


    @Select("SELECT * FROM `goods` ORDER BY sale_money DESC LIMIT 0,#{num}")
    List<Goods> getSaleRank(int num);
}
