package com.campusmind.app.assessment;

public class AssessmentModel {

    // contoh field yang masuk akal buat penilaian
    private int sessionId;
    private int rating;        // 1–5
    private String feedback;   // komentar dari user

    // WAJIB ada constructor kosong untuk Retrofit / Gson
    public AssessmentModel() {
    }

    public AssessmentModel(int sessionId, int rating, String feedback) {
        this.sessionId = sessionId;
        this.rating = rating;
        this.feedback = feedback;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
