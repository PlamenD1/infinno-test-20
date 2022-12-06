package com.example.infinnotest20.Services;

import com.example.infinnotest20.Models.Comment;
import com.example.infinnotest20.Interfaces.CommentsMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class CommentsDAO {
    SqlSessionFactory sessionFactory;
    public CommentsDAO() throws FileNotFoundException {
        sessionFactory = new SqlSessionFactoryBuilder().build(new FileReader(new File("C:\\Users\\plame\\IdeaProjects\\infinno-test-20\\src\\main\\java\\com\\example\\infinnotest20\\config.xml")));
    }

    public List<Comment> getCommentsByPost(int id) {
         try (SqlSession conn = sessionFactory.openSession()) {
            var mapper = conn.getMapper(CommentsMapper.class);
            return mapper.getCommentsByPost(id);
        }
    }
}
