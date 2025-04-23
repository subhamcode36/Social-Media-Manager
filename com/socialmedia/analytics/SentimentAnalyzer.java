package com.socialmedia.analytics;

import com.socialmedia.model.Post;
import java.util.*;
import java.util.stream.Collectors;

public class SentimentAnalyzer implements Analytics {
    private Map<String, Double> sentimentDictionary;
    private List<Post> posts;
    private Map<String, Object> results;

    public SentimentAnalyzer(List<Post> posts) {
        this.posts = posts;
        this.results = new HashMap<>();
        initializeSentimentDictionary();
    }

    private void initializeSentimentDictionary() {
        sentimentDictionary = new HashMap<>();
        sentimentDictionary.put("good", 1.0);
        sentimentDictionary.put("great", 1.5);
        sentimentDictionary.put("excellent", 2.0);
        sentimentDictionary.put("amazing", 2.0);
        sentimentDictionary.put("love", 1.5);
        sentimentDictionary.put("like", 1.0);
        sentimentDictionary.put("happy", 1.5);
        sentimentDictionary.put("awesome", 1.5);
        sentimentDictionary.put("bad", -1.0);
        sentimentDictionary.put("terrible", -2.0);
        sentimentDictionary.put("hate", -1.5);
        sentimentDictionary.put("dislike", -1.0);
        sentimentDictionary.put("awful", -1.5);
        sentimentDictionary.put("poor", -1.0);
        sentimentDictionary.put("sad", -1.0);
        sentimentDictionary.put("angry", -1.5);
    }

    @Override
    public void processData() {
        Map<Post.ContentType, Double> sentimentByType = new HashMap<>();
        Map<String, Double> profileSentiment = new HashMap<>();

        Map<Post.ContentType, List<Post>> postsByType = posts.stream()
                .collect(Collectors.groupingBy(Post::getContentType));

        for (Map.Entry<Post.ContentType, List<Post>> entry : postsByType.entrySet()) {
            double totalSentiment = 0;
            int count = 0;
            for (Post post : entry.getValue()) {
                double postSentiment = analyzeSentiment(post.getCommentTexts().toArray(new String[0]));
                totalSentiment += postSentiment;
                count++;
                profileSentiment.merge(post.getProfileName(), postSentiment, Double::sum);
            }
            sentimentByType.put(entry.getKey(), count > 0 ? totalSentiment / count : 0);
        }
        results.put("sentimentByContentType", sentimentByType);
        results.put("profileSentiment", profileSentiment);
    }

    @Override
    public Map<String, Object> getResults() {
        return results;
    }

    public double analyzeSentiment(String... comments) {
        if (comments.length == 0) {
            return 0.0;
        }
        double totalSentiment = 0.0;
        for (String comment : comments) {
            totalSentiment += analyzeSingleComment(comment);
        }
        return totalSentiment / comments.length;
    }

    private double analyzeSingleComment(String comment) {
        if (comment == null || comment.isEmpty()) {
            return 0.0;
        }
        String[] words = comment.toLowerCase().replaceAll("[^a-zA-Z\\s]", "").split("\\s+");
        double sentimentScore = 0.0;
        int wordCount = 0;
        for (String word : words) {
            if (sentimentDictionary.containsKey(word)) {
                sentimentScore += sentimentDictionary.get(word);
                wordCount++;
            }
        }
        return wordCount > 0 ? sentimentScore / wordCount : 0.0;
    }
}
