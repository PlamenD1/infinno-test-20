package com.example.infinnotest20;

import static jakarta.servlet.http.HttpServletResponse.*;

import com.example.infinnotest20.Models.User;
import com.example.infinnotest20.Services.LoginDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@MultipartConfig
@WebServlet(name = "loginServlet", value = "/login-servlet")
public class LoginServlet extends HttpServlet {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.setPrettyPrinting().create();

    LoginDAO dao = new LoginDAO();

    public LoginServlet() throws FileNotFoundException {}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Part user = request.getPart("username");
        String userString = new BufferedReader(new InputStreamReader(user.getInputStream())).readLine();
        Part pass = request.getPart("password");
        String passString = new BufferedReader(new InputStreamReader(pass.getInputStream())).readLine();
        String hashPass = DigestUtils.sha1Hex(passString);

        String path = request.getPathInfo();
        if (path != null && !path.equals("/"))
            sendError(response, SC_NOT_FOUND, "404 Not Found!");

        if (userString == null ||
                passString == null) {
            sendError(response, SC_UNAUTHORIZED, "Username or password is empty! USER NOT LOGGED IN!");
            return;
        }

        if (!dao.login(new User(userString, hashPass))) {
            String invalidCredentials = "Invalid credentials! USER NOT LOGGED IN!";
            sendError(response, SC_UNAUTHORIZED, invalidCredentials);
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("user", userString);
    }

    void sendResponse(HttpServletResponse response, Object o) throws IOException {
        String json = gson.toJson(o);

        response.setStatus(200);
        response.addHeader("Content-Type", "application/json");
        response.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
    }

    void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        String errorMessage = gson.toJson(message);

        response.addHeader("Content-Type", "application/json");
        response.getOutputStream().write(errorMessage.getBytes(StandardCharsets.UTF_8));
    }
}
