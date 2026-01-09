package com.campusmind.app.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.campusmind.app.R;
import com.campusmind.app.counseling.chat_human.ChatHumanActivity;

public class CounselorProfileActivity extends AppCompatActivity {

    private TextView txtCounselorName, txtCounselorRole, txtCounselorExperience;
    private Button btnStartCounseling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor_profile);

        txtCounselorName = findViewById(R.id.txt_counselor_name);
        txtCounselorRole = findViewById(R.id.txt_counselor_role);
        txtCounselorExperience = findViewById(R.id.txt_counselor_experience);
        btnStartCounseling = findViewById(R.id.btn_start_counseling);

        // Dummy data
        txtCounselorName.setText("Rina Putri, M.Psi.");
        txtCounselorRole.setText("Psikolog Klinis");
        txtCounselorExperience.setText("5 tahun mendampingi mahasiswa");

        btnStartCounseling.setOnClickListener(v -> {
            Intent intent = new Intent(CounselorProfileActivity.this, ChatHumanActivity.class);
            startActivity(intent);
        });
    }
}
