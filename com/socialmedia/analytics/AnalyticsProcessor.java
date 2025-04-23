package com.socialmedia.analytics;

import com.socialmedia.model.Post;
import com.socialmedia.model.Profile;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsProcessor implements Runnable {
    private Profile profile;
    private Map<String, Object> results;
    private SentimentAnalyzer sentimentAnalyzer;
    private ContentRecommender contentRecommender; // NEW
    private List<String> recommendedHashtags; // NEW
    private List<String> recommendedCaptions; // NEW

    public AnalyticsProcessor(Profile profile) {
        this.profile = profile;
        this.results = new HashMap<>();
        this.sentimentAnalyzer = new SentimentAnalyzer(profile.getPosts());
    }

    @Override
    public void run() {
        try {
            LocalTime optimalTime = profile.calculateOptimalPostingTime();
            results.put("optimalPostingTime", optimalTime);

            Map<Post.ContentType, LocalTime> optimalTimeByType = new HashMap<>();
            for (Post.ContentType type : Post.ContentType.values()) {
                optimalTimeByType.put(type, profile.calculateOptimalPostingTime(type));
            }
            results.put("optimalPostingTimeByType", optimalTimeByType);

            Map<Post.ContentType, Double> growthByType = profile.analyzeFollowerGrowthByContentType();
            results.put("followerGrowthByType", growthByType);

            sentimentAnalyzer.processData();
            results.put("sentimentAnalysis", sentimentAnalyzer.getResults());

            // NEW: Generate recommendations
            this.contentRecommender = new ContentRecommender(profile.getPosts());
            this.recommendedHashtags = contentRecommender.recommendHashtags(5);
            this.recommendedCaptions = contentRecommender.recommendCaptions(3);

        } catch (Exception e) {
            e.printStackTrace();
            results.put("error", e.getMessage());
        }
    }

    public Map<String, Object> getResults() {
        return results;
    }

    public String getProfileName() {
        return profile.getName();
    }

    // NEW GETTERS
    public List<String> getRecommendedHashtags() {
        return recommendedHashtags;
    }

    public List<String> getRecommendedCaptions() {
        return recommendedCaptions;
    }
}
