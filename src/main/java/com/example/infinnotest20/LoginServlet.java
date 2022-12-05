package com.example.infinnotest20;

import com.example.infinnotest20.Services.CommentsDAO;
import com.example.infinnotest20.Services.LoginDAO;
import com.example.infinnotest20.Services.PostDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;
import java.util.Objects;

@MultipartConfig
@WebServlet(name = "loginServlet", value = "/login-servlet")
public class LoginServlet extends HttpServlet {

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.setPrettyPrinting().create();

    LoginDAO dao = new LoginDAO();

    public LoginServlet() throws FileNotFoundException {}
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] pathParts = new String[0];
        if (request.getPathInfo() != null)
            pathParts = request.getPathInfo().substring(1).split("/");

        if (pathParts.length == 0) {
            Part user = request.getPart("username");
            String userString = new BufferedReader(new InputStreamReader(user.getInputStream())).readLine();
            Part pass = request.getPart("password");
            String passString = new BufferedReader(new InputStreamReader(pass.getInputStream())).readLine();

            if (userString == null ||
                passString == null)
                sendError(response, 401, "Username or password is empty! USER NOT LOGGED IN!");


            if (dao.login(userString, passString)) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", userString);
                sendResponse(response, "USER LOGGED IN!");
                return;
            }

            String invalidCredentials = "Invalid credentials! USER NOT LOGGED IN!";
            sendError(response, 401, invalidCredentials);
        } else sendError(response, 404, "404 Not Found!");

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
}
