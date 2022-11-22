package com.example.qingge_springboot.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodDTO {
    private Long id;
    private String name;
    private String imgs;
    private BigDecimal price;
}
