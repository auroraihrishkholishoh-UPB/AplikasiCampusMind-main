package com.campusmind.app.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.campusmind.app.R;
import com.campusmind.app.dashboard.DashboardActivity;
import com.campusmind.app.data.api.ApiClient;
import com.campusmind.app.data.api.ApiService;
import com.campusmind.app.data.model.HistoryResponse;
import com.campusmind.app.profile.UserProfileActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private View navHome, navHistory, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        rvHistory = findViewById(R.id.rv_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        navHome = findViewById(R.id.nav_home);
        navHistory = findViewById(R.id.nav_history);
        navProfile = findViewById(R.id.nav_profile);

        setupClickListeners();
        fetchHistory();
    }

    private void fetchHistory() {
        ApiService api = ApiClient.getApiService(this);

        api.getHistory().enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<HistoryResponse.Session> apiSessions = response.body().sessions;

                    if (apiSessions != null && !apiSessions.isEmpty()) {
                        List<HistorySession> sessions = new ArrayList<>();

                        for (HistoryResponse.Session s : apiSessions) {
                            HistorySession hs = new HistorySession();
                            hs.id = s.id;
                            hs.session_type = s.session_type;
                            hs.status = s.status;
                            hs.started_at = s.started_at;

                            if (s.assessment != null) {
                                HistorySession.Assessment a = new HistorySession.Assessment();
                                a.rating = s.assessment.rating;
                                hs.assessment = a;
                            }

                            if (s.messages != null) {
                                List<HistorySession.Message> msgs = new ArrayList<>();
                                for (HistoryResponse.Message m : s.messages) {
                                    HistorySession.Message msg = new HistorySession.Message();
                                    msg.text_content = m.text_content;
                                    msgs.add(msg);
                                }
                                hs.messages = msgs;
                            }

                            sessions.add(hs);
                        }

                        rvHistory.setAdapter(new HistoryAdapter(sessions));

                    } else {
                        Toast.makeText(HistoryActivity.this, "Tidak ada session tersedia", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(HistoryActivity.this, "Response gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {
                Toast.makeText(HistoryActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        navHome.setOnClickListener(v ->
                startActivity(new Intent(this, DashboardActivity.class)));

        navHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        navProfile.setOnClickListener(v ->
                startActivity(new Intent(this, UserProfileActivity.class)));
    }
}
