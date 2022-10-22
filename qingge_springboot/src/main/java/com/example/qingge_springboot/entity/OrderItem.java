package com.example.qingge_springboot.entity;

import lombok.Data;

/*
用来接收前台下订单时，传来的‘goods'参数
 */
@Data
public class OrderItem {

    /*
    商品id
     */
    private long id;
    /*
    商品规格
     */
    private String standard;
    /*
    数量
     */
    private int num;

}
