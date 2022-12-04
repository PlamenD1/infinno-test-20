package com.example.infinnotest20.Interfaces;

import com.example.infinnotest20.Comment;
import com.example.infinnotest20.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PostsMapper {
    @Select("SELECT * FROM posts")
    List<Post> getAllPosts();

    @Select("SELECT * FROM posts WHERE id = #{value}")
    Post getPostById(int id);

    @Select("SELECT * FROM comments where id = #{value}")
    List<Comment> getCommentsForPost(int id);

    @Insert("INSERT INTO posts (post, author) VALUES (#{post}, #{author})")
    int addPost(@Param("post") String post, @Param("author") String author);

    @Update("UPDATE posts SET post = #{post}, author = #{author} WHERE id = #{id}")
    int updatePost(@Param("id") int id, @Param("post") String post, @Param("author") String author);

    @Delete("DELETE FROM posts WHERE id = #{id}")
    int deletePost(int id);
}
