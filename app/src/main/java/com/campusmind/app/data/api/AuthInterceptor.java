package com.campusmind.app.data.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        SharedPreferences prefs =
                context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);

        String token = prefs.getString("token", null);

        Request original = chain.request();
        Request.Builder builder = original.newBuilder();

        if (token != null && !token.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        return chain.proceed(builder.build());
    }
}
