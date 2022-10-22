package com.example.qingge_springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;

@Data
@TableName("good_standard")
public class Standard extends Model<Standard> {

    /**
      * 商品id 
      */
    private Integer goodId;

    /**
      * 商品规格 
      */
    private String value;

    /**
      * 该规格的价格 
      */
    private BigDecimal price;

    /**
      * 该规格的库存 
      */
    private Integer store;

}