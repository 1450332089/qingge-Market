package com.example.qingge_springboot.mapper;

import com.example.qingge_springboot.entity.Standard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface StandardMapper extends BaseMapper<Standard> {
    @Update("update good_standard set store = #{num} where good_id = #{good_id} and value = #{standard}")
    void deductStore(long good_id, String standard, @Param("num") int num);

    @Select("select store from good_standard where good_id = #{good_id} and value = #{standard}")
    int getStore(long good_id,String standard);
}
