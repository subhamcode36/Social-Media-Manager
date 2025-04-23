package com.socialmedia.model;

public class ContentCreator extends User {
    public ContentCreator(String username, String password, String email) {
        super(username, password, email);
        this.role = Role.CONTENT_CREATOR;
    }

    @Override
    public boolean hasPermission(String action) {
        return action.equals("create_post") || action.equals("schedule_post");
    }
}
