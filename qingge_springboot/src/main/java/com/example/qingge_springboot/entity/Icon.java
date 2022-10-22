package com.example.qingge_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.List;


@Data
@TableName(value="icon")
public class Icon extends Model<Icon> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 图标的标识码 
      */
    private String value;

    @TableField(exist = false)
    private List<Category> categories;;
}