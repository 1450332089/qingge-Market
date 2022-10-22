package com.example.qingge_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.qingge_springboot.entity.User;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /*
        以下为引入mybatis-plus之前的操作

    @Select("select * from sys_user")
    List<User> findAll();
    @Insert("insert into sys_user(username,password,nickname,email,phone,address) " +
        "values (#{username},#{password},#{nickname},#{email},#{phone},#{address})")
    int insert(User user);

    int update(User user);

    @Delete("delete from sys_user where id = #{id}")
    int deleteById(@Param("id") int id);

    List<User> selectPage(int index,int pageSize,String username,String nickname);

    int searchTotal(String username, String nickname);

     */
}
