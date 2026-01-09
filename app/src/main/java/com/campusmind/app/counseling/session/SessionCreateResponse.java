package com.campusmind.app.counseling.session;

public class SessionCreateResponse {
    private boolean success;
    private String message;
    private int sessionId;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getSessionId() { return sessionId; }

    public void setSuccess(boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }
}
