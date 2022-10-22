package com.example.qingge_springboot.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IncomeMapper {
    @MapKey("categoryId")
    List<Map<String, Object>> selectCategoryIncome();

    BigDecimal selectSumIncome();

    BigDecimal getDayIncome(@Param("thisDay")String thisDay,@Param("nextDay") String nextDay);
}
