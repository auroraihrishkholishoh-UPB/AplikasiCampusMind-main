package com.campusmind.app.counseling.chat_ai;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.lifecycle.ViewModelProvider;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;


import com.campusmind.app.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAIActivity extends AppCompatActivity {

    private RecyclerView rvChatAI;
    private EditText etMessageAI;
    private ImageButton btnSendAI;

    private SimpleChatAdapter adapter;
    private final List<String> messages = new ArrayList<>();

    private ChatAIViewModel viewModel;
    private String userId = "1";

    // untuk tracking "AI sedang mengetik..."
    private int typingIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_ai);

        rvChatAI = findViewById(R.id.rv_chat_ai);
        etMessageAI = findViewById(R.id.et_message_ai);
        btnSendAI = findViewById(R.id.btn_send_ai);

        viewModel = new ViewModelProvider(this).get(ChatAIViewModel.class);


        SharedPreferences prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String savedId = prefs.getString("userId", null);
        if (savedId != null && !savedId.isEmpty()) {
            userId = savedId;
        }

        adapter = new SimpleChatAdapter(messages);
        rvChatAI.setLayoutManager(new LinearLayoutManager(this));
        rvChatAI.setAdapter(adapter);

        btnSendAI.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String text = etMessageAI.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        // tampilkan pesan user
        messages.add("You: " + text);
        adapter.notifyItemInserted(messages.size() - 1);
        rvChatAI.scrollToPosition(messages.size() - 1);
        etMessageAI.setText("");

        // tampilkan indikator AI mengetik
        messages.add("AI sedang mengetik...");
        typingIndex = messages.size() - 1;
        adapter.notifyItemInserted(typingIndex);
        rvChatAI.scrollToPosition(typingIndex);

        viewModel.sendAiMessage(userId, text).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {

                String replyText;

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String raw = response.body().string();
                        JSONObject json = new JSONObject(raw);
                        replyText = "AI: " + json.getString("reply");
                    } catch (Exception e) {
                        replyText = "AI: Maaf, aku lagi kesulitan menjawab sekarang.";
                    }
                } else {
                    replyText = "AI: Maaf, server lagi bermasalah.";
                }
                Log.e("AI_ERROR", "Code: " + response.code());
                replaceTypingWithReply(replyText);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                replaceTypingWithReply(
                        "AI: Maaf, koneksi lagi bermasalah. Tapi kamu tetap boleh cerita di sini."
                );
            }
        });
    }
    private void replaceTypingWithReply(String replyText) {
        runOnUiThread(() -> {
            if (typingIndex != -1 && typingIndex < messages.size()) {
                messages.set(typingIndex, replyText);
                adapter.notifyItemChanged(typingIndex);
                rvChatAI.scrollToPosition(typingIndex);
            } else {
                messages.add(replyText);
                adapter.notifyItemInserted(messages.size() - 1);
                rvChatAI.scrollToPosition(messages.size() - 1);
            }
            typingIndex = -1;
        });
    }
}
