package com.campusmind.app.auth;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    private UserModel user;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public UserModel getUser() { return user; }

    public void setSuccess(boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
    public void setToken(String token) { this.token = token; }
    public void setUser(UserModel user) { this.user = user; }
}
