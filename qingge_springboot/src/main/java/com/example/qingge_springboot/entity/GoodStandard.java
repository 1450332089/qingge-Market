package com.example.qingge_springboot.entity;

import lombok.Data;

/*
商品规格及对应库存
 */
@Data
public class GoodStandard {
    private int goodId;
    private String value;
    private double price;
    private int store;
}
