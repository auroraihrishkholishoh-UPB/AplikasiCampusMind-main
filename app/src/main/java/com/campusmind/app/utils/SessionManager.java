package com.campusmind.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.campusmind.app.auth.UserModel;

public class SessionManager {

    private static final String PREF_NAME = "campusmind_prefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_ROLE = "user_role";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveAuth(String token, UserModel user) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_ROLE, user.getRole());
        editor.apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }

    public void clearSession() {
        editor.clear().apply();
    }
}
