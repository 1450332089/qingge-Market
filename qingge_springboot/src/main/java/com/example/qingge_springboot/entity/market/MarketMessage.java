package com.example.qingge_springboot.entity.market;

import lombok.Data;

@Data
public class MarketMessage {
    private Integer id;
    private Integer goodId;
    private Integer userId;
    private String message;
    private String createTime;
}
