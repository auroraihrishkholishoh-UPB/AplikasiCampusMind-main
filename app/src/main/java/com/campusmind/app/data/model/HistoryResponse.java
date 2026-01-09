package com.campusmind.app.data.model;

import java.util.List;

public class HistoryResponse {
    public boolean success;
    public List<Session> sessions;

    public static class Session {
        public int id;
        public int user_id;
        public Integer counselor_id;
        public String session_type;
        public String status;
        public String started_at;
        public String ended_at;
        public String created_at;
        public String counselor_name;
        public List<Message> messages;
        public Assessment assessment;
    }

    public static class Message {
        public int id;
        public int session_id;
        public String sender_type;
        public Integer sender_id;
        public String message_type;
        public String text_content;
        public String audio_url;
        public String created_at;
    }

    public static class Assessment {
        public int id;
        public int session_id;
        public int user_id;
        public int rating;
        public String feedback;
        public String created_at;
    }
}
