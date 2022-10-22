package com.example.qingge_springboot.mapper;

import com.example.qingge_springboot.entity.dto.MessageDTO;
import com.example.qingge_springboot.entity.market.MarketGood;
import com.example.qingge_springboot.entity.market.MarketMessage;
import com.example.qingge_springboot.entity.market.MarketSchool;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

public interface MarketMapper {
    @Select("select * from market_school")
    List<MarketSchool> getAllSchools();

    @Select("SELECT market_good.* FROM `market_school_good`,market_good WHERE market_school_good.school_id = #{schoolId} AND market_good.id = market_school_good.good_id and state = '在售' order by create_time desc")
    List<MarketGood> getSchoolGoods(int schoolId);

    @Select("select mg.*,nickname from market_good as mg,sys_user where mg.id = #{goodId} and mg.user_id = sys_user.id")
    MarketGood getGoodById(int goodId);

    @Insert("insert into market_message(good_id,user_id, message, create_time) values (#{goodId},#{userId},#{message},#{createTime})")
    void sendMessage(MarketMessage message);

    List<MessageDTO> getMessageByGoodId(int goodId);

    @Delete("delete from market_message where id = #{id}")
    boolean delMessage(Integer id);

    boolean uploadGood(MarketGood marketGood);

    @Insert("insert into market_school_good(school_id, good_id) VALUES (#{schoolId},#{goodId})")
    void addSchoolGood(@Param("schoolId") Integer schoolId,@Param("goodId") int goodId);

    @Select("select * from market_good where user_id = #{userId} order by create_time desc")
    List<MarketGood> getGoodByUserId(Integer userId);
}
