package com.campusmind.app.auth;

public class UserModel {
    private int id;
    private String name;
    private String email;
    private String password;
    private String nim;
    private String role;

    public UserModel() {}

    public UserModel(String name, String email, String password, String nim) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nim = nim;
    }

    // getter & setter

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
