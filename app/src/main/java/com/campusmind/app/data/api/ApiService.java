package com.campusmind.app.data.api;

import com.campusmind.app.data.model.LoginRequest;
import com.campusmind.app.data.model.LoginResponse;
import com.campusmind.app.data.model.RegisterRequest;
import com.campusmind.app.data.model.RegisterResponse;
import com.campusmind.app.data.model.User;
import com.campusmind.app.data.model.HistoryResponse;
import com.campusmind.app.data.model.ApiResponse;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ApiService {

    // AUTH
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);

    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest body);

    // AI CHAT
    @POST("api/ai/message")
    Call<ResponseBody> sendAiMessage(@Body RequestBody body);
        // HUMAN MESSAGE
@POST("api/messages/human")
Call<ResponseBody> sendHumanMessage(@Body RequestBody body);

// HUMAN VOICE MESSAGE
@Multipart
@POST("api/messages/human-voice")
Call<ResponseBody> sendHumanVoice(
    @Part MultipartBody.Part audio,
    @Part("sessionId") RequestBody sessionId,
    @Part("sender") RequestBody sender
);


    // MESSAGES (TEXT & VOICE)
    @POST("api/messages/sessions/{sessionId}/messages")
    Call<ResponseBody> sendMessage(
        @Path("sessionId") int sessionId,
        @Body RequestBody body
    );

    // HISTORY
    @GET("api/sessions")
Call<HistoryResponse> getHistory();



    // USER PROFILE
    @POST("api/users/profile")
    Call<User> getUserProfile(@Body RequestBody body);

    // UPLOAD AVATAR
    @Multipart
    @POST("api/users/upload-avatar")
    Call<ResponseBody> uploadAvatar(
        @Part MultipartBody.Part avatar,
        @Part("userId") RequestBody userId
    );
    @POST("api/users/update-profile")
    Call<ResponseBody> updateProfile(@Body RequestBody body);

}
