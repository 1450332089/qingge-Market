package com.example.qingge_springboot.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("cart")
public class Cart extends Model<Cart> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 商品数量 
      */
    private Integer count;

    /**
      * 加入时间 
      */
    private String createTime;

    /**
      * 商品id 
      */
    private Long goodId;

    private String standard;

    /**
      * 用户id 
      */
    private Long userId;

}