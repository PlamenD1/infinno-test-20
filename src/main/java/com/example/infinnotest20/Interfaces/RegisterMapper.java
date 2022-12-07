package com.example.infinnotest20.Interfaces;

import com.example.infinnotest20.Models.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface RegisterMapper {
    @Insert("INSERT INTO users (username, password, salt) VALUES (#{user.username}, #{user.password}, #{user.salt})")
    int register(@Param("user") User user);
}
