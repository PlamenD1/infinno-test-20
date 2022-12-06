package com.example.infinnotest20.Interfaces;

import com.example.infinnotest20.Models.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface LoginMapper {
    @Select("SELECT id FROM users WHERE username = #{user.username} AND password = #{user.password}")
    Integer login(@Param("user") User user);


}
