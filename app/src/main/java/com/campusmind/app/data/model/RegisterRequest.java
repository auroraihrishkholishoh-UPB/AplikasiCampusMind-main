package com.campusmind.app.data.model;

public class RegisterRequest {
    private String name;
    private String nim;
    private String email;
    private String password;

    public RegisterRequest(String name, String nim, String email, String password) {
        this.name = name;
        this.nim = nim;
        this.email = email;
        this.password = password;
    }
}
