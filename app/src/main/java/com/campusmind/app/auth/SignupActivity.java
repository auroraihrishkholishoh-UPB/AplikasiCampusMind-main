package com.campusmind.app.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.campusmind.app.R;
import com.campusmind.app.data.api.ApiClient;
import com.campusmind.app.data.api.ApiService;
import com.campusmind.app.data.model.RegisterRequest;
import com.campusmind.app.data.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etNim, etEmail, etPassword;
    private Button btnSignup;

    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.et_name);
        etNim = findViewById(R.id.et_nim);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignup = findViewById(R.id.btn_signup);

        apiService = ApiClient.getApiService(this);

        btnSignup.setOnClickListener(v -> doRegister());
    }

    private void doRegister() {
        String name = etName.getText().toString().trim();
        String nim = etNim.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Nama wajib diisi");
            return;
        }
        if (TextUtils.isEmpty(nim)) {
            etNim.setError("NIM wajib diisi");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email wajib diisi");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password wajib diisi");
            return;
        }

        btnSignup.setEnabled(false);
        btnSignup.setText("Mendaftar...");

        RegisterRequest body = new RegisterRequest(name, nim, email, password);

        apiService.register(body).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call,
                                   Response<RegisterResponse> response) {

                btnSignup.setEnabled(true);
                btnSignup.setText("Sign Up");

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(SignupActivity.this,
                            "Registrasi gagal (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                RegisterResponse res = response.body();
                if (!res.isSuccess()) {
                    Toast.makeText(SignupActivity.this,
                            res.getMessage() != null ? res.getMessage() : "Registrasi gagal",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(SignupActivity.this,
                        "Registrasi berhasil, silakan login",
                        Toast.LENGTH_SHORT).show();

                finish(); // balik ke Login
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                btnSignup.setEnabled(true);
                btnSignup.setText("Sign Up");
                Toast.makeText(SignupActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
