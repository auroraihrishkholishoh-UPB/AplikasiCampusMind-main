package com.campusmind.app.counseling.chat_human;

import java.util.List;

public class HumanMessageResponse {
    private boolean success;
    private String message;
    private List<HumanMessageModel> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<HumanMessageModel> getData() { return data; }

    public void setSuccess(boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
    public void setData(List<HumanMessageModel> data) { this.data = data; }
}
