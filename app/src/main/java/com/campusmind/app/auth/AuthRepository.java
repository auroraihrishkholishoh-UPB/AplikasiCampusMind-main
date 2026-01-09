package com.campusmind.app.auth;

import android.content.Context;

import com.campusmind.app.data.api.ApiClient;
import com.campusmind.app.data.api.ApiService;

public class AuthRepository {

    private ApiService apiService;

    public AuthRepository(Context context) {
        apiService = ApiClient.getApiService(context);
    }

    public ApiService getApi() {
        return apiService;
    }
}
