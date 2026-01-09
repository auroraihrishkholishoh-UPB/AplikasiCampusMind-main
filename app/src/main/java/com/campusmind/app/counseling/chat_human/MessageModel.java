package com.campusmind.app.counseling.chat_human;

public class MessageModel {
    private String sender;     // "user" atau "counselor"
    private String text;       // null kalau voice note
    private String audioPath;  // null kalau text biasa
    private long timestamp;

    // Constructor text
    public MessageModel(String sender, String text, long timestamp) {
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
    }

    // Constructor voice note
    public static MessageModel createVoiceNote(String sender, String audioPath, long timestamp) {
        MessageModel m = new MessageModel(sender, null, timestamp);
        m.audioPath = audioPath;
        return m;
    }

    public boolean isVoiceNote() {
        return audioPath != null && !audioPath.isEmpty();
    }

    public String getSender() { return sender; }
    public String getText() { return text; }
    public String getAudioPath() { return audioPath; }
    public long getTimestamp() { return timestamp; }
}
