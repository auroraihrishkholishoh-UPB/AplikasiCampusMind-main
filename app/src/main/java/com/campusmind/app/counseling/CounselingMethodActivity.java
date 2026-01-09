package com.campusmind.app.counseling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.campusmind.app.R;
import com.campusmind.app.counseling.chat_ai.ChatAIActivity;
import com.campusmind.app.counseling.chat_human.ChatHumanActivity;

public class CounselingMethodActivity extends AppCompatActivity {

    private Button btnChatHuman, btnChatAI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counseling_method);

        btnChatHuman = findViewById(R.id.btn_chat_human);
        btnChatAI = findViewById(R.id.btn_chat_ai);

        btnChatHuman.setOnClickListener(v ->
                startActivity(new Intent(this, ChatHumanActivity.class)));

        btnChatAI.setOnClickListener(v ->
                startActivity(new Intent(this, ChatAIActivity.class)));
    }
}
