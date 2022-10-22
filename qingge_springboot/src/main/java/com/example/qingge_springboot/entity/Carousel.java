package com.example.qingge_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("carousel")
public class Carousel extends Model<Carousel> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 对应的商品 
      */
    private Long goodId;

    /**
      * 轮播顺序 
      */
    private Integer showOrder;

    @TableField(exist = false)
    private String goodName;

    @TableField(exist = false)
    private String img;
}