package com.socialmedia.platform;

import com.socialmedia.model.Post;
import java.util.Map;

public class FacebookPlatform implements Platform {
    private boolean connected;
    private String apiKey;

    @Override
    public void connect(String apiKey) {
        this.apiKey = apiKey;
        this.connected = true;
        System.out.println("Connected to Facebook API");
    }

    @Override
    public void disconnect() {
        this.connected = false;
        System.out.println("Disconnected from Facebook API");
    }

    @Override
    public boolean postContent(Post post) {
        if (!connected) return false;
        System.out.println("Posted content to Facebook: " + post.getPostId());
        return true;
    }

    @Override
    public Map<String, Integer> getMetrics(Post post) {
        return Map.of("likes", post.getLikes(), 
                     "shares", post.getShares(),
                     "comments", post.getComments());
    }
}
