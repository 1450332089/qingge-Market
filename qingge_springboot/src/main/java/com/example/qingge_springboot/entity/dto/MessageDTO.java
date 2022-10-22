package com.example.qingge_springboot.entity.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private Integer id;
    private Integer userId;
    private String avatar;
    private String nickname;
    private String message;
    private String createTime;
}
