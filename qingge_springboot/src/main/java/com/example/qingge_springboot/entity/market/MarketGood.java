package com.example.qingge_springboot.entity.market;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MarketGood {
    private int id;
    private int userId;
    private String goodName;
    private String description;
    private String img;
    private BigDecimal price;
    private String state;
    private String createTime;
    //发布者姓名
    @TableField(exist = false)
    private String nickname;
}
