package com.example.infinnotest20.Interfaces;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface LoginMapper {
    @Select("SELECT id FROM users WHERE username = #{username} AND password = #{password}")
    Integer login(@Param("username") String username,@Param("password") String password);
}
