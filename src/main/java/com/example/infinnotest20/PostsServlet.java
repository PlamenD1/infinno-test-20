package com.example.infinnotest20;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import com.example.infinnotest20.Services.PostDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "postsServlet", value = "/posts-servlet")
public class PostsServlet extends HttpServlet {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.setPrettyPrinting().create();

    PostDAO dao = new PostDAO();

    public PostsServlet() throws FileNotFoundException {}

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!isAuthorized(request, response))
            return;

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
                    int id = Integer.parseInt(pathParts[0]);
                    Post post = dao.getPostById(id);

                    if (post == null)
                        sendError(response, 404, "404 Not Found!");

                    sendResponse(response, post);
                } catch (Exception e) {
                    System.out.println(e);
                    sendError(response, 404, "404 Not Found!");
                    return;
                }
                break;
            }

            case 2: {
                Integer id = null;

                try {
                    id = Integer.parseInt(pathParts[0]);
                } catch (Exception e) {
                    sendError(response, 404, "404 Not Found!");
                    return;
                }

                if (!pathParts[1].equals("comments")) {
                    sendError(response, 404, "404 Not Found!");
                    return;
                }

                var obj = dao.getCommentsForPost(id);
                sendResponse(response, obj);
                break;
            }
            default: sendError(response, 404, "404 Not Found!");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!isAuthorized(request, response))
            return;

        String[] pathParts = new String[0];
        if (request.getPathInfo() != null)
            pathParts = request.getPathInfo().substring(1).split("/");

        Post postToAdd = getPostFromBody(request);

        if (pathParts.length == 0) {
            int rowsAffected = dao.addPost(postToAdd);

            if (rowsAffected != 1)
                sendError(response, 400, "400 Couldn't insert post!");

            String addedPostId = "Added post id: " + postToAdd.id;
            sendResponse(response, addedPostId);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!isAuthorized(request, response))
            return;

        String[] pathParts = new String[0];
        if (request.getPathInfo() != null)
            pathParts = request.getPathInfo().substring(1).split("/");

        Post postToAdd = getPostFromBody(request);

        if (pathParts.length != 1)
            sendError(response, 404, "404 Not Found!");

        Integer id = null;
        try {
            id = Integer.parseInt(pathParts[0]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendError(response, 404, "404 Not Found!");
            return;
        }
        postToAdd.id = id;

        int rowsAffected = dao.updatePost(postToAdd);
        if (rowsAffected != 1)
            sendError(response, 400, "Error while updating post!");

        sendResponse(response, postToAdd);
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!isAuthorized(request, response))
            return;

        String[] pathParts = new String[0];
        if (request.getPathInfo() != null)
            pathParts = request.getPathInfo().substring(1).split("/");

        if (pathParts.length != 1)
            sendError(response, 404, "404 Not Found!");

        Integer id = null;
        try {
            id = Integer.parseInt(pathParts[0]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendError(response, 404, "404 Not Found!");
            return;
        }

        int rowsAffected = dao.deletePost(id);
        if (rowsAffected != 1)
            sendError(response, 400, "Error while deleting post!");

        sendResponse(response, "Deleted post with id: " + id);
    }

    void sendResponse(HttpServletResponse response, Object o) throws IOException {
        String json = gson.toJson(o);

        response.setStatus(200);
        response.addHeader("Content-Length", String.valueOf(json.length()));
        response.addHeader("Content-Type", "application/json");
        response.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
    }

    void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
    }

    boolean isAuthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            sendError(response, 403, "Unauthorized user!");
            return false;
        }

        return true;
    }

    Post getPostFromBody(HttpServletRequest request) throws IOException {
        String jsonString = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return gson.fromJson(jsonString, Post.class);
    }
}