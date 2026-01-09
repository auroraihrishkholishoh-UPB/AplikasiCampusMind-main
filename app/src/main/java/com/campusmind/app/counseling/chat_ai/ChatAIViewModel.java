package com.campusmind.app.counseling.chat_ai;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * ViewModel sederhana untuk fitur Chat AI.
 * Belum pakai LiveData supaya tetap simple.
 */
public class ChatAIViewModel extends AndroidViewModel {

    private final ChatAIRepository repository;

    public ChatAIViewModel(@NonNull Application application) {
        super(application);
        repository = new ChatAIRepository(application);
    }

    public Call<ResponseBody> sendAiMessage(String userId, String message) {
        return repository.sendAiMessage(userId, message);
    }
}
