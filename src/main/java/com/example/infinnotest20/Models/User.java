package com.example.infinnotest20.Models;

public class User {
    public int id;
    public String username;
    public String password;

    public User() {}
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
