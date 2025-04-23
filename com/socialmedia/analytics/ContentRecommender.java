package com.socialmedia.analytics;

import com.socialmedia.model.Post;
import java.util.*;
import java.util.stream.Collectors;

public class ContentRecommender {
    private List<Post> posts;

    public ContentRecommender(List<Post> posts) {
        this.posts = posts;
    }

    // Recommend top N hashtags from high-engagement posts
    public List<String> recommendHashtags(int maxHashtags) {
        Map<String, Double> hashtagScores = new HashMap<>();
        for (Post post : posts) {
            double engagement = calculateEngagement(post);
            List<String> hashtags = extractHashtags(post.getCommentTexts());
            for (String hashtag : hashtags) {
                hashtagScores.put(hashtag, hashtagScores.getOrDefault(hashtag, 0.0) + engagement);
            }
        }
        return hashtagScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(maxHashtags)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Recommend captions from top-performing posts (using longest comment as a proxy)
    public List<String> recommendCaptions(int maxCaptions) {
        return posts.stream()
                .sorted(Comparator.comparingDouble(this::calculateEngagement).reversed())
                .limit(10)
                .map(this::generateCaptionFromPost)
                .filter(caption -> caption != null && !caption.isEmpty())
                .distinct()
                .limit(maxCaptions)
                .collect(Collectors.toList());
    }

    // Extract hashtags from a list of comments
    private List<String> extractHashtags(List<String> comments) {
        return comments.stream()
                .flatMap(comment -> Arrays.stream(comment.split("\\s+")))
                .filter(word -> word.startsWith("#") && word.length() > 1)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    // Engagement formula (customizable)
    private double calculateEngagement(Post post) {
        return post.getLikes() * 0.5 + post.getComments() * 2.0 + post.getShares() * 1.0;
    }

    // Generate a caption suggestion from the longest comment
    private String generateCaptionFromPost(Post post) {
        Optional<String> topComment = post.getCommentTexts().stream()
                .max(Comparator.comparingInt(String::length));
        return topComment.map(comment ->
                comment.substring(0, Math.min(comment.length(), 50)) + (comment.length() > 50 ? "..." : "")
        ).orElse("");
    }
}
