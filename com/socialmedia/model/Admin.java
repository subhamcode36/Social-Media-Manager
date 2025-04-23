package com.socialmedia.model;

public class Admin extends User {
    public Admin(String username, String password, String email) {
        super(username, password, email);
        this.role = Role.ADMIN;
    }

    @Override
    public boolean hasPermission(String action) {
        return true;
    }
}
