package com.socialmedia.model;

public class MarketingAnalyst extends User {
    public MarketingAnalyst(String username, String password, String email) {
        super(username, password, email);
        this.role = Role.MARKETING_ANALYST;
    }

    @Override
    public boolean hasPermission(String action) {
        return action.equals("view_analytics") || action.equals("export_report");
    }
}
