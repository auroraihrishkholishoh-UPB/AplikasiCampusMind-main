package com.campusmind.app.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.campusmind.app.R;
import com.campusmind.app.dashboard.DashboardActivity;
import com.campusmind.app.data.api.ApiClient;
import com.campusmind.app.data.api.ApiService;
import com.campusmind.app.data.model.LoginRequest;
import com.campusmind.app.data.model.LoginResponse;
import com.campusmind.app.data.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView btnGoSignup;

    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnGoSignup = findViewById(R.id.btn_go_signup);

        apiService = ApiClient.getApiService(this);


        btnLogin.setOnClickListener(v -> doLogin());

        btnGoSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void doLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email wajib diisi");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password wajib diisi");
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        LoginRequest body = new LoginRequest(email, password);

        apiService.login(body).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call,
                                   Response<LoginResponse> response) {

                btnLogin.setEnabled(true);
                btnLogin.setText("Login");

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(LoginActivity.this,
                            "Login gagal (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginResponse res = response.body();
                if (!res.isSuccess()) {
                    Toast.makeText(LoginActivity.this,
                            res.getMessage() != null ? res.getMessage() : "Login gagal",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = res.getUser();
                String name = (user != null && user.getName() != null)
                        ? user.getName()
                        : extractNameFromEmail(email);

                SharedPreferences prefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userId", user != null ? user.getId() : null);
                editor.putString("name", name);
                editor.putString("email", email);
                if (res.getToken() != null) editor.putString("token", res.getToken());
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Login");
                Toast.makeText(LoginActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String extractNameFromEmail(String email) {
        if (email == null || !email.contains("@")) return "User";
        String part = email.substring(0, email.indexOf("@"));
        if (part.isEmpty()) return "User";
        return part.substring(0, 1).toUpperCase() + part.substring(1);
    }
}
