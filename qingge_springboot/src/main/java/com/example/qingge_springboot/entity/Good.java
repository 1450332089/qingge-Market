package com.example.qingge_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;

@Data
@TableName("good")
public class Good extends Model<Good> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 商品名称 
      */
    private String name;

    /**
      * 商品描述 
      */
    private String description;



    /**
      * 折扣 
      */
    private Double discount;


    /**
      * 销量 
      */
    private Integer sales;

    /*
    *销售额
    */
    private BigDecimal saleMoney;

    /**
      * 分类id 
      */
    private Long categoryId;

    /**
      * 商品图片 
      */
    private String imgs;

    /**
      * 创建时间 
      */
    private String createTime;

    /**
      * 是否推荐：0不推荐，1推荐 
      */
    private Boolean recommend;


    /**
     * 是否删除
     */
    private Boolean isDelete;
    /**
     * 原价
     */
    @TableField(exist = false)
    private BigDecimal price;
}