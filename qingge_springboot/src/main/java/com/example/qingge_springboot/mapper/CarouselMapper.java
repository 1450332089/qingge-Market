package com.example.qingge_springboot.mapper;

import com.example.qingge_springboot.entity.Carousel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface CarouselMapper extends BaseMapper<Carousel> {

    List<Carousel> getAllCarousel();
}
