package com.example.qingge_springboot.mapper;

import com.example.qingge_springboot.entity.Icon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface IconMapper extends BaseMapper<Icon> {

    List<Icon> getIconCategoryMapList();
}
