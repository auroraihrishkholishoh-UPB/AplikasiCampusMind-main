package com.campusmind.app.counseling.chat_ai;
public class AIMessageModel {
    public static final String ROLE_USER = "user";
    public static final String ROLE_AI = "ai";

    private String role;
    private String message;

    public AIMessageModel(String role, String message) {
        this.role = role;
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }
}
