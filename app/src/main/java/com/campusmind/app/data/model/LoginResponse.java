package com.campusmind.app.data.model;

public class LoginResponse {
    private boolean success;
    private String message;
    private User user;
    private String token;   // kalau backend ngirim JWT / token

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public User getUser() { return user; }
    public String getToken() { return token; }
}
