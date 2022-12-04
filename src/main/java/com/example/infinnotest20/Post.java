package com.example.infinnotest20;

public class Post {
    public int id;
    public String post;
    public String author;

    public Post() {}
    public Post(String post, String author) {
        this.post = post;
        this.author = author;
    }
    public Post(int id, String post, String author) {
        this.id = id;
        this.post = post;
        this.author = author;
    }
}
