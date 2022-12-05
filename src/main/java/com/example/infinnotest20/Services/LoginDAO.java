package com.example.infinnotest20.Services;

import com.example.infinnotest20.AuthorizedSqlSessionFactoryBuilder;
import com.example.infinnotest20.Interfaces.LoginMapper;
import com.example.infinnotest20.Post;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class LoginDAO {
    SqlSessionFactory sessionFactory;

    public LoginDAO() throws FileNotFoundException {
        sessionFactory = new SqlSessionFactoryBuilder().build(new FileReader(new File("C:\\Users\\plame\\IdeaProjects\\infinno-test-20\\src\\main\\java\\com\\example\\infinnotest20\\config.xml")));
    }

    public boolean login(String username, String password) {
        try (SqlSession conn = sessionFactory.openSession()) {
            var mapper = conn.getMapper(LoginMapper.class);
            Integer userId = mapper.login(username, password);

            return userId != null;
        }
    }
}
