package com.campusmind.app.counseling.chat_human;

import android.content.Context;

import com.campusmind.app.data.api.ApiClient;
import com.campusmind.app.data.api.ApiService;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ChatHumanRepository {

    private final ApiService apiService;

    public ChatHumanRepository(Context context) {
        apiService = ApiClient.getApiService(context);
    }

    // Kirim pesan teks ke konselor
    public Call<ResponseBody> sendHumanMessage(String sessionId,
                                               String sender,
                                               String message) {
        JsonObject body = new JsonObject();
        body.addProperty("sessionId", sessionId);
        body.addProperty("sender", sender);
        body.addProperty("message", message);

        RequestBody reqBody = RequestBody.create(
                body.toString(),
                MediaType.parse("application/json")
        );

        return apiService.sendHumanMessage(reqBody);
    }

    // Kirim voice note ke konselor
    public Call<ResponseBody> sendHumanVoice(MultipartBody.Part audio,
                                             RequestBody sessionId,
                                             RequestBody sender) {
        return apiService.sendHumanVoice(audio, sessionId, sender);
    }
}
