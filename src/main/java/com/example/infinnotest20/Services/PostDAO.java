package com.example.infinnotest20.Services;

import com.example.infinnotest20.Comment;
import com.example.infinnotest20.Interfaces.PostsMapper;
import com.example.infinnotest20.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class PostDAO {
       SqlSessionFactory sessionFactory;

       public PostDAO() throws FileNotFoundException {
              sessionFactory = new SqlSessionFactoryBuilder().build(new FileReader(new File("C:\\Users\\plame\\IdeaProjects\\infinno-test-20\\src\\main\\java\\com\\example\\infinnotest20\\config.xml")));
       }

       public List<Post> getAllPosts() {
              try (SqlSession conn = sessionFactory.openSession()) {
                     var mapper = conn.getMapper(PostsMapper.class);
                     return mapper.getAllPosts();
              }
       }

       public Post getPostById(int id) {
              try (SqlSession conn = sessionFactory.openSession()) {
                     var mapper = conn.getMapper(PostsMapper.class);
                     return mapper.getPostById(id);
              }
       }

       public List<Comment> getCommentsForPost(int id) {
              try (SqlSession conn = sessionFactory.openSession()) {
                     var mapper = conn.getMapper(PostsMapper.class);
                     return mapper.getCommentsForPost(id);
              }
       }

       public int addPost(String post, String author) {
              try (SqlSession conn = sessionFactory.openSession()) {
                     var mapper = conn.getMapper(PostsMapper.class);
                     int result = mapper.addPost(post, author);
                     conn.commit();

                     return result;
              }
       }

       public int updatePost(int id, String post, String author) {
              try (SqlSession conn = sessionFactory.openSession()) {
                     var mapper = conn.getMapper(PostsMapper.class);
                     int result = mapper.updatePost(id, post, author);
                     conn.commit();

                     return result;
              }
       }

       public int deletePost(int id) {
              try (SqlSession conn = sessionFactory.openSession()) {
                     var mapper = conn.getMapper(PostsMapper.class);
                     int result = mapper.deletePost(id);
                     conn.commit();

                     return result;
              }
       }
}