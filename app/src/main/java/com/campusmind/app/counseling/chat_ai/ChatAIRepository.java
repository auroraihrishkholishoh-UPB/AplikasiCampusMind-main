package com.campusmind.app.counseling.chat_ai;

import android.content.Context;

import com.campusmind.app.data.api.ApiClient;
import com.campusmind.app.data.api.ApiService;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ChatAIRepository {

    private final ApiService apiService;

    public ChatAIRepository(Context context) {
        apiService = ApiClient.getApiService(context);
    }

    // Dipakai ViewModel
    public Call<ResponseBody> sendAiMessage(String userId, String message) {

        JsonObject bodyJson = new JsonObject();
        bodyJson.addProperty("user_Id", userId);
        bodyJson.addProperty("message", message);

        RequestBody requestBody = RequestBody.create(
                bodyJson.toString(),
                MediaType.parse("application/json")
        );

        return apiService.sendAiMessage(requestBody);
    }
}
