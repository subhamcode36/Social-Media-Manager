package com.socialmedia.dashboard;

import com.socialmedia.model.Post;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Dashboard {
    public static class ChartGenerator {
        public static void generateBarChart(String title, Map<String, Double> data) {
            System.out.println("Bar Chart: " + title);
            System.out.println("----------------------------------------");

            double maxValue = data.values().stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(1.0);

            for (Map.Entry<String, Double> entry : data.entrySet()) {
                String key = entry.getKey();
                double value = entry.getValue();
                int barLength = (int) (value / maxValue * 50);

                System.out.printf("%-15s | ", key);
                for (int i = 0; i < Math.max(1, barLength); i++) System.out.print("█");
                System.out.printf(" %.2f%n", value);
            }
            System.out.println();
        }
    }

    public static void displayProfileAnalytics(String profileName, Map<String, Object> results) {
        System.out.println("\nProfile Analysis: " + profileName);
        System.out.println("========================================");

        LocalTime optimalTime = (LocalTime) results.get("optimalPostingTime");
        System.out.println("Optimal Posting Time: " + optimalTime.format(DateTimeFormatter.ofPattern("HH:mm")));

        @SuppressWarnings("unchecked")
        Map<Post.ContentType, LocalTime> optimalTimeByType =
            (Map<Post.ContentType, LocalTime>) results.get("optimalPostingTimeByType");

        System.out.println("\nOptimal Posting Time by Content Type:");
        System.out.println("----------------------------------------");
        for (Map.Entry<Post.ContentType, LocalTime> entry : optimalTimeByType.entrySet()) {
            System.out.printf("%-6s: %s%n", entry.getKey(),
                             entry.getValue().format(DateTimeFormatter.ofPattern("HH:mm")));
        }

        @SuppressWarnings("unchecked")
        Map<Post.ContentType, Double> growthByType =
            (Map<Post.ContentType, Double>) results.get("followerGrowthByType");

        System.out.println("\nFollower Growth by Content Type:");
        Map<String, Double> growthData = Map.of(
            "PHOTO", growthByType.getOrDefault(Post.ContentType.PHOTO, 0.0),
            "VIDEO", growthByType.getOrDefault(Post.ContentType.VIDEO, 0.0),
            "REEL", growthByType.getOrDefault(Post.ContentType.REEL, 0.0),
            "TEXT", growthByType.getOrDefault(Post.ContentType.TEXT, 0.0)
        );
        ChartGenerator.generateBarChart("Average Follower Growth", growthData);

        @SuppressWarnings("unchecked")
        Map<String, Object> sentimentResults = (Map<String, Object>) results.get("sentimentAnalysis");

        @SuppressWarnings("unchecked")
        Map<Post.ContentType, Double> sentimentByType =
            (Map<Post.ContentType, Double>) sentimentResults.get("sentimentByContentType");

        System.out.println("\nSentiment Analysis by Content Type:");
        Map<String, Double> sentimentData = Map.of(
            "PHOTO", sentimentByType.getOrDefault(Post.ContentType.PHOTO, 0.0),
            "VIDEO", sentimentByType.getOrDefault(Post.ContentType.VIDEO, 0.0),
            "REEL", sentimentByType.getOrDefault(Post.ContentType.REEL, 0.0),
            "TEXT", sentimentByType.getOrDefault(Post.ContentType.TEXT, 0.0)
        );
        ChartGenerator.generateBarChart("Sentiment Score", sentimentData);
    }
     public static void displayRecommendations(String profileName, 
                                             List<String> hashtags, 
                                             List<String> captions) {
        System.out.println("\n=== Recommendations for " + profileName + " ===");
        System.out.println("\nTop Hashtags:");
        hashtags.forEach(hashtag -> System.out.println("  " + hashtag));
       
    }
}
