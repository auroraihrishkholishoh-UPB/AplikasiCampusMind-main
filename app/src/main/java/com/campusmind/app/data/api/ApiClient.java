package com.campusmind.app.data.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // ✅ Emulator Android
    public static final String BASE_URL = "http://127.0.0.1:5000/";

    private static Retrofit retrofit = null;

    public static ApiService getApiService(Context context) {

        if (retrofit == null) {

            // ⚠️ HARUS SAMA dengan LoginActivity
            SharedPreferences prefs =
                    context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(prefs))
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ApiService.class);
    }

    // =========================
    // AUTH INTERCEPTOR
    // =========================
    static class AuthInterceptor implements Interceptor {

        private final SharedPreferences prefs;

        AuthInterceptor(SharedPreferences prefs) {
            this.prefs = prefs;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            String token = prefs.getString("token", null);

            if (token != null && !token.isEmpty()) {
                Request request = original.newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(request);
            }

            return chain.proceed(original);
        }
    }
}
