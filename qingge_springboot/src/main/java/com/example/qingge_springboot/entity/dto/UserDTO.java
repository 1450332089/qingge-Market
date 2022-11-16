package com.example.qingge_springboot.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String username;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
}
