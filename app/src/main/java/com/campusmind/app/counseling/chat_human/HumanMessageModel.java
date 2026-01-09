package com.campusmind.app.counseling.chat_human;

public class HumanMessageModel {
    private String message;

    public HumanMessageModel(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
