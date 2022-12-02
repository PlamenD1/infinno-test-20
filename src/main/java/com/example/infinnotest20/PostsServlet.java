package com.example.infinnotest20;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.example.infinnotest20.Services.PostDAO;
import com.mysql.cj.xdevapi.JsonLiteral;
import com.mysql.cj.xdevapi.JsonParser;
import com.mysql.cj.xdevapi.JsonString;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.xml.xpath.XPath;

@WebServlet(name = "postsServlet", value = "/posts-servlet")
public class PostsServlet extends HttpServlet {
    private String message;

    PostDAO dao = new PostDAO();

    public PostsServlet() throws FileNotFoundException {
    }

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] pathParts = new String[0];
        if (request.getPathInfo() != null)
            pathParts = request.getPathInfo().substring(1).split("/");

        switch (pathParts.length) {
            case 0: {
                List<Post> posts = dao.getAllPosts();
                System.out.println(posts);
                StringBuilder sb = new StringBuilder();
                for (Post p : posts) {
                    sb.append(p.id).append(" ").append(p.post).append(" ").append(p.author).append("\n");
                }

                response.getOutputStream().write(sb.toString().getBytes(StandardCharsets.UTF_8));
                break;
            }

            case 1: {
                try {
                    int id = Integer.parseInt(pathParts[0]);
                    Post post = dao.getPostById(id);
                    response.getOutputStream().write((post.id + " " + post.post + " " + post.author).getBytes(StandardCharsets.UTF_8));
                } catch (NumberFormatException e) {
                    response.getOutputStream().write(("404 Not Found!").getBytes(StandardCharsets.UTF_8));
                    return;
                }
                break;
            }
        }

        response.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
    }

    public void destroy() {
    }
}