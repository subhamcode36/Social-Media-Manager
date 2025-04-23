package com.socialmedia.analytics;

import java.util.Map;

public interface Analytics {
    void processData();
    Map<String, Object> getResults();
}
