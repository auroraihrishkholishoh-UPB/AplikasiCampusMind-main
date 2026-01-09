package com.campusmind.app.data.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private String id;

    @SerializedName("nama")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("nim")
    private String nim;

    @SerializedName("role")
    private String role;

    @SerializedName("avatar")
    private String avatar;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getNim() { return nim; }
    public String getRole() { return role; }
    public String getAvatar() { return avatar; }
}
