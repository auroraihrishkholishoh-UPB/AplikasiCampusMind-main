package com.campusmind.app.counseling.chat_ai;

public class AIResponse {
    private boolean status;
    private String reply;
    private int sessionId;

    public boolean isStatus() { return status; }
    public String getReply() { return reply; }
    public int getSessionId() { return sessionId; }
}
