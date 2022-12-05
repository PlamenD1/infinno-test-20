package com.example.infinnotest20;

import com.example.infinnotest20.Services.CommentsDAO;
import com.example.infinnotest20.Services.PostDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(name = "commentsServlet", value = "/comments-servlet")
public class CommentsServlet extends HttpServlet {

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.setPrettyPrinting().create();

    CommentsDAO dao = new CommentsDAO();

    public CommentsServlet() throws FileNotFoundException {}
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!isAuthorized(request, response))
            return;

        String[] pathParts = new String[0];
        if (request.getPathInfo() != null)
            pathParts = request.getPathInfo().substring(1).split("/");

        if (pathParts.length != 0)
            sendError(response, 404, "404 Not Found!");

        Integer id = null;
        try {
            id = Integer.parseInt(request.getParameter("postId"));
        } catch (Exception e) {
            sendError(response, 404, "404 Not Found!");
            return;
        }

        List<Comment> result = dao.getCommentsByPost(id);

        sendResponse(response, result);
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
}
