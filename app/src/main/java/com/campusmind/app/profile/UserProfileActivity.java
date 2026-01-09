package com.campusmind.app.profile;

import static com.campusmind.app.R.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.campusmind.app.R;
import com.campusmind.app.auth.LoginActivity;
import com.campusmind.app.dashboard.DashboardActivity;
import com.campusmind.app.data.api.ApiClient;
import com.campusmind.app.data.api.ApiService;
import com.campusmind.app.data.model.User;
import com.campusmind.app.history.HistoryActivity;
import com.campusmind.app.utils.FileUtils;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvNim, tvRole;
    private ImageView imgAvatar;
    private Button btnLogout, btnEditProfile, btnTakePhoto;
    private TextView navHome, navHistory, navProfile;

    private String idUser;

    // Gallery picker
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            if (imageUri != null) {
                                imgAvatar.setImageURI(imageUri);
                                uploadAvatar(imageUri);
                            }
                        }
                    });

    // Camera launcher
    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Bundle extras = result.getData().getExtras();
                            if (extras != null) {
                                Bitmap bitmap = (Bitmap) extras.get("data");
                                if (bitmap != null) {
                                    imgAvatar.setImageBitmap(bitmap);

                                    File file = FileUtils.bitmapToFile(this, bitmap, "avatar_temp.jpg");
                                    if (file != null) uploadAvatar(Uri.fromFile(file));
                                }
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Inisialisasi view
        tvName = findViewById(R.id.tv_profile_name);
        tvEmail = findViewById(R.id.tv_profile_email);
        tvNim = findViewById(R.id.tv_profile_nim);
        tvRole = findViewById(R.id.tv_profile_role);
        imgAvatar = findViewById(R.id.img_avatar);
        btnLogout = findViewById(R.id.btn_logout);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnTakePhoto = findViewById(R.id.btn_take_photo);

        navHome = findViewById(R.id.nav_home);
        navHistory = findViewById(R.id.nav_history);
        navProfile = findViewById(R.id.nav_profile);

        setupClickListeners();

        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        idUser = pref.getString("userId", "");

        if (idUser == null || idUser.isEmpty()) {
            Toast.makeText(this, "Session habis, silakan login ulang", Toast.LENGTH_SHORT).show();
            logout();
            return;
        }

        loadUserData();

        // Klik avatar -> pilih dari galeri
        imgAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        // Ambil foto dari kamera
        btnTakePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(intent);
        });

        // Edit profil
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("userId", idUser);
            intent.putExtra("name", tvName.getText().toString());
            intent.putExtra("email", tvEmail.getText().toString());
            startActivity(intent);
        });

        // Logout
        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadUserData() {
        ApiService api = ApiClient.getApiService(this);

        JSONObject json = new JSONObject();
        try { json.put("id", idUser); } catch (Exception ignored) {}

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json.toString()
        );

        api.getUserProfile(body).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    tvName.setText(user.getName());
                    tvEmail.setText(user.getEmail());
                    tvNim.setText("NIM: " + user.getNim());
                    tvRole.setText("Role: " + user.getRole());

                    if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                        Glide.with(UserProfileActivity.this)
                                .load(ApiClient.BASE_URL + "uploads/avatars/" + user.getAvatar())
                                .placeholder(R.drawable.ic_avatar_default)
                                .error(R.drawable.ic_avatar_default)
                                .circleCrop()
                                .into(imgAvatar);
                    } else {
                        imgAvatar.setImageResource(R.drawable.ic_avatar_default);
                    }

                } else {
                    Toast.makeText(UserProfileActivity.this, "Data user kosong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadAvatar(Uri imageUri) {
        File file = FileUtils.getFile(this, imageUri);
        if (file == null) {
            Toast.makeText(this, "Gagal membaca file gambar", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part avatar = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
        RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), idUser);

        ApiService api = ApiClient.getApiService(this);
        api.uploadAvatar(avatar, userIdBody)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(UserProfileActivity.this, "Avatar berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            loadUserData();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "Upload gagal", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(UserProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void logout() {
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        pref.edit().clear().apply();
        startActivity(new Intent(this, LoginActivity.class));
        finishAffinity();
    }

    private void setupClickListeners() {
        navHome.setOnClickListener(v -> startActivity(new Intent(this, DashboardActivity.class)));
        navHistory.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, UserProfileActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }
}
