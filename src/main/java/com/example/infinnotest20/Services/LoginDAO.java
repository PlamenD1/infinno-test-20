package com.example.infinnotest20.Services;

import com.example.infinnotest20.Interfaces.LoginMapper;
import com.example.infinnotest20.Models.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.Objects;

public class LoginDAO {
    SqlSessionFactory sessionFactory;

    public LoginDAO() throws FileNotFoundException, URISyntaxException {
        sessionFactory = new SqlSessionFactoryBuilder().build(new FileReader(new File(Objects.requireNonNull(this.getClass().getResource("/config.xml")).getFile())));
    }

    public boolean login(User user) {
        try (SqlSession conn = sessionFactory.openSession()) {
            var mapper = conn.getMapper(LoginMapper.class);
            Integer userId = mapper.login(user);

            return userId != null;
        }
    }

    public Integer getUserSalt(String username) {
        try (SqlSession conn = sessionFactory.openSession()) {
            var mapper = conn.getMapper(LoginMapper.class);
            return mapper.getUserSalt(username);
        }
    }

}
