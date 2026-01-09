package com.campusmind.app.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.campusmind.app.R;
import com.campusmind.app.data.api.ApiClient;
import com.campusmind.app.data.api.ApiService;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail;
    private Button btnSave;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        btnSave = findViewById(R.id.btn_save);

        userId = getIntent().getStringExtra("userId");
        etName.setText(getIntent().getStringExtra("name"));
        etEmail.setText(getIntent().getStringExtra("email"));

        btnSave.setOnClickListener(v -> {
            if (validateInput()) {
                updateProfile();
            }
        });
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(etName.getText())) {
            etName.setError("Nama wajib diisi");
            return false;
        }

        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError("Email wajib diisi");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {
            etEmail.setError("Format email tidak valid");
            return false;
        }

        return true;
    }

    private void updateProfile() {
        try {
            JSONObject json = new JSONObject();
            json.put("id", userId);
            json.put("name", etName.getText().toString());
            json.put("email", etEmail.getText().toString());

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    json.toString()
            );

            ApiService api = ApiClient.getApiService(this);
            api.updateProfile(body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EditProfileActivity.this,
                                "Profil berhasil diperbarui",
                                Toast.LENGTH_SHORT).show();
                        finish(); // kembali ke Profile
                    } else {
                        Toast.makeText(EditProfileActivity.this,
                                "Gagal update profil",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(EditProfileActivity.this,
                            "Error: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
