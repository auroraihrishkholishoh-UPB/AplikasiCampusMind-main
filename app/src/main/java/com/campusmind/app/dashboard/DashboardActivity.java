package com.campusmind.app.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.campusmind.app.R;
import com.campusmind.app.counseling.chat_ai.ChatAIActivity;
import com.campusmind.app.counseling.chat_human.ChatHumanActivity;
import com.campusmind.app.history.HistoryActivity;
import com.campusmind.app.profile.UserProfileActivity;

public class DashboardActivity extends AppCompatActivity {

    private CardView cardAiChat, cardHumanChat;
    private TextView tvGreeting, navHome, navHistory, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Card fitur utama
        cardAiChat = findViewById(R.id.card_ai_chat);
        cardHumanChat = findViewById(R.id.card_human_chat);

        // Bottom nav
        navHome = findViewById(R.id.nav_home);
        navHistory = findViewById(R.id.nav_history);
        navProfile = findViewById(R.id.nav_profile);

        // Greeting text
        tvGreeting = findViewById(R.id.tv_greeting);

        setupGreeting();
        setupClickListeners();
    }

    private void setupGreeting() {
        SharedPreferences prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String name = prefs.getString("name", "User");
        tvGreeting.setText("Halo, " + name + " 👋");
    }

    private void setupClickListeners() {
        cardAiChat.setOnClickListener(v ->
                startActivity(new Intent(this, ChatAIActivity.class)));

        cardHumanChat.setOnClickListener(v ->
                startActivity(new Intent(this, ChatHumanActivity.class)));

        navHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        navProfile.setOnClickListener(v ->
                startActivity(new Intent(this, UserProfileActivity.class)));
    }
}
