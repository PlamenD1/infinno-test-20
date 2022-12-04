package com.example.infinnotest20;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import com.example.infinnotest20.Services.PostDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mysql.cj.xdevapi.JsonLiteral;
import com.mysql.cj.xdevapi.JsonParser;
import com.mysql.cj.xdevapi.JsonString;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.xml.xpath.XPath;

@WebServlet(name = "postsServlet", value = "/posts-servlet")
public class PostsServlet extends HttpServlet {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.setPrettyPrinting().create();

    PostDAO dao = new PostDAO();

    public PostsServlet() throws FileNotFoundException {}

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] pathParts = new String[0];
        if (request.getPathInfo() != null)
            pathParts = request.getPathInfo().substring(1).split("/");

        switch (pathParts.length) {
            case 0: {
                List<Post> posts = dao.getAllPosts();

                sendResponse(response, posts);

                break;
            }

            case 1: {
                try {
                    Object obj;
                    if (pathParts[0].startsWith("comments")) {
                        int id = Integer.parseInt(request.getParameter("postId"));
                        obj = dao.getCommentsForPost(id);
                    } else {
                        int id = Integer.parseInt(pathParts[0]);
                        obj = dao.getPostById(id);
                    }

                    if (obj == null)
                        sendError(response);

                    sendResponse(response, obj);
                } catch (Exception e) {
                    System.out.println(e);
                    sendError(response);
                    return;
                }
                break;
            }

            case 2: {
                Integer id = null;

                try {
                    id = Integer.parseInt(pathParts[0]);
                } catch (Exception e) {
                    sendError(response);
                    return;
                }

                if (!pathParts[1].equals("comments")) {
                    sendError(response);
                    return;
                }

                var obj = dao.getCommentsForPost(id);
                sendResponse(response, obj);
                break;
            }
            default: sendError(response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] pathParts = new String[0];
        if (request.getPathInfo() != null)
            pathParts = request.getPathInfo().substring(1).split("/");

        String jsonString = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Post postToAdd = gson.fromJson(jsonString, Post.class);

        if (pathParts.length == 0) {
            Integer result = null;
                result = dao.addPost(postToAdd.post, postToAdd.author);

            if (result != 1)
                sendError(response);
            else sendResponse(response, result);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] pathParts = new String[0];
        if (request.getPathInfo() != null)
            pathParts = request.getPathInfo().substring(1).split("/");

        String jsonString = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Post postToAdd = gson.fromJson(jsonString, Post.class);

        if (pathParts.length != 1)
            sendError(response);

        Integer id = null;
        try {
            id = Integer.parseInt(pathParts[0]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendError(response);
            return;
        }
        dao.updatePost(id, postToAdd.post, postToAdd.author);
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] pathParts = new String[0];
        if (request.getPathInfo() != null)
            pathParts = request.getPathInfo().substring(1).split("/");

        if (pathParts.length != 1)
            sendError(response);

        Integer id = null;
        try {
            id = Integer.parseInt(pathParts[0]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendError(response);
            return;
        }

        dao.deletePost(id);
    }

    void sendResponse(HttpServletResponse response, Object o) throws IOException {
        String json = gson.toJson(o);

        response.setStatus(200);
        response.addHeader("Content-Length", String.valueOf(json.length()));
        response.addHeader("Content-Type", "application/json");
        response.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
    }

    void sendError(HttpServletResponse response) throws IOException {
        response.setStatus(404);
        response.getOutputStream().write("404 Not Found!".getBytes(StandardCharsets.UTF_8));
    }

    public void destroy() {
    }
}