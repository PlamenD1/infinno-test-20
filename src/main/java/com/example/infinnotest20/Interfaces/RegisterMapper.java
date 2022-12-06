package com.example.infinnotest20.Interfaces;

import com.example.infinnotest20.Models.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface RegisterMapper {
    @Insert("INSERT INTO users (username, password) VALUES (#{user.username}, #{user.password})")
    int register(@Param("user") User user);
}
