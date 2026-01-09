package com.campusmind.app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.campusmind.app.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Nanti lo bisa bikin layout splash sendiri: activity_main.xml
        setContentView(R.layout.activity_main);

        // Sementara: langsung pindah ke Login
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
