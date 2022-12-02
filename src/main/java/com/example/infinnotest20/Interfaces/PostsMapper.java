package com.example.infinnotest20.Interfaces;

import com.example.infinnotest20.Comment;
import com.example.infinnotest20.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PostsMapper {
    @Select("SELECT * FROM Posts")
    List<Post> getAllPosts();

    @Select("SELECT * FROM Posts where ID == #{id}")
    Post getPostById(@Param("id") int id);

    @Select("SELECT comments FROM Posts where ID == #{id}")
    List<Comment> getCommentsForPost(@Param("id") int id);

    @Insert("INSERT INTO Posts (post, author) VALUES (#{post}, #{author});")
    int addPost(String post, String author);

    @Update("UPDATE Posts SET post = #{post}, author = #{author} WHERE ID == #{id}")
    int updatePost(int id);

    @Delete("DELETE FROM Posts WHERE ID == #{id}")
    int deletePost(int id);
}
