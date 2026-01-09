package com.campusmind.app.counseling.session;

public class SessionModel {

    // Untuk sekarang cukup kirim tipe sesi, nanti bisa ditambah counselorId kalau perlu
    private String sessionType;   // "human" atau "ai"
    private Integer counselorId;  // boleh null kalau AI

    public SessionModel(String sessionType, Integer counselorId) {
        this.sessionType = sessionType;
        this.counselorId = counselorId;
    }

    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }

    public Integer getCounselorId() { return counselorId; }
    public void setCounselorId(Integer counselorId) { this.counselorId = counselorId; }
}
