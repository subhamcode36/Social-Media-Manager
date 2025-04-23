package com.socialmedia.platform;

import com.socialmedia.model.Post;
import java.util.Map;

public interface Platform {
    void connect(String apiKey);
    void disconnect();
    boolean postContent(Post post);
    Map<String, Integer> getMetrics(Post post);
}
