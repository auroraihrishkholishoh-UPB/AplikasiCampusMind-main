package com.campusmind.app.history;

import java.util.List;

public class HistorySession {
    public int id;
    public String session_type;
    public String status;
    public String started_at;
    public Assessment assessment;
    public List<Message> messages;

    public static class Assessment {
        public int rating;
    }

    public static class Message {
        public String text_content;
    }
}
